package coverage;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("ExampleApplication/src/ea/ComplexNumber.java");
        CompilationUnit cu;
        try (FileInputStream fis = new FileInputStream(file)) {
            cu = JavaParser.parse(fis);
        }
        new ProbeInsertionVisitor().visit(cu, null);
        System.out.println(cu.toString());
    }
}
