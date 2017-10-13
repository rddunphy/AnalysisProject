package generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import parser.ProjectParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static parser.ProjectParser.getCompilationUnitFromFile;

public class ProjectGenerator {

    private String sourceProjectPath;
    private String generatedProjectPath;

    private static final String templatePath = "CoverageCalculator/templates";

    public ProjectGenerator(String sourceProjectPath, String generatedProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
        this.generatedProjectPath = generatedProjectPath;
    }

    public void generate() {
        ProjectParser parser = new ProjectParser(sourceProjectPath);
        MethodSignatureVisitor msv = new MethodSignatureVisitor();
        try {
            for (Map.Entry<String, CompilationUnit> entry : parser.getAllSourceFiles().entrySet()) {
                copySourceFile(entry.getKey(), entry.getValue());
                entry.getValue().accept(msv, null);
            }
            for (Map.Entry<String, CompilationUnit> entry : parser.getAllTestFiles().entrySet()) {
                copyTestFile(entry.getKey(), entry.getValue(), msv.getAllMethodSignatures());
            }
        } catch (IOException e) {
            System.out.println(e);
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
        cu.accept(new ProbeInsertionVisitor(), null);
        writeCompilationUnitToFile(cu, generatedProjectPath + "/src/" + sourceFilePath);
    }

    private void copyTestFile(String testFilePath, CompilationUnit cu, Collection<String> methodSignatures) throws IOException {
        String path = sourceProjectPath + "/test/" + testFilePath;
        Optional<ClassOrInterfaceDeclaration> o = cu.getClassByName("ComplexNumberTest");
        if (o.isPresent()) {
            ClassOrInterfaceDeclaration c = o.get();
            cu.addImport("org.junit.BeforeClass");
            cu.addImport("org.junit.AfterClass");
            cu.addImport("runtime.CoverageLogger");
            cu.addImport("runtime.ReportGenerator");
            c.addMember(getJUnitSetupMethod(methodSignatures));
            c.addMember(getJUnitWrapupMethod());
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

    private MethodDeclaration getJUnitSetupMethod(Collection<String> methodSignatures) throws IOException {
        List<MethodDeclaration> methods = getJUnitCoverageMethods();
        MethodDeclaration method = methods.get(0);
        Optional<BlockStmt> o = method.getBody();
        if (o.isPresent()) {
            BlockStmt body = o.get();
            StringBuilder builder = new StringBuilder();
            builder.append("String[] signatures = new String[] {");
            methodSignatures = methodSignatures.stream().map(x -> "\"" + x + "\"").collect(Collectors.toSet());
            builder.append(String.join(", ", methodSignatures));
            builder.append("};");
            body.addStatement(builder.toString());
            body.addStatement("CoverageLogger.getInstance().setMethodSignatures(signatures);");
        }
        return method;
    }

    private MethodDeclaration getJUnitWrapupMethod() throws IOException {
        List<MethodDeclaration> methods = getJUnitCoverageMethods();
        return methods.get(1);
    }

}
