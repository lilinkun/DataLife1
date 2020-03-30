package com.datalife.datalife.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.DisplayMetrics;

/**
 * Created by LG on 2018/1/18.
 */

public class DrawUtil {
    public static double getTextHeight(float frontSize) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return Math.ceil(fm.descent - fm.top) + 2;
    }

    public static float getTextWidth(float frontSize, String text) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        return paint.measureText(text);

    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        return new int[]{displayWidth, displayHeight};
    }

    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }


    public static int sp2px(Context context, float spValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }


    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }


    public static float getScaleTextWidth(TextPaint itemsPaint, String string) {
        // TODO Auto-generated method stub
        return itemsPaint.measureText(string);
    }


}