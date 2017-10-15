package calc;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void addIntegersTest() {
        int result = new Calculator().addNumbers(3, 4);
        assertThat(result, is(7));
    }
}
