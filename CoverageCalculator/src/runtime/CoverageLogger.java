package runtime;

import com.github.javaparser.ast.CompilationUnit;
import jdk.internal.util.xml.impl.Input;
import parser.ProjectParser;

import java.io.*;
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
        try {
            allMethods = deserialiseMethodSignatures();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Set<String> deserialiseMethodSignatures() throws IOException, ClassNotFoundException {
        try (InputStream streamIn = new FileInputStream("../Generated/ser/methods.ser");
             ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
            Set<String> methodSignatures = (Set<String>) objectinputstream.readObject();
            return methodSignatures;
        }
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