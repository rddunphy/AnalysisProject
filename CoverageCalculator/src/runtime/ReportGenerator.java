package runtime;

import parser.ProjectStructureNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ReportGenerator {

    public void generate() {
        try {
            ProjectStructureNode tree = new CoverageCalculator().calculate(deserialiseStructure());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*
        double methodCoverage = Trace.getInstance().getMethodCoverage();
        ReportFileWriter writer = new ReportFileWriter();
        Map<String, Double> methods = new HashMap<>();
        methods.put("Method A", 0.234);
        methods.put("Method B", 0.1234);
        writer.generateClassPage("Root", methodCoverage, methods, "../ExampleApplication/report/index.html");*/
    }


    public static ProjectStructureNode deserialiseStructure() throws IOException, ClassNotFoundException {
        try (InputStream streamIn = new FileInputStream("../Generated/ser/structure.ser");
             ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
            return (ProjectStructureNode) objectinputstream.readObject();
        }
    }

}
