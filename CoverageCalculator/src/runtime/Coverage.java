package runtime;

import parser.CODE_UNIT;

import java.awt.*;

public class Coverage {

    private final Point classCoverage;
    private final Point methodCoverage;
    private final Point statementCoverage;

    public Coverage() {
        this(new Point(0, 0));
    }

    public Coverage(Point statementCoverage) {
        this.statementCoverage = statementCoverage;
        methodCoverage = new Point(0, 0);
        classCoverage = new Point(0, 0);
    }

    public Point getClassCoverage() {
        return classCoverage;
    }

    public Point getMethodCoverage() {
        return methodCoverage;
    }

    public Point getStatementCoverage() {
        return statementCoverage;
    }

    public static double calculateCoverage(Point p) {
        return p.getX() / p.getY();
    }
}
