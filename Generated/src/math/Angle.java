package math;

import runtime.CoverageLogger;

public class Angle {

    public static final double TAU = 2 * Math.PI;

    private double radians;

    public Angle(double radians) {
        CoverageLogger.getInstance().logProbe(8);
        this.radians = radians;
    }

    public double getRadians() {
        CoverageLogger.getInstance().logProbe(9);
        return radians;
    }

    public double getDegrees() {
        CoverageLogger.getInstance().logProbe(10);
        double deg = radians * 360 / TAU;
        return deg;
    }
}
