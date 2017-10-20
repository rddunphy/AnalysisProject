package generator;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

/**
 * Visits methods and inserts probes into all block statements using a ProbeInsertionVisitor.
 * Used instead of calling ProbeInsertionVisitor directly so that the ProbeInsertionVisitor can
 * have access to method signature information.
 */
class MethodProcessingVisitor extends VoidVisitorAdapter<String> {

    private final VoidVisitorAdapter<String> blockVisitor = new ProbeInsertionVisitor();

    /**
     * Pass the signature of the given method to a ProbeInsertionVisitor.
     *
     * @param method
     * @param path
     */
    public void visit(MethodDeclaration method, String path) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = path + "." + method.getSignature().asString();
        body.accept(blockVisitor, signature);
    }

    /**
     * Pass the signature of the given constructor to a ProbeInsertionVisitor.
     *
     * @param constructor
     * @param path
     */
    public void visit(ConstructorDeclaration constructor, String path) {
        BlockStmt body = constructor.getBody();
        String signature = path + "." + constructor.getSignature().asString();
        body.accept(blockVisitor, signature);
    }
}
