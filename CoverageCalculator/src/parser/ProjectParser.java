package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProjectParser {

    private String sourceProjectPath;
    private ProjectStructureNode sourceFilesTree;
    private ProjectStructureNode testFilesTree;

    public ProjectParser(String sourceProjectPath) {
        this.sourceProjectPath = sourceProjectPath;
        DirectoryScanner scanner = new DirectoryScanner();
        sourceFilesTree = scanner.scan(sourceProjectPath + "/src");
        testFilesTree = scanner.scan(sourceProjectPath + "/test");
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

    public ProjectStructureNode getTestFiles() {
        return testFilesTree;
    }

    public ProjectStructureNode getSourceFiles() {
        return sourceFilesTree;
    }

}
