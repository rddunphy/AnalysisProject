package coverage;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ProjectGenerator pg = new ProjectGenerator("ExampleApplication", "Generated");
        pg.generate();

    }
}
