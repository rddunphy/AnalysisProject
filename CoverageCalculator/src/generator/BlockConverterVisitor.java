package generator;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Visits all compound statements (i.e. statements which may contain a block statement, such as
 * for statements) and replaces the body of the statement with an equivalent block statement if
 * it is not a block statement.
 */
class BlockConverterVisitor extends VoidVisitorAdapter<Void> {

    private BlockStmt createBlock(Statement stmt) {
        BlockStmt block = new BlockStmt();
        block.addStatement(stmt);
        return block;
    }

    public void visit(SwitchEntryStmt stmt, Void v) {
        BlockStmt block = new BlockStmt();
        for (Statement s : stmt.getStatements()) {
            block.addStatement(s);
        }
        NodeList<Statement> nodeList = new NodeList<>();
        nodeList.add(block);
        stmt.setStatements(nodeList);
    }

    public void visit(DoStmt stmt, Void v) {
        if (!stmt.getBody().isBlockStmt()) {
            stmt.setBody(createBlock(stmt.getBody()));
        }
        super.visit(stmt, v);
    }

    public void visit(WhileStmt stmt, Void v) {
        if (!stmt.getBody().isBlockStmt()) {
            stmt.setBody(createBlock(stmt.getBody()));
        }
        super.visit(stmt, v);
    }

    public void visit(ForeachStmt stmt, Void v) {
        if (!stmt.getBody().isBlockStmt()) {
            stmt.setBody(createBlock(stmt.getBody()));
        }
        super.visit(stmt, null);
    }

    public void visit(ForStmt stmt, Void v) {
        if (!stmt.getBody().isBlockStmt()) {
            stmt.setBody(createBlock(stmt.getBody()));
        }
        super.visit(stmt, null);
    }

    public void visit(IfStmt stmt, Void v) {
        if (!stmt.getThenStmt().isBlockStmt()) {
            stmt.setThenStmt(createBlock(stmt.getThenStmt()));
        }
        if (stmt.getElseStmt().isPresent() && !stmt.getElseStmt().get().isBlockStmt()) {
            stmt.setElseStmt(createBlock(stmt.getElseStmt().get()));
        }
        super.visit(stmt, null);
    }
}
