package probes;

import java.io.Serializable;

public interface Probe extends Serializable {

    long getId();

    String getMethodSignature();

    int getStatementCount();

}
