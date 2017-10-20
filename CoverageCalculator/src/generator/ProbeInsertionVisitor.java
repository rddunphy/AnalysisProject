package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import probes.Probe;
import probes.ProbeFactory;

/**
 * Recursively inserts probe calls into all block statements. Probes are inserted before each
 * terminating statement (e.g. 'return' or 'throw') and before each compound statement (e.g.
 * 'if' or 'while'), as well as at the end of methods. If there are no statements to be covered,
 * corresponding probes are omitted. Takes the signature of the method containing the block as
 * a parameter; this is provided by the MethodProcessingVisitor.
 */
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
        return stmt.isIfStmt() || stmt.isForStmt() || stmt.isForeachStmt() || stmt.isWhileStmt() || stmt.isTryStmt()
                || stmt.isDoStmt() || stmt.isSwitchStmt() || stmt.isSynchronizedStmt();
    }

    private boolean isTerminatingStatement(Statement stmt) {
        return stmt.isReturnStmt() || stmt.isThrowStmt() || stmt.isBreakStmt() || stmt.isContinueStmt();
    }

    /**
     * Insert probe calls into a block statement.
     *
     * @param block The block statement
     * @param signature The signature of the method containing this block
     */
    public void visit(BlockStmt block, String signature) {
        NodeList<Statement> statements = new NodeList<>(block.getStatements());
        int n = 0; // Number of statements since last probe
        BlockStmt modified = new BlockStmt();
        for (Statement stmt : statements) {
            n++;
            if (isCompoundStatement(stmt) || isTerminatingStatement(stmt)) {
                insertBlockEndProbe(modified, signature, n);
                n = 0;
            }
            modified.addStatement(stmt);
        }
        if (n > 0) {
            insertBlockEndProbe(modified, signature, n);
        }
        block.setStatements(modified.getStatements());
        super.visit(block, signature);
    }

}
