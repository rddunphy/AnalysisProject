package generator;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

class MethodProcessingVisitor extends VoidVisitorAdapter<String> {

    private final VoidVisitorAdapter<String> blockVisitor = new ProbeInsertionVisitor();

    public void visit(MethodDeclaration method, String path) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = path + "." + method.getSignature().asString();
        body.accept(blockVisitor, signature);
    }

    public void visit(ConstructorDeclaration constructor, String path) {
        BlockStmt body = constructor.getBody();
        String signature = path + "." + constructor.getSignature().asString();
        body.accept(blockVisitor, signature);
    }
}
