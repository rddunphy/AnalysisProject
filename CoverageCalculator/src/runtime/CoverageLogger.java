package runtime;

import com.github.javaparser.ast.CompilationUnit;
import parser.ProjectParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoverageLogger {

    private static CoverageLogger instance = new CoverageLogger();

    private Set<String> allMethods;
    private Set<String> coveredMethods;

    private CoverageLogger() {
        coveredMethods = new HashSet<>();
        allMethods = new HashSet<>();
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

    public double getMethodCoverage() {
        double coverage = coveredMethods.size();
        return coverage / allMethods.size();
    }

    public void setMethodSignatures(String[] signatures) {
        allMethods.clear();
        allMethods.addAll(Arrays.asList(signatures));
    }
}