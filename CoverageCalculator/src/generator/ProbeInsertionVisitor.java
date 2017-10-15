package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.StatementMetaModel;
import probes.PROBE_TYPE;
import probes.Probe;

import javax.swing.plaf.nimbus.State;
import java.util.*;

public class ProbeInsertionVisitor extends VoidVisitorAdapter {

    private long nextProbeId = 0;
    private Map<Long, Probe> probes = new HashMap<>();

    private Statement generateProbeCall(PROBE_TYPE type, String name, int nStatements) {
        Probe probe = new Probe(nextProbeId, type, name, nStatements);
        String probeCall = "TraceLogger.getInstance().logProbe(" + nextProbeId + ");";
        Statement stmt = JavaParser.parseStatement(probeCall);
        probes.put(nextProbeId, probe);
        nextProbeId++;
        return stmt;
    }

    private void insertMethodStartProbe(BlockStmt body, String name) {
        Statement stmt = generateProbeCall(PROBE_TYPE.METHOD_START, name, 0);
        body.addStatement(0, stmt);
    }

    private void insertBlockEndProbe(BlockStmt block, String name, int nStatements) {
        Statement stmt = generateProbeCall(PROBE_TYPE.BLOCK_END, name, nStatements);
        block.addStatement(stmt);
    }

    private boolean isCompoundStatement(Statement stmt) {
        if (stmt instanceof IfStmt || stmt instanceof ForStmt) { // TODO
            return true;
        }
        return false;
    }

    private boolean isTerminatingStatement(Statement stmt) {
        if (stmt instanceof ReturnStmt || stmt instanceof ThrowStmt) { // TODO
            return true;
        }
        return false;
    }

    private Collection<BlockStmt> getBlocksFromCompoundStatement(Statement stmt) {
        Set<BlockStmt> blocks = new HashSet<>();
        if (stmt instanceof IfStmt) {
            IfStmt ifStmt = stmt.asIfStmt();
            blocks.add(ifStmt.getThenStmt().asBlockStmt());
            if (ifStmt.hasElseBlock()) {
                Statement elseStmt = ifStmt.getElseStmt().get();
                if (isCompoundStatement(elseStmt)) {
                    blocks.addAll(getBlocksFromCompoundStatement(elseStmt));
                } else {
                    blocks.add(elseStmt.asBlockStmt());
                }
            }
        }
        return blocks;
    }

    private void insertProbes(BlockStmt block, String name) {
        NodeList<Statement> statements = new NodeList<>(block.getStatements());
        int n = 0;
        BlockStmt modified = new BlockStmt();
        for (Statement stmt : statements) {
            n++;
            if (isCompoundStatement(stmt) || isTerminatingStatement(stmt)) {
                insertBlockEndProbe(modified, name, n);
                n = 0;
            }
            if (isCompoundStatement(stmt)) {
                for (BlockStmt b : getBlocksFromCompoundStatement(stmt)) {
                    insertProbes(b, name);
                }
            }
            modified.addStatement(stmt);
        }
        if (n > 0) {
            insertBlockEndProbe(modified, name, n);
        }
        block.setStatements(modified.getStatements());
    }

    private void insertProbesIntoMethod(BlockStmt body, String name) {
        insertProbes(body, name);
        insertMethodStartProbe(body, name);
    }

    public void visit(MethodDeclaration method, Object arg) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = method.getSignature().asString();
        insertProbesIntoMethod(body, signature);
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        BlockStmt body = constructor.getBody();
        String signature = constructor.getSignature().asString();
        insertProbesIntoMethod(body, signature);
    }

    public Map<Long, Probe> getProbes() {
        return probes;
    }
}
