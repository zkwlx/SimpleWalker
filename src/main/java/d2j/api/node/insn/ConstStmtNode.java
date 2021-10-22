package d2j.api.node.insn;


import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;

public class ConstStmtNode extends DexStmtNode {
    public final int a;
    public final Object value;

    public ConstStmtNode(Op op, int a, Object value) {
        super(op);
        this.a = a;
        this.value = value;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitConstStmt(op, a, value);
    }
}
