package runtime;

public class ReportGenerator {

    public void generate() {
        double methodCoverage = CoverageLogger.getInstance().getMethodCoverage();
        System.out.println("Method coverage: " + formatPercentage(methodCoverage));
    }

    private String formatPercentage(double d) {
        return String.format("%.1f%%", d * 100);
    }
}
