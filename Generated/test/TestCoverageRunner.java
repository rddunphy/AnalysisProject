import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import runtime.CoverageLogger;
import runtime.ReportGenerator;

@RunWith(Suite.class)
@Suite.SuiteClasses({ math.AngleTest.class, math.ComplexNumberTest.class, calc.CalculatorTest.class })
public class TestCoverageRunner {

    @BeforeClass
    public static void coverageSetup() {
        CoverageLogger.getInstance().reset();
    }

    @AfterClass
    public static void coverageWrapup() {
        new ReportGenerator().generate();
    }
}
