package math;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

public class ComplexNumberTest {

    @Test
    public void toStringTest() {
        ComplexNumber c = new ComplexNumber(3, 1);
        assertThat(c.toString(), is("3.0 + 1.0 i"));
    }

    @Test
    public void addComplexNumbersTest() {
        ComplexNumber c1 = new ComplexNumber(1, 2);
        ComplexNumber c2 = new ComplexNumber(3, 4);
        ComplexNumber result = new ComplexNumber(4, 6);
        assertThat(c1.add(c2), is(result));
    }
}
