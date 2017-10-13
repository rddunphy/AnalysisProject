package ea;

import java.util.Objects;
import runtime.CoverageLogger;

public class ComplexNumber {

    public final double re;

    public final double im;

    public ComplexNumber(double re, double im) {
        CoverageLogger.getInstance().addCoveredMethod("ComplexNumber(double, double)");
        this.re = re;
        this.im = im;
    }

    public ComplexNumber(ComplexNumber c) {
        CoverageLogger.getInstance().addCoveredMethod("ComplexNumber(ComplexNumber)");
        this.re = c.re;
        this.im = c.im;
    }

    public ComplexNumber add(ComplexNumber c) {
        CoverageLogger.getInstance().addCoveredMethod("add(ComplexNumber)");
        double re = this.re + c.re;
        double im = this.im + c.im;
        return new ComplexNumber(re, im);
    }

    public ComplexNumber subtract(ComplexNumber c) {
        CoverageLogger.getInstance().addCoveredMethod("subtract(ComplexNumber)");
        double re = this.re - c.re;
        double im = this.im - c.im;
        return new ComplexNumber(re, im);
    }

    public int hashCode() {
        CoverageLogger.getInstance().addCoveredMethod("hashCode()");
        return Objects.hash(this.re, this.im);
    }

    public boolean equals(Object o) {
        CoverageLogger.getInstance().addCoveredMethod("equals(Object)");
        if (o == null || !(o instanceof ComplexNumber)) {
            return false;
        }
        ComplexNumber c = (ComplexNumber) o;
        return this.re == c.re && this.im == c.im;
    }

    public String toString() {
        CoverageLogger.getInstance().addCoveredMethod("toString()");
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
