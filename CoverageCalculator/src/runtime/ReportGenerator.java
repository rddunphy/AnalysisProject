package runtime;

import j2html.tags.ContainerTag;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static j2html.TagCreator.*;

public class ReportGenerator {

    public void generate() {
        double methodCoverage = TraceLogger.getInstance().getMethodCoverage();
        System.out.println("Method coverage: " + formatPercentage(methodCoverage));
        writeReportFile(generateHTML(formatPercentage(methodCoverage)), "../ExampleApplication/report/index.html");
    }

    private void writeReportFile(String html, String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            Files.write(file.toPath(), html.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatPercentage(double d) {
        return String.format("%.1f%%", d * 100);
    }

    private String generateHTML(String coverage) {
        return html(
                body().with(
                        h1("Method coverage"),
                        p(coverage)
                )
        ).render();
    }

    private ContainerTag getMethodDiv(String signature, double coverage) {
        return div(
                h2(signature),
                p("Coverage: " + formatPercentage(coverage))
        );
    }
}
