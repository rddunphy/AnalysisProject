package probes;

import java.io.Serializable;

/**
 * Probes should only be instantiated by the ProbeFactory to ensure that they have unique IDs.
 */
public interface Probe extends Serializable {

    /**
     * @return The unique ID of the probe
     */
    long getId();

    /**
     * @return The signature of the method in which this probe is placed
     */
    String getMethodSignature();

    /**
     * @return The number of statements covered by this probe
     */
    int getStatementCount();

}
