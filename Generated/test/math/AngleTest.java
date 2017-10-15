package math;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

public class AngleTest {

    @Test
    public void getRadiansTest() {
        double rad = Math.PI;
        Angle a = new Angle(rad);
        assertThat(a.getRadians(), is(closeTo(rad, Math.ulp(rad))));
    }

    @Test
    public void getDegreesTest() {
        Angle a = new Angle(Math.PI);
        assertThat(a.getDegrees(), is(closeTo(180, Math.ulp(180))));
    }
}
