package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectParser {

    private String sourceProjectPath;

    public ProjectParser(String sourceProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
    }

    public static CompilationUnit getCompilationUnitFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        CompilationUnit cu;
        try (InputStream input = new FileInputStream(file)) {
            cu = JavaParser.parse(input);
        }
        for (Comment comment : cu.getAllContainedComments()) {
            comment.remove();
        }
        return cu;
    }

    public Map<String, CompilationUnit> getAllTestFiles() {
        Map<String, CompilationUnit> map = new HashMap<>();
        try {
            map.put("ea/ComplexNumberTest.java", getCompilationUnitFromFile(sourceProjectPath + "/test/ea/ComplexNumberTest.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, CompilationUnit> getAllSourceFiles() {
        Map<String, CompilationUnit> map = new HashMap<>();
        try {
            map.put("ea/ComplexNumber.java", getCompilationUnitFromFile(sourceProjectPath + "/src/ea/ComplexNumber.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<CompilationUnit> getAllSourceCompilationUnits() {
        List<CompilationUnit> l = new ArrayList<>();
        try {
            l.add(getCompilationUnitFromFile("../" +sourceProjectPath + "/src/ea/ComplexNumber.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }
}
