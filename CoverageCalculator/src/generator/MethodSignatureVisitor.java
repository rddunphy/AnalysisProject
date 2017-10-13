package generator;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class MethodSignatureVisitor extends VoidVisitorAdapter {

    private Set<String> allMethods = new HashSet<>();

    public void visit(MethodDeclaration method, Object arg) {
        if (method.getBody().isPresent()) {
            String signature = method.getSignature().asString();
            allMethods.add(signature);
        }
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        String signature = constructor.getSignature().asString();
        allMethods.add(signature);
    }

    public Set<String> getAllMethodSignatures() {
        return this.allMethods;
    }
}