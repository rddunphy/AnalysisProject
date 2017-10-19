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
import java.util.Optional;

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

    public static String getFullSignature(CompilationUnit cu, String name) {
        List<String> tokens = new ArrayList<>();
        Optional<ClassOrInterfaceDeclaration> oClass = cu.getClassByName(name);
        if (oClass.isPresent()) {
            tokens.addAll(getModifierTokens(oClass.get().getModifiers()));
            tokens.add("class");
        } else {
            oClass = cu.getInterfaceByName(name);
            if (oClass.isPresent()) {
                tokens.addAll(getModifierTokens(oClass.get().getModifiers()));
                tokens.add("interface");
            } else {
                Optional<EnumDeclaration> oEnum = cu.getEnumByName(name);
                if (oEnum.isPresent()) {
                    tokens.addAll(getModifierTokens(oEnum.get().getModifiers()));
                    tokens.add("enum");
                } else {
                    Optional<AnnotationDeclaration> oAnn = cu.getAnnotationDeclarationByName(name);
                    oAnn.ifPresent(annotationDeclaration -> {
                        tokens.addAll(getModifierTokens(annotationDeclaration.getModifiers()));
                        tokens.add("annotation");
                    });
                }
            }
        }
        tokens.add(name);
        return String.join(" ", tokens);
    }

    private static List<String> getModifierTokens(EnumSet<Modifier> modifiers) {
        List<String> tokens = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            tokens.add(modifier.asString());
        }
        return tokens;
    }
}
