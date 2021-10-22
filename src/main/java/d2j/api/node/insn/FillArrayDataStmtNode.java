package d2j.api.node.insn;


import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;

public class FillArrayDataStmtNode extends DexStmtNode {

    public final int ra;
    public final Object array;

    public FillArrayDataStmtNode(Op op, int ra, Object array) {
        super(op);
        this.ra = ra;
        this.array = array;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitFillArrayDataStmt(op, ra, array);
    }
}
