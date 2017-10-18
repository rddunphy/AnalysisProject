package runtime;

import j2html.tags.ContainerTag;
import parser.CODE_UNIT;
import parser.ProjectStructureNode;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

class ReportPageGenerator {

    void generatePage(String projectName, ProjectStructureNode node, String path) {
        ContainerTag pageName;
        if (node.getType() == CODE_UNIT.SOURCE_DIR) {
            pageName = span(projectName);
        } else {
            boolean isIndexPage = node.getType() != CODE_UNIT.CLASS;
            pageName = getBreadCrumbs(projectName, node.getJavaPath(), isIndexPage);
        }
        ContainerTag html = html(
                head().with(
                        title(projectName + " - coverage report")
                ),
                body().with(
                        h1(pageName),
                        getCoverageDiv(node.getCoverage()),
                        each(node.getChildren(), this::getSectionDiv)
                )
        );
        writeReportFile(html, path);
    }

    private ContainerTag getBreadCrumbs(String projectName, String javaPath, boolean isIndexPage) {
        List<ContainerTag> linkTags = new ArrayList<>();
        String[] levels = javaPath.split("\\.");
        String href;
        int offset = isIndexPage ? 1 : 2;
        for (int i = 0; i < levels.length - 1; i++) {
            href = repeatUpDirectory(levels.length - offset - i) + "index.html";
            linkTags.add(a(levels[i]).withHref(href));
        }
        href = repeatUpDirectory(levels.length - offset + 1) + "index.html";
        return span(
                a(projectName).withHref(href),
                text(" - "),
                each(linkTags, l -> span(l, text("."))),
                text(levels[levels.length - 1])
        );
    }

    private String repeatUpDirectory(int n) {
        return new String(new char[n]).replace("\0", "../");
    }

    private ContainerTag getSectionDiv(ProjectStructureNode node) {
        ContainerTag sectionName;
        if (node.getType() == CODE_UNIT.METHOD) {
            sectionName = span(node.getJavaPath());
        } else if (node.getType() == CODE_UNIT.CLASS) {
            sectionName = a(node.getJavaPath()).withHref(node.getName() + ".html");
        } else {
            sectionName = a(node.getJavaPath()).withHref((node.getName() + "/index.html"));
        }
        return div(
                h2(sectionName),
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
                div().withStyle("width: " + greenWidth + "px; height: 12px; background-color: green; display: inline-block;"),
                div().withStyle("width: " + redWidth + "px; height: 12px; background-color: red; display: inline-block;")
        ).withStyle("display: inline-block;");
    }

    private String formatCoveragePoint(Point p, String label) {
        double d = 100 * Coverage.calculateCoverage(p);
        return String.format("%.1f%% of %s (%d of %d)", d, label, p.x, p.y);
    }

    private void writeReportFile(ContainerTag html, String path) {
        String output = document(html);
        File file = new File(path);
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            try {
                Files.write(file.toPath(), output.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
