package runtime;

import j2html.tags.ContainerTag;
import j2html.tags.UnescapedText;
import parser.CODE_UNIT;
import parser.ProjectStructureNode;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

import static j2html.TagCreator.*;

class ReportPageGenerator {

    void generatePage(String projectName, String timeStamp, ProjectStructureNode node, String path) {
        boolean isIndexPage = node.getType() != CODE_UNIT.CLASS;
        List<ProjectStructureNode> children = sortSections(node.getChildren());
        ContainerTag html = html(
                head().with(
                        title("Coverage report: " + projectName),
                        link().withRel("stylesheet").withHref(getCssPath(node, isIndexPage))
                ),
                body().with(
                        div(
                                getHeaderDiv(timeStamp),
                                getOverviewDiv(projectName, node, isIndexPage),
                                each(children, this::getSectionDiv)
                        ).withId("main")
                )
        );
        writeReportFile(html, path);
    }

    private ContainerTag getHeaderDiv(String timeStamp) {
        return div(
                h1("Coverage report"),
                div("Generated " + timeStamp).withId("timestampDiv")
        ).withId("headerDiv");
    }

    private String getCssPath(ProjectStructureNode node, boolean isIndexPage) {
        if (node.getJavaPath().equals("")) {
            return "style.css";
        }
        int dirs = node.getJavaPath().split("\\.").length;
        if (!isIndexPage) {
            dirs--;
        }
        return repeatUpDirectory(dirs) + "/style.css";
    }

    private ContainerTag getOverviewDiv(String projectName, ProjectStructureNode node, boolean isIndexPage) {
        ContainerTag coverageDiv = null;
        if (node.getCoverage() != null) {
            coverageDiv = getCoverageDiv(node.getCoverage()).withClass("overviewCoverage");
        }
        ContainerTag header;
        boolean isRoot = node.getType() == CODE_UNIT.SOURCE_DIR;
        if (isRoot) {
            header = h2(projectName);
        } else {
            header = getBreadCrumbs(projectName, node.getJavaPath(), isIndexPage);
        }
        return div(
                header,
                iff(!isRoot, p(getFormattedSignature(node))),
                iff(node.getCoverage() != null, coverageDiv)
        ).withId("overviewDiv");
    }

    private List<ProjectStructureNode> sortSections(Collection<ProjectStructureNode> nodes) {
        List<ProjectStructureNode> sorted = new ArrayList<>(nodes);
        sorted.sort((n1, n2) -> {
            if (n1.getCoverage() == null && n2.getCoverage() != null) {
                return 1;
            }
            if (n2.getCoverage() == null && n1.getCoverage() != null) {
                return -1;
            }
            return n1.getName().compareTo(n2.getName());
        });
        return sorted;
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
        return h2(
                a(projectName).withHref(href),
                text(" - "),
                each(linkTags, l -> join(l, text("."))),
                text(levels[levels.length - 1])
        );
    }

    private String repeatUpDirectory(int n) {
        return new String(new char[n]).replace("\0", "../");
    }

    private ContainerTag getSectionDiv(ProjectStructureNode node) {
        ContainerTag sectionName;
        if (node.getType() == CODE_UNIT.METHOD) {
            sectionName = span(getFormattedSignature(node));
        } else if (node.getType() == CODE_UNIT.CLASS) {
            sectionName = a(getFormattedSignature(node)).withHref(node.getName() + ".html");
        } else {
            sectionName = a(getFormattedSignature(node)).withHref((node.getName() + "/index.html"));
        }
        if (node.getCoverage() == null) {
            return div(h3(sectionName).withClass("interface")).withClass("sectionDiv");
        }
        return div(
                h3(sectionName),
                getCoverageDiv(node.getCoverage())
        ).withClass("sectionDiv");
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

    private UnescapedText getFormattedSignature(ProjectStructureNode node) {
        int i = node.getSignature().indexOf(node.getName());
        String modifiers = node.getSignature().substring(0, i);
        return join(span(modifiers).withClass("modifiers"), node.getName());
    }
}
