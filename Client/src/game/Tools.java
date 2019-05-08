package game;

import java.nio.ByteBuffer;

public class Tools {

    public static byte[] merge(float f1, float f2) {
        byte[] a1 = floatToBytes(f1);
        byte[] a2 = floatToBytes(f2);
        byte[] r = new byte[a1.length + a2.length];
        int i = 0;

        for (i = 0; i < a1.length; i++)
            r[i] = a1[i];

        for (i = i; i < a1.length + a2.length; i++)
            r[i] = a2[i - a1.length];

        return r;
    }

    public static float[] divide(byte[] a) {
        byte[] a1 = new byte[4];
        byte[] a2 = new byte[4];

        for (int i = 0; i < 4; i++)
            a1[i] = a[i];

        for (int i = 0; i < 4; i++)
            a2[i] = a[i + 4];

        float[] r = {bytesToFloat(a1), bytesToFloat(a2)};

        return r;
    }

    public static byte[] floatToBytes(float f) {
        return ByteBuffer.allocate(4).putFloat(f).array();
    }

    public static float bytesToFloat(byte[] b) {
        return ByteBuffer.wrap(b).getFloat();
    }

    public static String bytesToString(byte[] b) {
        byte[] trimmed;
        int i;
        for (i = 0; i < b.length; i++)
            if (b[i] == 0)
                break;

        trimmed = new byte[i];
        for (int x = 0; x < i; x++)
            trimmed[x] = b[x];

        return new String(trimmed);
    }
}
