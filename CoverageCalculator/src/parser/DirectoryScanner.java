package parser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DirectoryScanner {

    private MyNode tree;

    public DirectoryScanner() {
        tree = new MyNode(null, CODE_UNIT.PROJECT,"root", "");
    }

    public MyNode scan(String root) {
        return scan(new File(root));
    }

    public MyNode scan(File root) {
        return scan("", root, tree);
    }

    private MyNode scan(String path, File file, MyNode node) {
        if (file.isDirectory()) {
            MyNode childNode = new MyNode(node, CODE_UNIT.PACKAGE, file.getName(), path);
            for (File child : file.listFiles()) {
                scan(path + "/" + child.getName(), child, childNode);
            }
            if (!childNode.getChildren().isEmpty()) {
                node.addChild(childNode);
            }
        } else {
            if (path.endsWith(".java")) {
                String unitName = file.getName().substring(file.getName().lastIndexOf("."));
                MyNode childNode = new MyNode(node, CODE_UNIT.CLASS, unitName, path);
            }
        }
        return tree;
    }
}
