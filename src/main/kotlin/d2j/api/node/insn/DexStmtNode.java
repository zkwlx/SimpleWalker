package d2j.api.node.insn;


import d2j.api.reader.Op;
import d2j.api.visitors.DexCodeVisitor;


public abstract class DexStmtNode {
    public final Op op;

    public int __index;

    protected DexStmtNode(Op op) {
        this.op = op;
    }

    public abstract void accept(DexCodeVisitor cv);
}
