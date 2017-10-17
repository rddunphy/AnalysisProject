package runtime;

import probes.BlockEndProbe;
import probes.Probe;
import probes.ProbeFactory;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TraceLogger {

    private static final TraceLogger instance = new TraceLogger();

    private Map<Long, Probe> probeMap;
    private final List<Probe> trace;

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
            return (Map<Long, Probe>) objectinputstream.readObject();
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
        trace.add(ProbeFactory.createExceptionProbe(probe, e));
    }

    public void calculateStatementCoverage() {
        Map<BlockEndProbe, Boolean> covered = new HashMap<>();
        for (Probe probe : probeMap.values()) {
            if (probe instanceof BlockEndProbe) {
                covered.put((BlockEndProbe) probe, false);
            }
        }
        for (Probe probe : trace) {
            if (probe instanceof  BlockEndProbe) {
                covered.put((BlockEndProbe) probe, true);
            }
        }
        Map<String, Point> coveredStatements = new HashMap<>();
        for (BlockEndProbe probe : covered.keySet()) {
            String method = probe.getMethodSignature();
            if (!coveredStatements.containsKey(method)) {
                coveredStatements.put(method, new Point(0, 0));
            }
            if (covered.get(probe)) {
                coveredStatements.get(method).x += probe.getStatementCount();
            }
            coveredStatements.get(method).y += probe.getStatementCount();
        }
        Map<String, Double> statementCoverage = new HashMap<>();
        for (String method : coveredStatements.keySet()) {
            Point p = coveredStatements.get(method);
            double coverage = p.getX() / p.getY();
            statementCoverage.put(method, coverage);
        }
        for (String method : statementCoverage.keySet()) {
            System.out.println(method + ": " + formatPercentage(statementCoverage.get(method)));
        }
    }

    private String formatPercentage(double d) {
        return String.format("%.1f%%", d * 100);
    }

}