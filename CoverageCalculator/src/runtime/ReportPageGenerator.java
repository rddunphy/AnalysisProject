package runtime;

import j2html.tags.ContainerTag;
import parser.CODE_UNIT;
import parser.ProjectStructureNode;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;

public class ReportPageGenerator {

    private ReportFileWriter writer;

    public ReportPageGenerator() {
        this.writer = new ReportFileWriter();
    }

    public void generatePage(String projectName, ProjectStructureNode node, String path) {
        String pageName = (node.getType() == CODE_UNIT.SOURCE_DIR) ? projectName : node.getJavaPath();
        String html = html(
                head().with(
                        title(projectName + " - coverage report")
                ),
                body().with(
                        h1(pageName),
                        getCoverageDiv(node.getCoverage()),
                        each(node.getChildren(), child -> getSectionDiv(child))
                )
        ).render();
        writer.writeReportFile(html, path);
    }

    private ContainerTag getSectionDiv(ProjectStructureNode node) {
        return div(
                h2(node.getJavaPath()),
                getCoverageDiv(node.getCoverage())
        );
    }

    private ContainerTag getCoverageDiv(Coverage coverage) {
        Map<String, Double> lines = new HashMap<>();
        String line = formatCoveragePoint(coverage.getStatementCoverage(), "statements");
        double value = Coverage.calculateCoverage(coverage.getStatementCoverage());
        lines.put(line, value);
        if (coverage.getMethodCoverage().y > 0) {
            line = formatCoveragePoint(coverage.getMethodCoverage(), "methods");
            value = Coverage.calculateCoverage(coverage.getMethodCoverage());
            lines.put(line, value);
        }
        if (coverage.getClassCoverage().y > 0) {
            line = formatCoveragePoint(coverage.getClassCoverage(), "classes");
            value = Coverage.calculateCoverage(coverage.getClassCoverage());
            lines.put(line, value);
        }
        return div(
                each(lines.keySet(), l -> p(getCoverageBar(lines.get(l)), span(l)))
        );
    }

    private ContainerTag getCoverageBar(double coverage) {
        int totalWidth = 300;
        int greenWidth = (int) Math.round(coverage * totalWidth);
        int redWidth = totalWidth - greenWidth;
        return div(
                div().withStyle("width: " + greenWidth + "; height: 12px; background-color: green; display: inline-block;"),
                div().withStyle("width: " + redWidth + "px; height: 12px; background-color: red; display: inline-block;")
        ).withStyle("display: inline-block;");
    }

    private String formatCoveragePoint(Point p, String label) {
        double d = Coverage.calculateCoverage(p);
        return String.format("%.1f%% of %s (%d of %d)", d * 100, label, p.x, p.y);
    }
}
