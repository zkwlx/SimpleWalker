package d2j.api.node.insn;


import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;

public class Stmt0RNode extends DexStmtNode {
    public Stmt0RNode(Op op) {
        super(op);
    }
    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitStmt0R(op);
    }
}
