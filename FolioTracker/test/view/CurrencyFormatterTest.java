package view;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrencyFormatterTest {

    @Test
    public void testFormatter() {
        assertEquals(CurrencyFormatter.format(123456), "$1234.56");
    }

    @Test
    public void testFormatterWithTrailingZeros() {
        assertEquals(CurrencyFormatter.format(123000), "$1230.00");
    }

}