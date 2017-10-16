package runtime;

import parser.CODE_UNIT;

public class CoverageMeasure {

    private CODE_UNIT type;
    private String methodSignature;
    private double methodCoverage;
    private double statementCoverage;

    public CoverageMeasure(CODE_UNIT type, String methodSignature, double methodCoverage, double statementCoverage) {
        this.type = type;
        this.methodSignature = methodSignature;
        this.methodCoverage = methodCoverage;
        this.statementCoverage = statementCoverage;
    }

    public CODE_UNIT getType() {
        return type;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public double getMethodCoverage() {
        return methodCoverage;
    }

    public double getStatementCoverage() {
        return statementCoverage;
    }

}
