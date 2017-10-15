package calc;

import math.ComplexNumber;
import runtime.CoverageLogger;

public class Calculator {

    public ComplexNumber addNumbers(ComplexNumber a, ComplexNumber b) {
        CoverageLogger.getInstance().logProbe(7);
        return a.add(b);
    }

    public int addNumbers(int a, int b) {
        CoverageLogger.getInstance().logProbe(8);
        return a + b;
    }
}