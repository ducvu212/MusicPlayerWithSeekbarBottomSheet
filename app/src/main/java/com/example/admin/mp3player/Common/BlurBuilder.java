package com.example.admin.mp3player.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.security.AccessControlContext;

/**
 * Created by VuMinhDuc on 17/05/14.
 */

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        if (context != null) {
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            }
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                theIntrinsic.setRadius(BLUR_RADIUS);
                theIntrinsic.setInput(tmpIn);
                theIntrinsic.forEach(tmpOut);
            }

            tmpOut.copyTo(outputBitmap);
        }
        return outputBitmap;
    }
}
