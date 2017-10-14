package runtime;

import static j2html.TagCreator.*;

public class ReportGenerator {

    public void generate() {
        double methodCoverage = CoverageLogger.getInstance().getMethodCoverage();
        System.out.println("Method coverage: " + formatPercentage(methodCoverage));
        System.out.println(generateHTML(formatPercentage(methodCoverage)));
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
}
