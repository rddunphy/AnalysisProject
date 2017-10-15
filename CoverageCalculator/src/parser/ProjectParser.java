package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        String root = sourceProjectPath + "/test";
        return getAllJavaFiles(root);
    }

    public Map<String, CompilationUnit> getAllSourceFiles() {
        String root = sourceProjectPath + "/src";
        return getAllJavaFiles(root);
    }

    public Map<String, CompilationUnit> getAllJavaFiles(String root) {
        Map<String, CompilationUnit> map = new HashMap<>();
        DirectoryScanner scanner = new DirectoryScanner();
        Set<String> filePaths = scanner.scan(root);
        for (String path : filePaths) {
            try {
                map.put(path, getCompilationUnitFromFile(root + "/" + path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
