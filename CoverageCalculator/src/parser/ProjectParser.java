package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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

    static String getFullSignature(CompilationUnit cu, String name) {
        List<String> tokens = new ArrayList<>();
        TypeDeclaration type = cu.getType(0);
        EnumSet<Modifier> modifiers = type.getModifiers();
        for (Modifier modifier : modifiers) {
            tokens.add(modifier.asString());
        }
        if (type instanceof ClassOrInterfaceDeclaration) {
            if (((ClassOrInterfaceDeclaration) type).isInterface()) {
                tokens.add("interface");
            } else {
                tokens.add("class");
            }
        } else if (type instanceof AnnotationDeclaration) {
            tokens.add("annotation");
        } else if (type instanceof EnumDeclaration) {
            tokens.add("enum");
        }
        tokens.add(type.getNameAsString());
        tokens.add(name);
        return String.join(" ", tokens);
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
