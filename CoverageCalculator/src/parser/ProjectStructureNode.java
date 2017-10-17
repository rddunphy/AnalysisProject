package parser;

import java.util.HashSet;
import java.util.Set;

public class ProjectStructureNode {

    private ProjectStructureNode parent;
    private CODE_UNIT type;
    private String name;
    private String filePath;
    private String javaPath;
    private Set<ProjectStructureNode> children;

    public ProjectStructureNode(ProjectStructureNode parent, CODE_UNIT type, String name, String filePath, String javaPath) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.filePath = filePath;
        this.javaPath = javaPath;
        this.children = new HashSet<>();
    }

    public ProjectStructureNode getParent() {
        return parent;
    }

    public Set<ProjectStructureNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getJavaPath() {
        return javaPath;
    }

    void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }

    public void addChild(ProjectStructureNode node) {
        children.add(node);
    }

    public CODE_UNIT getType() {
        return type;
    }

    public Set<ProjectStructureNode> getAllNodesOfType(CODE_UNIT type) {
        Set<ProjectStructureNode> nodes = new HashSet<>();
        for (ProjectStructureNode child : children) {
            if (child.getType() == type) {
                nodes.add(child);
            }
            nodes.addAll(child.getAllNodesOfType(type));
        }
        return nodes;
    }
}
