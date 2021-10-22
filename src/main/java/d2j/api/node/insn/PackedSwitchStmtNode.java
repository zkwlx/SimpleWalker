package d2j.api.node.insn;


import d2j.api.DexLabel;
import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;

public class PackedSwitchStmtNode extends BaseSwitchStmtNode {

    public final int first_case;

    public PackedSwitchStmtNode(Op op, int a, int first_case, DexLabel[] labels) {
        super(op, a,labels);
        this.first_case = first_case;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitPackedSwitchStmt(op, a, first_case, labels);
    }
}
