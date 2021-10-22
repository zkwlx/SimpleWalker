package d2j.api.node.insn;


import d2j.api.DexLabel;
import d2j.api.visitors.DexCodeVisitor;

public class DexLabelStmtNode extends DexStmtNode {
    public DexLabel label;

    public DexLabelStmtNode(DexLabel label) {
        super(null);
        this.label = label;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitLabel(label);
    }
}
