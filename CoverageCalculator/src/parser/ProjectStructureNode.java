package parser;

import runtime.Coverage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProjectStructureNode implements Serializable {

    private final CODE_UNIT type;
    private final String name;
    private final String signature;
    private final String filePath;
    private String javaPath;
    private final Set<ProjectStructureNode> children;
    private Coverage coverage;

    ProjectStructureNode(CODE_UNIT type, String name, String signature, String filePath, String javaPath) {
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.filePath = filePath;
        this.javaPath = javaPath;
        this.children = new HashSet<>();
        this.coverage = null;
    }

    public Set<ProjectStructureNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
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

    void addChild(ProjectStructureNode node) {
        children.add(node);
    }

    void addChildren(Collection<ProjectStructureNode> nodes) {
        children.addAll(nodes);
    }

    public CODE_UNIT getType() {
        return type;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
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
