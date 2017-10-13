package ea;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import runtime.CoverageLogger;

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
        String[] signatures = new String[] { "add(ComplexNumber)", "subtract(ComplexNumber)", "ComplexNumber(ComplexNumber)", "hashCode()", "toString()", "ComplexNumber(double, double)", "equals(Object)" };
        CoverageLogger.getInstance().setMethodSignatures(signatures);
    }

    @AfterClass
    public static void coverageWrapup() {
        System.out.println("Method coverage: " + CoverageLogger.getInstance().getMethodCoverage());
    }
}
