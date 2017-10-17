package parser;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class MethodNodeVisitor extends VoidVisitorAdapter {

    Set<ProjectStructureNode> nodes = new HashSet<>();
    ProjectStructureNode parent;

    public MethodNodeVisitor(ProjectStructureNode parent) {
        this.parent = parent;
    }

    public void visit(MethodDeclaration method, Object arg) {
        String signature = method.getSignature().asString();
        String javaPath = parent.getJavaPath() + "." + signature;
        nodes.add(new ProjectStructureNode(parent, CODE_UNIT.METHOD, signature, parent.getFilePath(), javaPath));
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        String signature = constructor.getSignature().asString();
        String javaPath = parent.getJavaPath() + "." + signature;
        nodes.add(new ProjectStructureNode(parent, CODE_UNIT.METHOD, signature, parent.getFilePath(), javaPath));
    }

    public Set<ProjectStructureNode> getNodes() {
        return nodes;
    }
}
