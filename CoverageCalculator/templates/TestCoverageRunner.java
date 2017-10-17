import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import runtime.Trace;
import runtime.ReportGenerator;

@RunWith(Suite.class)
public class TestCoverageRunner {

    @BeforeClass
    public static void coverageSetup() {
        Trace.getInstance().reset();
    }

    @AfterClass
    public static void coverageWrapup() {
        new ReportGenerator().generate();
    }
}

