package runtime;

import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    public void generate() {
        double methodCoverage = TraceLogger.getInstance().getMethodCoverage();
        ReportFileWriter writer = new ReportFileWriter();
        Map<String, Double> methods = new HashMap<>();
        methods.put("Method A", 0.234);
        methods.put("Method B", 0.1234);
        writer.generateClassPage("Root", methodCoverage, methods, "../ExampleApplication/report/index.html");
    }

}
