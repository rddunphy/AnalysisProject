package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.FileNotFoundException;

class DirectoryScanner {

    private ProjectStructureNode tree;

    ProjectStructureNode scan(String root) {
        this.tree = null;
        File file = new File(root);
        scan("", file, null);
        return tree;
    }

    private void scan(String path, File file, ProjectStructureNode parent) {
        File[] files = file.listFiles();
        if (files != null) { // file is a directory
            CODE_UNIT type = (parent == null) ? CODE_UNIT.ROOT : CODE_UNIT.PACKAGE;
            ProjectStructureNode node = new ProjectStructureNode(type, file.getName(), "package " + file.getName(), file.getPath(), "");
            for (File childFile : files) {
                scan(path + "/" + childFile.getName(), childFile, node);
            }
            if (!node.getChildren().isEmpty()) {
                for (ProjectStructureNode child : node.getChildren()) {
                    if (node.getJavaPath().equals("") && child.getJavaPath().contains(".")) {
                        node.setJavaPath(child.getJavaPath().substring(0, child.getJavaPath().lastIndexOf(".")));
                    }
                }
            }
            if (parent == null) {
                tree = node;
            } else {
                parent.addChild(node);
            }
        } else if (path.endsWith(".java")) {
            String unitName = file.getName().substring(0, file.getName().lastIndexOf("."));
            try {
                CompilationUnit cu = JavaParser.parse(file);
                String javaPath = "";
                if (cu.getPackageDeclaration().isPresent()) {
                    javaPath = cu.getPackageDeclaration().get().getNameAsString() + ".";
                }
                for (TypeDeclaration declaration : cu.getTypes()) {
                    String name = declaration.getNameAsString();
                    String signature = ProjectParser.getFullSignature(declaration);
                    ProjectStructureNode childNode = new ProjectStructureNode(CODE_UNIT.CLASS, name, signature, file.getPath(), javaPath + name);
                    parent.addChild(childNode);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
