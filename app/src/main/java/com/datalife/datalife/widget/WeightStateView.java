package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.util.DrawUtil;
import com.datalife.datalife.util.StandardUtils;

/**
 * Created by LG on 2018/1/18.
 */

public class WeightStateView extends View {
    private float location;
    private int width;
    private Paint paint;
    private Paint textPain;
    private double value;
    private Context context;
    private StandardUtils.Standard standard;
    private double startWeight;
    private Bitmap bit = ((BitmapDrawable) getResources().getDrawable(
            R.mipmap.guo_measure_location_green)).getBitmap();
    ;
    private String unit = "";
    private String text_1 = "", text_2 = "", text_3 = "";

    public WeightStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public WeightStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public WeightStateView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        // int height=MeasureSpec.getSize(heightMeasureSpec);

    }

    private void init(Context context) {
        this.context = context;
        location = 0;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(35);

        textPain = new Paint();
        textPain.setAntiAlias(true);
        textPain.setTextSize(30);


        standard = new StandardUtils.Standard();
        standard.setMin(18.5);
        standard.setMax(28);

        setLanguage();
//        standard = StandardUtils.comparsionWithStandard(context, week, bmi);
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                getStandard();
//            }
//        }).start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Bitmap bit2 = ((BitmapDrawable) getResources().getDrawable(
                R.mipmap.guo_measure_line_weightrange)).getBitmap();


        float textWidth = DrawUtil.getTextWidth(35, "增长");
        canvas.drawText(text_1, (float) (width * 1.0 / 6) - textWidth / 2,
                bit.getHeight() + bit2.getHeight() + 50, textPain);
        canvas.drawText(text_2, (float) (width * 1.0 / 2) - textWidth / 2,
                bit.getHeight() + bit2.getHeight() + 50, textPain);
        canvas.drawText(text_3, (float) (width * 5.0 / 6) - textWidth / 2,
                bit.getHeight() + bit2.getHeight() + 50, textPain);
        if (standard != null) {
//            canvas.drawText(standard.getMin() + unit, (float) (width * 1.0 / 3 - DrawUtil.getTextWidth(35, standard.getMin() + "") / 2), bit.getHeight() + bit2.getHeight() - 10, paint);
//            canvas.drawText(standard.getMax() + unit, (float) (width * 2.0 / 3 - DrawUtil.getTextWidth(35, standard.getMax() + "") / 2), bit.getHeight() + bit2.getHeight() - 10, paint);
        }
        canvas.drawBitmap(bit2, (float) ((width - bit2.getWidth()) * 1.0 / 2),
                bit.getHeight(), paint);
        canvas.drawBitmap(bit, location, 0, paint);
    }

    public void setValue(double value, StandardUtils.Standard stan, String unit) {
        setLanguage();
        value = value - startWeight;
        this.unit = unit;
        if (stan != null)
            standard = stan;
        if (standard != null) {
            double minV = 2 * standard.getMin() - standard.getMax();
            double maxV = 2 * standard.getMax() - standard.getMin();
            if (value < minV) {
                location = 0;
            } else if (value > maxV) {
                location = width - bit.getWidth();
            } else {
                location = (float) ((value - minV) * width / (maxV - minV)) - bit.getWidth();
            }
            invalidate();
        }
    }

    public void setValue(int mywidth, double value, StandardUtils.Standard stan, String unit) {
        setLanguage();
        value = value - startWeight;
        if (width == 0)
            width = mywidth;
        this.unit = unit;
        if (stan != null)
            standard = stan;
        if (standard != null) {
            double minV = 2 * standard.getMin() - standard.getMax();
            double maxV = 2 * standard.getMax() - standard.getMin();
            if (value < minV) {
                location = 0;
            } else if (value > maxV) {
                location = width - bit.getWidth();
            } else {
                location = (float) ((value - minV) * width / (maxV - minV)) - bit.getWidth();
            }
            invalidate();
        }
    }

    private void setLanguage() {
        String mode;
        mode = "0";
        text_1 = getResources().getString(R.string.text_Low_text);
        text_2 = getResources().getString(R.string.text_Standard_text);
        text_3 = getResources().getString(R.string.text_High_text);
    }


}
