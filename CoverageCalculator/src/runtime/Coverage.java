package runtime;

import java.awt.Point;

/**
 * A data class containing Points which represent various coverage metrics. The x coordinate
 * of the point represents the number of covered elements, the y coordinate represents the
 * total number.
 */
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

    /**
     * @return The covered and total numbers of classes
     */
    Point getClassCoverage() {
        return classCoverage;
    }

    /**
     * @return The covered and total numbers of methods
     */
    Point getMethodCoverage() {
        return methodCoverage;
    }

    /**
     * @return The covered and total numbers of statements
     */
    Point getStatementCoverage() {
        return statementCoverage;
    }

    /**
     * @param p A coverage point
     * @return The coverage as a ratio
     */
    static double asDouble(Point p) {
        return p.getX() / p.getY();
    }
}
