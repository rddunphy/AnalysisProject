package probes;

public class BlockEndProbe extends Probe {

    private int statementCount;

    BlockEndProbe(long id, String methodSignature, int statementCount) {
        super(id, methodSignature);
        this.statementCount = statementCount;
    }

    public int getStatementCount() {
        return statementCount;
    }
}
