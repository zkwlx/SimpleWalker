package d2j.api.node.insn;


import d2j.api.Field;
import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;

public class FieldStmtNode extends DexStmtNode {

    public final int a;
    public final int b;
    public final Field field;

    public FieldStmtNode(Op op, int a, int b, Field field) {
        super(op);
        this.a = a;
        this.b = b;
        this.field = field;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitFieldStmt(op, a, b, field);
    }
}
