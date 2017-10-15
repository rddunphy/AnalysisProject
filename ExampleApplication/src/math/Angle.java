package math;

public class Angle {

    public static final double TAU = 2 * Math.PI;

    private double radians;

    public Angle(double radians) {
        this.radians = radians;
    }

    public double getRadians() {
        return radians;
    }

    public double getDegrees() {
        double deg = radians * 360 / TAU;
        return deg;
    }
}
