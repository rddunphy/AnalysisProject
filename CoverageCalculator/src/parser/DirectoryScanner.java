package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

public class DirectoryScanner {

    private ProjectStructureNode tree;

    public ProjectStructureNode scan(String root) {
        return scan(new File(root));
    }

    public ProjectStructureNode scan(File root) {
        tree = null;//new ProjectStructureNode(null, CODE_UNIT.SOURCE_DIR, "root", root.getPath(), "");
        scan("", root, tree);
        return tree;
    }

    private void scan(String path, File file, ProjectStructureNode parent) {
        if (file.isDirectory()) {
                ProjectStructureNode node = new ProjectStructureNode(parent, CODE_UNIT.PACKAGE, file.getName(), file.getPath(), "");
            for (File childFile : file.listFiles()) {
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
        } else {
            if (path.endsWith(".java")) {
                String unitName = file.getName().substring(0, file.getName().lastIndexOf("."));
                try {
                    CompilationUnit cu = JavaParser.parse(file);
                    String javaPath = unitName;
                    if (cu.getPackageDeclaration().isPresent()) {
                        javaPath = cu.getPackageDeclaration().get().getNameAsString() + "." + javaPath;
                    }
                    ProjectStructureNode childNode = new ProjectStructureNode(parent, CODE_UNIT.CLASS, unitName, file.getPath(), javaPath);
                    parent.addChild(childNode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
