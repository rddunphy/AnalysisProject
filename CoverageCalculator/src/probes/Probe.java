package probes;

import java.io.Serializable;

public class Probe implements Serializable {

    private final long id;
    private final PROBE_TYPE type;
    private String methodSignature;
    private int statementCount;

    public Probe(long id, PROBE_TYPE type, String methodSignature, int statementCount) {
        this.id = id;
        this.type = type;
        this.methodSignature = methodSignature;
        this.statementCount = statementCount;
    }

    public PROBE_TYPE getType() {
        return this.type;
    }

    public long getId() {
        return id;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public int getStatementCount() {
        return statementCount;
    }
}
