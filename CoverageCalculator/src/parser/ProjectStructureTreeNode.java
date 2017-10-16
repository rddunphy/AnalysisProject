package parser;

import java.util.HashSet;
import java.util.Set;

public class ProjectStructureTreeNode {

    private ProjectStructureTreeNode parent;
    private CODE_UNIT type;
    private String name;
    private String path;
    private Set<ProjectStructureTreeNode> children;

    public ProjectStructureTreeNode(ProjectStructureTreeNode parent, CODE_UNIT type, String name, String path) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.path = path;
        this.children = new HashSet<>();
    }

    public ProjectStructureTreeNode getParent() {
        return parent;
    }

    public Set<ProjectStructureTreeNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void addChild(ProjectStructureTreeNode node) {
        children.add(node);
    }

    public CODE_UNIT getType() {
        return type;
    }

    public Set<ProjectStructureTreeNode> getAllNodesOfType(CODE_UNIT type) {
        Set<ProjectStructureTreeNode> nodes = new HashSet<>();
        for (ProjectStructureTreeNode child : children) {
            if (child.getType() == type) {
                nodes.add(child);
            }
            nodes.addAll(child.getAllNodesOfType(type));
        }
        return nodes;
    }
}
