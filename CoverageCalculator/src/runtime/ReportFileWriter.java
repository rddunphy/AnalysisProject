package runtime;

import j2html.tags.ContainerTag;
import parser.ProjectStructureNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static j2html.TagCreator.*;

class ReportFileWriter {

    public void generatePage(ProjectStructureNode node, String path) {
        double statementCoverage = Coverage.calculateCoverage(node.getCoverage().getStatementCoverage());
        String html = html(
                head().with(
                        title("Coverage report")
                ),
                body().with(
                        h1(node.getName() + " (" + node.getJavaPath() + ")"),
                        p("Statement coverage: " + formatPercentage(statementCoverage))//,
                        //each(methods.entrySet(), entry -> getMethodDiv(entry.getKey(), entry.getValue()))
                )
        ).render();
        writeReportFile(html, path);
    }

    public void generateClassPage(String className, double classCoverage, Map<String, Double> methods, String path) {
        String html = html(
                head().with(
                        title("Coverage report")
                ),
                body().with(
                        h1("Class: " + className),
                        p("Method coverage: " + formatPercentage(classCoverage)),
                        each(methods.entrySet(), entry -> getMethodDiv(entry.getKey(), entry.getValue()))
                )
        ).render();
        writeReportFile(html, path);
    }

    private ContainerTag getMethodDiv(String signature, double coverage) {
        return div(
                h2("Method: " + signature),
                p("Coverage: " + formatPercentage(coverage))
        );
    }

    private String formatPercentage(double d) {
        return String.format("%.1f%%", d * 100);
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
