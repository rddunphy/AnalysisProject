package runtime;

import probes.Probe;
import probes.ProbeFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            Object o = objectinputstream.readObject();
            return (Map<Long, Probe>) o;
        }
    }

    Map<Long, Probe> getProbes() {
        return probeMap;
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

    public List<Probe> getTrace() {
        return trace;
    }
}