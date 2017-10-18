package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.*;

public class ProjectParser {

    private final ProjectStructureNode sourceFilesTree;
    private final ProjectStructureNode testFilesTree;

    public ProjectParser(String sourceProjectPath) {
        DirectoryScanner scanner = new DirectoryScanner();
        sourceFilesTree = scanner.scan(sourceProjectPath + "/src");
        addMethodsToSourceFilesTree();
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

    private void addMethodsToSourceFilesTree() {
        for (ProjectStructureNode node : sourceFilesTree.getAllNodesOfType(CODE_UNIT.CLASS)) {
            try {
                CompilationUnit cu = JavaParser.parse(new File(node.getFilePath()));
                MethodNodeVisitor visitor = new MethodNodeVisitor(node);
                cu.accept(visitor, null);
                node.addChildren(visitor.getNodes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
