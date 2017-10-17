package probes;

import java.util.HashMap;
import java.util.Map;

public class ProbeFactory {

    private static long nextProbeId = 0;
    private static Map<Long, Probe> probes = new HashMap<>();

    private static void storeProbe(Probe probe) {
        probes.put(probe.getId(), probe);
        nextProbeId++;
    }

    public static Probe createMethodStartProbe(String methodSignature) {
        Probe probe = new MethodStartProbe(nextProbeId, methodSignature);
        storeProbe(probe);
        return probe;
    }

    public static Probe createBlockEndProbe(String methodSignature, int nStatements) {
        Probe probe = new BlockEndProbe(nextProbeId, methodSignature, nStatements);
        storeProbe(probe);
        return probe;
    }

    public static Probe createExceptionProbe(Probe probe, Exception e) {
        return new ExceptionProbe(probe, e);
    }

    public static Map<Long, Probe> getProbes() {
        return probes;
    }

}
