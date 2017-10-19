package parser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

class MethodNodeVisitor extends VoidVisitorAdapter<Void> {

    private final Collection<ProjectStructureNode> nodes = new HashSet<>();
    private final ProjectStructureNode parent;

    MethodNodeVisitor(ProjectStructureNode parent) {
        this.parent = parent;
    }

    private void processCallable(CallableDeclaration callable, EnumSet<Modifier> modifiers, String type) {
        String name = callable.getSignature().asString();
        List<String> signatureTokens = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            signatureTokens.add(modifier.asString());
        }
        if (type != null) {
            signatureTokens.add(type);
        }
        signatureTokens.add(name);
        String signature = String.join(" ", signatureTokens);
        String javaPath = parent.getJavaPath() + "." + name;
        nodes.add(new ProjectStructureNode(CODE_UNIT.METHOD, name, signature, parent.getFilePath(), javaPath));
    }

    public void visit(MethodDeclaration method, Void v) {
        processCallable(method, method.getModifiers(), method.getType().asString());
    }

    public void visit(ConstructorDeclaration constructor, Void v) {
        processCallable(constructor, constructor.getModifiers(), null);
    }

    Collection<ProjectStructureNode> getNodes() {
        return nodes;
    }
}
