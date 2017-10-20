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

/**
 * Parses a source module and creates trees corresponding to the source and test directories
 * of the module.
 */
public class ProjectParser {

    private final ProjectStructureNode sourceFilesTree;
    private final ProjectStructureNode testFilesTree;

    /**
     * Parses the source project and generates corresponding project structure trees.
     *
     * @param sourceProjectPath
     */
    public ProjectParser(String sourceProjectPath) {
        DirectoryScanner scanner = new DirectoryScanner();
        sourceFilesTree = scanner.scan(sourceProjectPath + "/src");
        addMethodsToSourceFilesTree();
        testFilesTree = scanner.scan(sourceProjectPath + "/test");
    }

    /**
     * Utility method to convert a file to a compilation unit with all comments removed.
     *
     * @param filePath
     * @return The compilation unit
     * @throws IOException
     */
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

    /**
     * Retrieve the generated test file tree.
     *
     * @return The root of the test file tree
     */
    public ProjectStructureNode getTestFiles() {
        return testFilesTree;
    }

    /**
     * Retrieve the generated source file tree.
     *
     * @return The root of the source file tree
     */
    public ProjectStructureNode getSourceFiles() {
        return sourceFilesTree;
    }

    /**
     * Utility method to generate the full signature of a class, interface, enum, or annotation
     * declaration.
     *
     * @param declaration
     * @return A signature of the form "public abstract class MyClass"
     */
    static String getFullSignature(TypeDeclaration declaration) {
        List<String> tokens = new ArrayList<>();
        EnumSet<Modifier> modifiers = declaration.getModifiers();
        for (Modifier modifier : modifiers) {
            tokens.add(modifier.asString());
        }
        if (declaration instanceof ClassOrInterfaceDeclaration) {
            if (((ClassOrInterfaceDeclaration) declaration).isInterface()) {
                tokens.add("interface");
            } else {
                tokens.add("class");
            }
        } else if (declaration instanceof AnnotationDeclaration) {
            tokens.add("annotation");
        } else if (declaration instanceof EnumDeclaration) {
            tokens.add("enum");
        }
        tokens.add(declaration.getNameAsString());
        tokens.add(declaration.getNameAsString());
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
