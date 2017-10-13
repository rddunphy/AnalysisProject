import org.junit.AfterClass;
import org.junit.BeforeClass;

public class JUnitMethods {

    @BeforeClass
    public static void coverageSetup() {
        CoverageLogger.getInstance().reset();
    }

    @AfterClass
    public static void coverageWrapup() {
        System.out.println("Method coverage: " + CoverageLogger.getInstance().getMethodCoverage());
    }
}
