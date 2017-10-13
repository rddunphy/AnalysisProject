import org.junit.AfterClass;
import org.junit.BeforeClass;

public class JUnitMethods {

    @BeforeClass
    public static void coverageSetup() {
        CoverageLogger.getInstance().reset();
    }

    @AfterClass
    public static void coverageWrapup() {
        System.out.println("Number of methods visited: " + CoverageLogger.getInstance().getNumberOfCoveredMethods());
    }
}
