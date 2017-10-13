package coverage;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class ProjectGenerator {

    private String sourceProjectPath;
    private String generatedProjectPath;

    private static final String templatePath = "CoverageCalculator/templates";

    public ProjectGenerator(String sourceProjectPath, String generatedProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
        this.generatedProjectPath = generatedProjectPath;
    }

    public void generate() {
        try {
            copyCoverageLogger();
            copySourceFile("ea/ComplexNumber.java");
            copyTestFile("ea/ComplexNumberTest.java");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private CompilationUnit getCompilationUnitFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        CompilationUnit cu;
        try (InputStream fis = new FileInputStream(file)) {
            cu = JavaParser.parse(fis);
        }
        return cu;
    }

    private void writeCompilationUnitToFile(CompilationUnit cu, String filePath) throws IOException {
        String source = cu.toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        Files.write(file.toPath(), source.getBytes(StandardCharsets.UTF_8));
    }

    private void copySourceFile(String sourceFilePath) throws IOException {
        CompilationUnit cu = getCompilationUnitFromFile(sourceProjectPath + "/src/" + sourceFilePath);
        cu.addImport("cc.CoverageLogger");
        new ProbeInsertionVisitor().visit(cu, null);
        writeCompilationUnitToFile(cu, generatedProjectPath + "/src/" + sourceFilePath);
    }

    private void copyCoverageLogger() throws IOException {
        CompilationUnit cu = getCompilationUnitFromFile(templatePath + "/CoverageLogger.java");
        writeCompilationUnitToFile(cu, generatedProjectPath + "/src/cc/CoverageLogger.java");
    }

    private void copyTestFile(String testFilePath) throws IOException {
        String path = sourceProjectPath + "/test/" + testFilePath;
        CompilationUnit cu = getCompilationUnitFromFile(path);
        Optional<ClassOrInterfaceDeclaration> o = cu.getClassByName("ComplexNumberTest");
        if (o.isPresent()) {
            ClassOrInterfaceDeclaration c = o.get();
            cu.addImport("org.junit.BeforeClass");
            cu.addImport("org.junit.AfterClass");
            cu.addImport("cc.CoverageLogger");
            for (MethodDeclaration method : getJUnitCoverageMethods()) {
                c.addMember(method);
            }
        } else {
            throw new IOException("No class named ComplexNumberTest in ComplexNumberTest.java");
        }
        writeCompilationUnitToFile(cu, generatedProjectPath + "/test/" + testFilePath);
    }

    private List<MethodDeclaration> getJUnitCoverageMethods() throws IOException {
        CompilationUnit cu = getCompilationUnitFromFile(templatePath + "/JUnitMethods.java");
        Optional<ClassOrInterfaceDeclaration> o = cu.getClassByName("JUnitMethods");
        if (o.isPresent()) {
            ClassOrInterfaceDeclaration c = o.get();
            return c.getMethods();
        } else {
            throw new IOException("Problem reading JUnitMethods.java.");
        }
    }

}
