package probes;

/**
 * Implementation of Probe which is inserted at the end of blocks.
 */
class BlockEndProbe implements Probe {

    private final int statementCount;
    private final long id;
    private final String methodSignature;

    BlockEndProbe(long id, String methodSignature, int statementCount) {
        this.statementCount = statementCount;
        this.id = id;
        this.methodSignature = methodSignature;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getMethodSignature() {
        return this.methodSignature;
    }

    @Override
    public int getStatementCount() {
        return statementCount;
    }
}
