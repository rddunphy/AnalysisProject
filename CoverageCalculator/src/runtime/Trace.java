package runtime;

import probes.Probe;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for a single instance of a list containing the trace of probes encountered during
 * a run of the generated test classes.
 */
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

    /**
     * @return The map of probe IDs to probes contained within the test suite.
     */
    Map<Long, Probe> getProbes() {
        return probeMap;
    }

    /**
     * @return The singleton instance of the trace
     */
    public static Trace getInstance() {
        return instance;
    }

    /**
     * Clear the trace for a new run.
     */
    public void reset() {
        trace.clear();
    }

    /**
     * Log a call to the probe with the given ID.
     *
     * @param probeID The unique probe ID.
     */
    public void logProbe(long probeID) {
        trace.add(probeMap.get(probeID));
    }

    /**
     * @return The list of probe entries represented by this trace.
     */
    public List<Probe> getTrace() {
        return trace;
    }
}