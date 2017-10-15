package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import parser.ProjectParser;
import probes.Probe;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static parser.ProjectParser.getCompilationUnitFromFile;

public class ProjectGenerator {

    private String sourceProjectPath;
    private String generatedProjectPath;

    private static final String templatePath = "CoverageCalculator/templates";
    private ProbeInsertionVisitor probeInsertionVisitor;

    public ProjectGenerator(String sourceProjectPath, String generatedProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
        this.generatedProjectPath = generatedProjectPath;
    }

    public void generate() {
        ProjectParser parser = new ProjectParser(sourceProjectPath);
        probeInsertionVisitor = new ProbeInsertionVisitor();
        try {
            for (Map.Entry<String, CompilationUnit> entry : parser.getAllSourceFiles().entrySet()) {
                copySourceFile(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, CompilationUnit> entry : parser.getAllTestFiles().entrySet()) {
                copyTestFile(entry.getKey(), entry.getValue());
            }
            generateTestRunner(parser.getAllTestFiles().keySet());
            serialiseProbes();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void serialiseProbes() throws IOException {
        File file = new File(generatedProjectPath + "/ser/probes.ser");
        file.getParentFile().mkdirs();
        file.delete();
        try (FileOutputStream fout = new FileOutputStream(file, true);
             ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(probeInsertionVisitor.getProbes());
        }
    }

    private void writeCompilationUnitToFile(CompilationUnit cu, String filePath) throws IOException {
        String source = cu.toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        Files.write(file.toPath(), source.getBytes(StandardCharsets.UTF_8));
    }

    private void copySourceFile(String sourceFilePath, CompilationUnit cu) throws IOException {
        cu.addImport("runtime.CoverageLogger");
        cu.accept(probeInsertionVisitor, null);
        writeCompilationUnitToFile(cu, generatedProjectPath + "/src/" + sourceFilePath);
    }

    private void generateTestRunner(Collection<String> testClassFiles) throws IOException {
        String filePath = templatePath + "/TestCoverageRunner.java";
        CompilationUnit cu = getCompilationUnitFromFile(filePath);
        ClassOrInterfaceDeclaration c = getContainedClass(filePath, cu);
        List<String> testClasses = new ArrayList<>();
        for (String path : testClassFiles) {
            path = path.substring(0, path.indexOf("."));
            path += ".class";
            path = path.substring(1);
            path = path.replaceAll("/", ".");
            testClasses.add(path);
        }
        StringBuilder builder = new StringBuilder("@Suite.SuiteClasses({");
        builder.append(String.join(", ", testClasses));
        builder.append("})");
        AnnotationExpr annotation = JavaParser.parseAnnotation(builder.toString());
        c.addAnnotation(annotation);
        writeCompilationUnitToFile(cu, generatedProjectPath + "/test/TestCoverageRunner.java");
    }

    private void copyTestFile(String testFilePath, CompilationUnit cu) throws IOException {
        writeCompilationUnitToFile(cu, generatedProjectPath + "/test/" + testFilePath);
    }

    private ClassOrInterfaceDeclaration getContainedClass(String filePath, CompilationUnit cu) {
        String className = filePath.substring(filePath.lastIndexOf("/") + 1);
        className = className.substring(0, className.indexOf("."));
        Optional<ClassOrInterfaceDeclaration> o = cu.getClassByName(className);
        if (o.isPresent()) {
            return o.get();
        }
        return null;
    }

    private List<MethodDeclaration> getJUnitCoverageMethods() throws IOException {
        String filePath = templatePath + "/JUnitMethods.java";
        CompilationUnit cu = getCompilationUnitFromFile(filePath);
        ClassOrInterfaceDeclaration c = getContainedClass(filePath, cu);
        return c.getMethods();
    }
}
