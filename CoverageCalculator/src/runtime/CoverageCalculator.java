package runtime;

import parser.CODE_UNIT;
import parser.ProjectStructureNode;
import probes.BlockEndProbe;
import probes.Probe;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoverageCalculator {

    private static void addCoverageStatements(boolean covered, Point coverage, int n) {
        if (covered) {
            coverage.x += n;
        }
        coverage.y += n;
    }

    private static void addCoverageStatements(Point coverage, Point toAdd) {
        coverage.x += toAdd.x;
        coverage.y += toAdd.y;
    }

    private static boolean hasStatementCoverage(ProjectStructureNode node) {
        return node.getCoverage().getStatementCoverage().x > 0;
    }

    private static Map<String, Point> calculateStatementCoverageForMethods(Map<Long, Probe> probes, List<Probe> trace) {
        Map<BlockEndProbe, Boolean> covered = new HashMap<>();
        for (Probe probe : probes.values()) {
            if (probe instanceof BlockEndProbe) {
                covered.put((BlockEndProbe) probe, false);
            }
        }
        for (Probe probe : trace) {
            if (probe instanceof BlockEndProbe) {
                covered.put((BlockEndProbe) probe, true);
            }
        }
        Map<String, Point> coveredStatements = new HashMap<>();
        for (BlockEndProbe probe : covered.keySet()) {
            String method = probe.getMethodSignature();
            if (!coveredStatements.containsKey(method)) {
                coveredStatements.put(method, new Point(0, 0));
            }
            addCoverageStatements(covered.get(probe), coveredStatements.get(method), probe.getStatementCount());
        }
        return coveredStatements;
    }

    public static ProjectStructureNode calculate(ProjectStructureNode tree) {
        Map<Long, Probe> probes = Trace.getInstance().getProbes();
        List<Probe> trace = Trace.getInstance().getTrace();
        Map<String, Point> statementCoverages = calculateStatementCoverageForMethods(probes, trace);
        return calculate(tree, statementCoverages);
    }

    private static ProjectStructureNode calculate(ProjectStructureNode node, Map<String, Point> statementCoverages) {
        for (ProjectStructureNode child : node.getChildren()) {
            calculate(child, statementCoverages);
        }
        if (node.getType() == CODE_UNIT.METHOD) {
            Point statementCoverage = statementCoverages.get(node.getJavaPath());
            if (statementCoverage == null) {
                node.setCoverage(new Coverage());
            } else {
                Coverage coverage = new Coverage(statementCoverages.get(node.getJavaPath()));
                node.setCoverage(coverage);
            }
        } else if (node.getType() == CODE_UNIT.CLASS) {
            Coverage coverage = new Coverage();
            for (ProjectStructureNode child : node.getChildren()) {
                addCoverageStatements(coverage.getStatementCoverage(), child.getCoverage().getStatementCoverage());
                addCoverageStatements(hasStatementCoverage(child), coverage.getMethodCoverage(), 1);
            }
            node.setCoverage(coverage);
        } else {
            Coverage coverage = new Coverage();
            for (ProjectStructureNode child : node.getChildren()) {
                addCoverageStatements(coverage.getStatementCoverage(), child.getCoverage().getStatementCoverage());
                addCoverageStatements(coverage.getMethodCoverage(), child.getCoverage().getMethodCoverage());
                if (child.getType() == CODE_UNIT.CLASS) {
                    addCoverageStatements(hasStatementCoverage(child), coverage.getClassCoverage(), 1);
                } else {
                    addCoverageStatements(coverage.getClassCoverage(), child.getCoverage().getClassCoverage());
                }
            }
            node.setCoverage(coverage);
        }
        return node;
    }

}
