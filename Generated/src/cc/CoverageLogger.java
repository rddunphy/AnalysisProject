package cc;

import java.util.HashSet;
import java.util.Set;

public class CoverageLogger {

    private static CoverageLogger instance = new CoverageLogger();

    private Set<String> coveredMethods;

    private CoverageLogger() {
        coveredMethods = new HashSet<>();
    }

    public static CoverageLogger getInstance() {
        return instance;
    }

    public void reset() {
        coveredMethods.clear();
    }

    public void addCoveredMethod(String methodSignature) {
        coveredMethods.add(methodSignature);
    }

    public int getNumberOfCoveredMethods() {
        return coveredMethods.size();
    }
}
