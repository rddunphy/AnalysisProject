package probes;

import java.io.Serializable;

public abstract class Probe implements Serializable {

    private final long id;
    private final String methodSignature;

    Probe(long id, String methodSignature) {
        this.id = id;
        this.methodSignature = methodSignature;
    }

    public long getId() {
        return this.id;
    }

    public String getMethodSignature() {
        return this.methodSignature;
    }

}
