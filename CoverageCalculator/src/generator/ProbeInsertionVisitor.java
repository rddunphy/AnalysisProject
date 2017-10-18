package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import probes.Probe;
import probes.ProbeFactory;

import java.util.*;

class ProbeInsertionVisitor extends VoidVisitorAdapter<String> {

    private Statement generateProbeCall(Probe probe) {
        String probeCall = "Trace.getInstance().logProbe(" + probe.getId() + ");";
        return JavaParser.parseStatement(probeCall);
    }

    private void insertBlockEndProbe(BlockStmt block, String signature, int nStatements) {
        Probe probe = ProbeFactory.createBlockEndProbe(signature, nStatements);
        Statement stmt = generateProbeCall(probe);
        block.addStatement(stmt);
    }

    private boolean isCompoundStatement(Statement stmt) {
        return stmt instanceof IfStmt || stmt instanceof ForStmt;
    }

    private boolean isTerminatingStatement(Statement stmt) {
        return stmt instanceof ReturnStmt || stmt instanceof ThrowStmt;
    }

    private Collection<BlockStmt> getBlocksFromCompoundStatement(Statement stmt) {
        Set<BlockStmt> blocks = new HashSet<>();
        if (stmt instanceof IfStmt) {
            IfStmt ifStmt = stmt.asIfStmt();
            blocks.add(ifStmt.getThenStmt().asBlockStmt());
            if (ifStmt.hasElseBlock() && ifStmt.getElseStmt().isPresent()) {
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

    private void insertProbes(BlockStmt block, String signature) {
        NodeList<Statement> statements = new NodeList<>(block.getStatements());
        int n = 0;
        BlockStmt modified = new BlockStmt();
        for (Statement stmt : statements) {
            n++;
            if (isCompoundStatement(stmt) || isTerminatingStatement(stmt)) {
                insertBlockEndProbe(modified, signature, n);
                n = 0;
            }
            if (isCompoundStatement(stmt)) {
                for (BlockStmt b : getBlocksFromCompoundStatement(stmt)) {
                    insertProbes(b, signature);
                }
            }
            modified.addStatement(stmt);
        }
        if (n > 0) {
            insertBlockEndProbe(modified, signature, n);
        }
        block.setStatements(modified.getStatements());
    }

    public void visit(MethodDeclaration method, String path) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = path + "." + method.getSignature().asString();
        insertProbes(body, signature);
    }

    public void visit(ConstructorDeclaration constructor, String path) {
        BlockStmt body = constructor.getBody();
        String signature = path + "." + constructor.getSignature().asString();
        insertProbes(body, signature);
    }
}
