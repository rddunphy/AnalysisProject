package runtime;

import j2html.tags.ContainerTag;
import parser.CODE_UNIT;
import parser.ProjectStructureNode;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

class ReportPageGenerator {

    void generatePage(String projectName, ProjectStructureNode node, String path) {
        ContainerTag pageName;
        boolean isIndexPage = node.getType() != CODE_UNIT.CLASS;
        if (node.getType() == CODE_UNIT.SOURCE_DIR) {
            pageName = span(projectName);
        } else {
            pageName = getBreadCrumbs(projectName, node.getJavaPath(), isIndexPage);
        }
        ContainerTag coverageDiv = null;
        if (node.getCoverage() != null) {
            coverageDiv = getCoverageDiv(node.getCoverage());
        }
        String cssPath;
        if (node.getJavaPath().equals("")) {
            cssPath = "style.css";
        } else {
            int dirs = node.getJavaPath().split("\\.").length;
            if (!isIndexPage) {
                dirs--;
            }
            cssPath = repeatUpDirectory(dirs) + "/style.css";
        }
        ContainerTag html = html(
                head().with(
                        title(projectName + " - coverage report"),
                        link().withRel("stylesheet").withHref(cssPath)
                ),
                body().with(
                        div(
                                h1(pageName),
                                iff(node.getCoverage() != null, coverageDiv),
                                each(node.getChildren(), this::getSectionDiv)
                        ).withId("main")
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
        if (node.getCoverage() == null) {
            return div(h2(sectionName).withClass("interface"));
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
        int greenWidth = (int) Math.round(coverage * 300);
        return div(
                div().withStyle("width: " + greenWidth + "px;").withClass("coverageBarGreen")
        ).withClass("coverageBar");
    }

    private String formatCoveragePoint(Point p, String label) {
        double value = 100 * Coverage.calculateCoverage(p);
        String formattedValue = new DecimalFormat("#.#").format(value);
        return String.format("%s%% of %s (%d of %d)", formattedValue, label, p.x, p.y);
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
