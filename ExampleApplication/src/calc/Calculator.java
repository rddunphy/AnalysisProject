package calc;

import math.ComplexNumber;

public class Calculator {

    public ComplexNumber addNumbers(ComplexNumber a, ComplexNumber b) {
        return a.add(b);
    }

    public void methodWithSwitchStatement() {
        int x = 3;
        switch (x) {
            case 0:
                break;
            case 1:
            case 2:
                int i = 9;
                i += x;
                break;
            case 3:
            {
                x++;
            }
        }
    }

    public int addNumbers(int a, int b) {
        return a + b;
    }
}
