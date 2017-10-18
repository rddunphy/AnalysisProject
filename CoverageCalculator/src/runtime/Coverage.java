package runtime;

import java.awt.Point;

public class Coverage {

    private final Point classCoverage;
    private final Point methodCoverage;
    private final Point statementCoverage;

    Coverage() {
        this(new Point(0, 0));
    }

    Coverage(Point statementCoverage) {
        this.statementCoverage = statementCoverage;
        methodCoverage = new Point(0, 0);
        classCoverage = new Point(0, 0);
    }

    Point getClassCoverage() {
        return classCoverage;
    }

    Point getMethodCoverage() {
        return methodCoverage;
    }

    Point getStatementCoverage() {
        return statementCoverage;
    }

    static double calculateCoverage(Point p) {
        return p.getX() / p.getY();
    }
}
