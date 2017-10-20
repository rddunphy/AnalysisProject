package probes;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory to create probe objects. Stores the probes in a map to ensure that each probe
 * receives a unique ID.
 */
public class ProbeFactory {

    private static long nextProbeId = 0;
    private static final Map<Long, Probe> probes = new HashMap<>();

    private static void storeProbe(Probe probe) {
        probes.put(probe.getId(), probe);
        nextProbeId++;
    }

    /**
     * @param methodSignature The signature of the method in which the probe will be placed
     * @param nStatements The number of statements covered by the probe
     * @return The probe
     */
    public static Probe createBlockEndProbe(String methodSignature, int nStatements) {
        Probe probe = new BlockEndProbe(nextProbeId, methodSignature, nStatements);
        storeProbe(probe);
        return probe;
    }

    /**
     * @return A map containing all generated probes, mapped by their IDs.
     */
    public static Map<Long, Probe> getProbes() {
        return probes;
    }

}
