package parser;

import java.io.File;

public class DirectoryScanner {

    private ProjectStructureTreeNode tree;

    public DirectoryScanner() {
        tree = new ProjectStructureTreeNode(null, CODE_UNIT.PROJECT,"root", "");
    }

    public ProjectStructureTreeNode scan(String root) {
        return scan(new File(root));
    }

    public ProjectStructureTreeNode scan(File root) {
        return scan("", root, tree);
    }

    private ProjectStructureTreeNode scan(String path, File file, ProjectStructureTreeNode node) {
        if (file.isDirectory()) {
            ProjectStructureTreeNode childNode = new ProjectStructureTreeNode(node, CODE_UNIT.PACKAGE, file.getName(), file.getPath());
            for (File child : file.listFiles()) {
                scan(path + "/" + child.getName(), child, childNode);
            }
            if (!childNode.getChildren().isEmpty()) {
                node.addChild(childNode);
            }
        } else {
            if (path.endsWith(".java")) {
                String unitName = file.getName().substring(0, file.getName().lastIndexOf("."));
                ProjectStructureTreeNode childNode = new ProjectStructureTreeNode(node, CODE_UNIT.CLASS, unitName, file.getPath());
                node.addChild(childNode);
            }
        }
        return tree;
    }
}
