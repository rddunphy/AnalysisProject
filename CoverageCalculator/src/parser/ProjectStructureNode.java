package parser;

import runtime.Coverage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a node in the project hierarchy tree. Node can be either a package, a class/
 * interface/enum/annotation declaration, or a method declaration.
 */
public class ProjectStructureNode implements Serializable {

    private final CODE_UNIT type;
    private final String name;
    private final String signature;
    private final String filePath;
    private String javaPath;
    private final Collection<ProjectStructureNode> children;
    private Coverage coverage;

    ProjectStructureNode(CODE_UNIT type, String name, String signature, String filePath,
                         String javaPath) {
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.filePath = filePath;
        this.javaPath = javaPath;
        this.children = new HashSet<>();
        this.coverage = null;
    }

    /**
     * @return A set containing all child nodes of this node
     */
    public Collection<ProjectStructureNode> getChildren() {
        return children;
    }

    /**
     * @return The name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * @return The signature of the node, including any modifiers and the type
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @return The path in the file structure
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return The path in the java package hierarchy
     */
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

    /**
     * @return The code unit type (i.e. level in the hierarchy) of the node
     */
    public CODE_UNIT getType() {
        return type;
    }

    /**
     * @return The coverage object associated with this node
     */
    public Coverage getCoverage() {
        return coverage;
    }

    /**
     * @param coverage The coverage object to be associated with this node
     */
    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    /**
     * @param type The code unit type to search for
     * @return A set containing all nodes of the given type contained within the tree
     */
    public Collection<ProjectStructureNode> getAllNodesOfType(CODE_UNIT type) {
        Collection<ProjectStructureNode> nodes = new HashSet<>();
        for (ProjectStructureNode child : children) {
            if (child.getType() == type) {
                nodes.add(child);
            }
            nodes.addAll(child.getAllNodesOfType(type));
        }
        return nodes;
    }
}
