package math;

import java.util.Objects;

public class ComplexNumber {
    // class level comment
    public final double re;

    public final double im;

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
        if (re == 3) {
            int x = 1;
            int y = 3;
            int z = x+y;
        } else if (im == 2) {
            int x =2;
            int y = 4;
            int z = x* y;
            int a = 4;
        } else {
            int a = 2;
            int b = 3;
            int c = 4;
        }
    }

    public ComplexNumber(ComplexNumber c) {
        this.re = c.re;
        this.im = c.im;
    }

    public ComplexNumber add(ComplexNumber c) {
        double re = this.re + c.re;
        double im = this.im + c.im;
        return new ComplexNumber(re, im);
    }

    /**
     * javadoc
     * @param c
     * @return
     */
    public ComplexNumber subtract(ComplexNumber c) {
        double re = this.re - c.re; double im = this.im - c.im;


        // a comment
        return new ComplexNumber(re, im);
    }

    public int hashCode() {
        return Objects.hash(this.re, this.im);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ComplexNumber)) {
            return false;
        }
        ComplexNumber c = (ComplexNumber) o;
        return this.re == c.re && this.im == c.im;
    }

    public String toString() {
        String reStr = Double.toString(re);
        String imStr = Double.toString(im) + " i";
        if (im == 0) {
            return reStr;
        } else if (re == 0) {
            return imStr;
        } else {
            return reStr + " + " + imStr;
        }
    }

}
