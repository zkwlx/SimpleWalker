package d2j.reader.zip;

import java.io.ByteArrayOutputStream;

public class AccessBufByteArrayOutputStream extends ByteArrayOutputStream {
    public byte[] getBuf() {
        return buf;
    }
}
