package probes;

import java.util.HashMap;
import java.util.Map;

public class ProbeFactory {

    private static long nextProbeId = 0;
    private static final Map<Long, Probe> probes = new HashMap<>();

    private static void storeProbe(Probe probe) {
        probes.put(probe.getId(), probe);
        nextProbeId++;
    }

    public static Probe createBlockEndProbe(String methodSignature, int nStatements) {
        Probe probe = new BlockEndProbe(nextProbeId, methodSignature, nStatements);
        storeProbe(probe);
        return probe;
    }

    public static Map<Long, Probe> getProbes() {
        return probes;
    }

}
