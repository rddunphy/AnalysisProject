package probes;

class BlockEndProbe implements Probe {

    private final int statementCount;
    private final long id;
    private final String methodSignature;

    BlockEndProbe(long id, String methodSignature, int statementCount) {
        this.statementCount = statementCount;
        this.id = id;
        this.methodSignature = methodSignature;
    }

    public long getId() {
        return this.id;
    }

    public String getMethodSignature() {
        return this.methodSignature;
    }

    public int getStatementCount() {
        return statementCount;
    }
}
