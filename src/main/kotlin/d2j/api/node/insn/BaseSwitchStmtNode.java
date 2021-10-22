package d2j.api.node.insn;


import d2j.api.DexLabel;
import d2j.api.reader.Op;

public abstract class BaseSwitchStmtNode extends DexStmtNode {

    public final int a;
    public final DexLabel[] labels;

    protected BaseSwitchStmtNode(Op op, int a, DexLabel[] labels) {
        super(op);
        this.a = a;
        this.labels = labels;
    }
}
