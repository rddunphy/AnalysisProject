package parser;

import java.util.HashSet;
import java.util.Set;

public class MyNode {

    private MyNode parent;
    private CODE_UNIT type;
    private String name;
    private String path;
    private Set<MyNode> children;

    public MyNode(MyNode parent, CODE_UNIT type, String name, String path) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.path = path;
        this.children = new HashSet<>();
    }

    public MyNode getParent() {
        return parent;
    }

    public Set<MyNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void addChild(MyNode node) {
        children.add(node);
    }

    public CODE_UNIT getType() {
        return type;
    }

    public Set<MyNode> getAllNodesOfType(CODE_UNIT type) {
        Set<MyNode> nodes = new HashSet<>();
        for (MyNode child : children) {
            if (child.getType() == type) {
                nodes.add(child);
            }
            nodes.addAll(child.getAllNodesOfType(type));
        }
        return nodes;
    }
}
