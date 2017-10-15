package runtime;

import probes.PROBE_TYPE;
import probes.Probe;
import probes.ExceptionProbe;

import java.io.*;
import java.util.*;

public class TraceLogger {

    private static TraceLogger instance = new TraceLogger();

    private Map<Long, Probe> probeMap;
    private List<Probe> trace;

    private TraceLogger() {
        trace = new ArrayList<>();
        try {
            probeMap = deserialiseProbeMap();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Map<Long, Probe> deserialiseProbeMap() throws IOException, ClassNotFoundException {
        try (InputStream streamIn = new FileInputStream("../Generated/ser/probes.ser");
             ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
            Map<Long, Probe> probes = (Map<Long, Probe>) objectinputstream.readObject();
            return probes;
        }
    }

    public static TraceLogger getInstance() {
        return instance;
    }

    public void reset() {
        trace.clear();
    }

    public void logProbe(long probeID) {
        trace.add(probeMap.get(probeID));
    }

    public void logProbe(long probeID, Exception e) {
        Probe probe = probeMap.get(probeID);
        trace.add(new ExceptionProbe(probe, e));
    }

    public double getMethodCoverage() {
        Set<String> allMethodSignatures = new HashSet<>();
        Set<String> visitedMethodSignatures = new HashSet<>();
        for (Probe probe : probeMap.values()) {
            if (probe.getType() == PROBE_TYPE.METHOD_START) {
                allMethodSignatures.add(probe.getMethodSignature());
            }
        }
        for (Probe probe : trace) {
            if (probe.getType() == PROBE_TYPE.METHOD_START) {
                visitedMethodSignatures.add(probe.getMethodSignature());
            }
        }
        double coverage = visitedMethodSignatures.size();
        return coverage / allMethodSignatures.size();
    }

}