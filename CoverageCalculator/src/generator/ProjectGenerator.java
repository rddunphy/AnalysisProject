package generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import parser.CODE_UNIT;
import parser.ProjectParser;
import parser.ProjectStructureNode;
import probes.ProbeFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static parser.ProjectParser.getCompilationUnitFromFile;

public class ProjectGenerator {

    private final String sourceProjectPath;
    private final String generatedProjectPath;

    private static final String templatePath = "CoverageCalculator/templates";

    public ProjectGenerator(String sourceProjectPath, String generatedProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
        this.generatedProjectPath = generatedProjectPath;
    }

    public void generate() {
        ProjectParser parser = new ProjectParser(sourceProjectPath);
        try {
            for (ProjectStructureNode node : parser.getSourceFiles().getAllNodesOfType(CODE_UNIT.CLASS)) {
                copySourceFile(node);
            }
            for (ProjectStructureNode node : parser.getTestFiles().getAllNodesOfType(CODE_UNIT.CLASS)) {
                copyTestFile(node);
            }
            generateTestRunner(parser.getTestFiles().getAllNodesOfType(CODE_UNIT.CLASS));
            serialiseData(ProbeFactory.getProbes(), "probes");
            serialiseData(parser.getSourceFiles(), "structure");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void serialiseData(Object data, String name) throws IOException {
        File file = new File(generatedProjectPath + "/ser/" + name + ".ser");
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            try (FileOutputStream out = new FileOutputStream(file, true);
                 ObjectOutputStream oos = new ObjectOutputStream(out)) {
                oos.writeObject(data);
            }
        }
    }

    private void writeCompilationUnitToFile(CompilationUnit cu, String filePath) throws IOException {
        String source = cu.toString();
        File file = new File(filePath);
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            Files.write(file.toPath(), source.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void copySourceFile(ProjectStructureNode node) throws IOException {
        CompilationUnit cu = ProjectParser.getCompilationUnitFromFile(node.getFilePath());
        String sourceFilePath = node.getFilePath();
        cu.addImport("runtime.Trace");
        cu.accept(new BlockConverterVisitor(), null);
        cu.accept(new MethodProcessingVisitor(), node.getJavaPath());
        String path = generatedProjectPath + sourceFilePath.substring(sourceFilePath.indexOf("/"));
        writeCompilationUnitToFile(cu, path);
    }

    private void generateTestRunner(Collection<ProjectStructureNode> nodes) throws IOException {
        String testRunnerPath = templatePath + "/TestCoverageRunner.java";
        CompilationUnit cu = getCompilationUnitFromFile(testRunnerPath);
        List<String> testClasses = new ArrayList<>();
        for (ProjectStructureNode node : nodes) {
            testClasses.add(node.getJavaPath() + ".class");
        }
        ClassOrInterfaceDeclaration c = getContainedClass(testRunnerPath, cu);
        String str = "@Suite.SuiteClasses({" + String.join(", ", testClasses) + "})";
        AnnotationExpr annotation = JavaParser.parseAnnotation(str);
        assert c != null;
        c.addAnnotation(annotation);
        writeCompilationUnitToFile(cu, generatedProjectPath + "/test/TestCoverageRunner.java");
    }

    private void copyTestFile(ProjectStructureNode node) throws IOException {
        CompilationUnit cu = ProjectParser.getCompilationUnitFromFile(node.getFilePath());
        String testFilePath = node.getFilePath();
        String path = generatedProjectPath + testFilePath.substring(testFilePath.indexOf("/"));
        writeCompilationUnitToFile(cu, path);
    }

    private ClassOrInterfaceDeclaration getContainedClass(String filePath, CompilationUnit cu) {
        String className = filePath.substring(filePath.lastIndexOf("/") + 1);
        className = className.substring(0, className.indexOf("."));
        Optional<ClassOrInterfaceDeclaration> o = cu.getClassByName(className);
        return o.orElse(null);
    }

}
