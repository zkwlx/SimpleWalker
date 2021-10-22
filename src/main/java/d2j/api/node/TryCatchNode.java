package d2j.api.node;


import d2j.api.DexLabel;
import d2j.api.visitors.DexCodeVisitor;

public class TryCatchNode {

    public final DexLabel start;
    public final DexLabel end;
    public final DexLabel[] handler;
    public final String[] type;

    public TryCatchNode(DexLabel start, DexLabel end, DexLabel[] handler, String[] type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    public void accept(DexCodeVisitor cv) {
        cv.visitTryCatch(start, end, handler, type);
    }
}
