package runtime;

import probes.Probe;
import probes.ProbeFactory;

import java.io.*;
import java.util.*;
import java.util.List;

public class Trace {

    private static final Trace instance = new Trace();

    private Map<Long, Probe> probeMap;
    private final List<Probe> trace;

    private Trace() {
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
            return (Map<Long, Probe>) objectinputstream.readObject();
        }
    }

    public static Trace getInstance() {
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
        trace.add(ProbeFactory.createExceptionProbe(probe, e));
    }

    public Map<Long, Probe> getProbes() {
        return probeMap;
    }

    public List<Probe> getTrace() {
        return trace;
    }
}