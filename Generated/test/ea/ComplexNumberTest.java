package ea;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import runtime.CoverageLogger;
import runtime.ReportGenerator;

public class ComplexNumberTest {

    @Test
    public void toStringTest() {
        ComplexNumber c = new ComplexNumber(3, 1);
        assertEquals(c.toString(), "3.0 + 1.0 i");
    }

    @Test
    public void addComplexNumbersTest() {
        ComplexNumber c1 = new ComplexNumber(1, 2);
        ComplexNumber c2 = new ComplexNumber(3, 4);
        ComplexNumber result = new ComplexNumber(4, 6);
        assertEquals(c1.add(c2), result);
    }

    @BeforeClass
    public static void coverageSetup() {
        CoverageLogger.getInstance().reset();
    }

    @AfterClass
    public static void coverageWrapup() {
        new ReportGenerator().generate();
    }
}
