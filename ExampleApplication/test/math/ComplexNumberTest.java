package math;

import org.junit.Test;

import static org.junit.Assert.*;

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
        ComplexNumber result = new ComplexNumber(4,6);
        assertEquals(c1.add(c2), result);
    }

    @Test
    public void angleTest() {
        Angle a = new Angle(Math.PI);
        assertTrue(Math.abs(a.getDegrees() - 180) < 0.1);
    }

}