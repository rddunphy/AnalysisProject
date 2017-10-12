package coverage;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class ProbeInsertionVisitor extends VoidVisitorAdapter {

    private void insertProbe(BlockStmt body, String name) {
        Statement stmt = JavaParser.parseStatement("System.out.println(\"" + name + " called.\");");
        body.addStatement(0, stmt);
    }

    public void visit(MethodDeclaration method, Object arg) {
        Optional<BlockStmt> o = method.getBody();
        if (!o.isPresent()) {
            return;
        }
        BlockStmt body = o.get();
        String signature = method.getSignature().asString();
        insertProbe(body, signature);
    }

    public void visit(ConstructorDeclaration constructor, Object arg) {
        BlockStmt body = constructor.getBody();
        String signature = constructor.getSignature().asString();
        insertProbe(body, signature);
    }
}
