package d2j.reader.util;

public interface Out {
    void push();

    void s(String s);

    void s(String format, Object... arg);

    void pop();
}
