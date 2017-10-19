package parser;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Collection;
import java.util.HashSet;

class MethodNodeVisitor extends VoidVisitorAdapter<Object> {

    private final Collection<ProjectStructureNode> nodes = new HashSet<>();
    private final ProjectStructureNode parent;

    MethodNodeVisitor(ProjectStructureNode parent) {
        this.parent = parent;
    }

    public void visit(MethodDeclaration method, Object arg) {
        String name = method.getSignature().asString();
        String signature = method.getType() + " " + name;
        String javaPath = parent.getJavaPath() + "." + name;
        nodes.add(new ProjectStructureNode(CODE_UNIT.METHOD, name, signature, parent.getFilePath(), javaPath));
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        String name = constructor.getSignature().asString();
        String signature = name;
        String javaPath = parent.getJavaPath() + "." + name;
        nodes.add(new ProjectStructureNode(CODE_UNIT.METHOD, name, signature, parent.getFilePath(), javaPath));
    }

    Collection<ProjectStructureNode> getNodes() {
        return nodes;
    }
}
