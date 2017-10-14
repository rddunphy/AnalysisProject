package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import probes.PROBE_TYPE;
import probes.Probe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProbeInsertionVisitor extends VoidVisitorAdapter {

    private long nextProbeId = 0;
    private Map<Long, Probe> probes = new HashMap<>();

    private void insertProbe(BlockStmt body, String name) {
        Probe probe = new Probe(nextProbeId, PROBE_TYPE.METHOD_START, name);
        String probeCall = "CoverageLogger.getInstance().logProbe(" + nextProbeId + ");";
        Statement stmt = JavaParser.parseStatement(probeCall);
        body.addStatement(0, stmt);
        nextProbeId++;
    }

    public void visit(MethodDeclaration method, Object arg) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = method.getSignature().asString();
        insertProbe(body, signature);
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        BlockStmt body = constructor.getBody();
        String signature = constructor.getSignature().asString();
        insertProbe(body, signature);
    }

    public Map<Long, Probe> getProbes() {
        return probes;
    }
}
