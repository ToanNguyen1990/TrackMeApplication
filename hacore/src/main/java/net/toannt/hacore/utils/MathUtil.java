package net.toannt.hacore.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class MathUtil {

    public static short[] convertByteToShort(byte[] data) {
        ShortBuffer buffer = ShortBuffer.allocate(data.length);

        for (byte d : data) {
            //cast to data
            buffer.put((short) (d & 0XFF));
        }

        return buffer.array();
    }

    public static byte[] convertShortToByte(short[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);

        for (short d : data) {
            buffer.put((byte) d);
        }

        return buffer.array();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
