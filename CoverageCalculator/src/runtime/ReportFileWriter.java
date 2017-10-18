package runtime;

import j2html.tags.ContainerTag;
import parser.ProjectStructureNode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

class ReportFileWriter {

    public void generatePage(ProjectStructureNode node, String path) {
        String html = html(
                head().with(
                        title("Coverage report")
                ),
                body().with(
                        h1(node.getName() + " (" + node.getJavaPath() + ")"),
                        getCoverageDiv(node.getCoverage()),
                        each(node.getChildren(), child -> getSectionDiv(child))
                )
        ).render();
        writeReportFile(html, path);
    }

    private ContainerTag getSectionDiv(ProjectStructureNode node) {
        return div(
                h2(node.getJavaPath()),
                getCoverageDiv(node.getCoverage())
        );
    }

    private ContainerTag getCoverageDiv(Coverage coverage) {
        List<String> lines = new ArrayList<>();
        lines.add("Statement coverage: " + formateCoveragePoint(coverage.getStatementCoverage()));
        if (coverage.getMethodCoverage().y > 0) {
            lines.add("Method coverage: " + formateCoveragePoint(coverage.getMethodCoverage()));
        }
        if (coverage.getClassCoverage().y > 0) {
            lines.add("Class coverage: " + formateCoveragePoint(coverage.getClassCoverage()));
        }
        return div(
                each(lines, line -> p(line))
        );
    }

    private String formateCoveragePoint(Point p) {
        double d = Coverage.calculateCoverage(p);
        return String.format("%.1f%% (%d of %d)", d * 100, p.x, p.y);
    }

    private void writeReportFile(String html, String path) {
        File file = new File(path);
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            try {
                Files.write(file.toPath(), html.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
