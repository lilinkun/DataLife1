package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.SyncStateContract;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.MainActivity;

/**
 * Created by Administrator on 2018/2/13.
 */

public class ChartView  extends View {

    private Context context;

    /** 原点的X坐标 */
    public float XPoint = 40;
    /** 原点的Y坐标 */
    public float YPoint = 260;
    /** X的刻度长度 */
    public float XScale = 55;
    /** Y的刻度长度 */
    public float YScale = 40;
    /** X轴的长度 */
    public float XLength = 380;

    /** 横线间的距离 */
    public float YBetween;

    /** x刻度间的距离 */
    public float XBetween;

    /** 七个点的x坐标 */
    private float c1x, c2x, c3x, c4x, c5x, c6x, c7x;
    /** 七个点的y坐标 */
    private float c1y, c2y, c3y, c4y, c5y, c6y, c7y;

    // 可移动的圆点
    private float cx, cy;
    private boolean isMove;
    private float downX, downY;
    private float k1, k2, k3, k4, k5, k6;// y=kx+b;
    private float b1, b2, b3, b4, b5, b6;

    private String txty1, txty2, txty3, txty4, txty5;
    private String[] txtx, txty;
    private String[] values;
    // 移动的值
    private String value = "0.00";

    public float XScaleY;

    /** Y轴的长度 */
    public float YLength = 240; // Y轴的长度
    /** X的刻度 */
    public String[] XLabel; // X的刻度
    /** Y的刻度 */
    public String[] YLabel; // Y的刻度
    /** 数据 */
    public String[] Data; // 数据
    /** 显示的标题 */
    public String Title; // 显示的标题

    public ChartView(Context context) {

        super(context);
        this.context = context;
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setData(String[] txtx, String[] txty, String[] values) {
        this.txtx = txtx;
        this.txty = txty;
        this.values = values;

// 折线区间的总高度
        float hei = YBetween * 4;
        float top = Float.parseFloat(txty[4]);
        float bottom = Float.parseFloat(txty[0]);

//初始化可移动的原点位置

        if (cx == c7x) {

            cy = c7y = (float) (hei * (top - Float.parseFloat(values[6])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[6];
        }
        if (cx == c6x) {
            cy = c6y = (float) (hei * (top - Float.parseFloat(values[5])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[5];
        }
        if (cx == c5x) {
            cy = c5y = (float) (hei * (top - Float.parseFloat(values[4])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[4];
        }
        if (cx == c4x) {
            cy = c4y = (float) (hei * (top - Float.parseFloat(values[3])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[3];
        }
        if (cx == c3x) {
            cy = c3y = (float) (hei * (top - Float.parseFloat(values[2])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[2];
        }
        if (cx == c2x) {
            cy = c2y = (float) (hei * (top - Float.parseFloat(values[1])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[1];
        }
        if (cx == c1x) {
            cy = c1y = (float) (hei * (top - Float.parseFloat(values[0])) / (top - bottom) + (YPoint - YBetween * 4));
            value = values[0];
        }
        invalidate();
    }

    public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, String strTitle) {
        XLabel = XLabels;
        YLabel = YLabels;
        Data = AllData;
        Title = strTitle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// 重写onDraw方法
        if (txty != null) {
            setPositions();
// canvas.drawColor(Color.WHITE);//设置背景颜色
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);// 去锯齿
            paint.setColor(context.getResources().getColor(R.color.gray));// 颜色
            Paint paint1 = new Paint();
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);// 去锯齿
            paint1.setColor(context.getResources().getColor(R.color.green));
            int lineWidth = (int) (MainActivity.SCREEN_WIDTH * 3 / 1080);
            paint1.setStrokeWidth(lineWidth);

            int textSize = (int) (MainActivity.SCREEN_WIDTH * 30 / 1080);

            paint.setTextSize(textSize); // 设置轴文字大小
// 设置Y轴
            canvas.drawLine(XPoint, YPoint - YBetween * 4 - MainActivity.SCREEN_HEIGHT * 35 / 1230, XPoint, YPoint, paint);
// 设置X轴
            canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线0
            canvas.drawLine(XPoint, YPoint - YBetween, XPoint + XLength, YPoint - YBetween, paint); // 轴线1
            canvas.drawLine(XPoint, YPoint - YBetween * 2, XPoint + XLength, YPoint - YBetween * 2, paint); // 轴线2
            canvas.drawLine(XPoint, YPoint - YBetween * 3, XPoint + XLength, YPoint - YBetween * 3, paint); // 轴线3
            canvas.drawLine(XPoint, YPoint - YBetween * 4, XPoint + XLength, YPoint - YBetween * 4, paint); // 轴线4

            canvas.drawText(txty[4], YScale, (YPoint - YBetween * 4) + 8, paint);// y刻度4
            canvas.drawText(txty[3], YScale, (YPoint - YBetween * 3) + 8, paint);// y刻度3
            canvas.drawText(txty[2], YScale, (YPoint - YBetween * 2) + 8, paint);// y刻度2
            canvas.drawText(txty[1], YScale, (YPoint - YBetween * 1) + 8, paint);// y刻度1
            canvas.drawText(txty[0], YScale, (YPoint - YBetween * 0) + 8, paint);// y刻度0

            canvas.drawText(txtx[0], XScale + XBetween - XBetween / 2, XScaleY, paint);// x刻度0
            canvas.drawText(txtx[1], XScale + XBetween * 2 - XBetween / 2, XScaleY, paint);// x刻度1
            canvas.drawText(txtx[2], XScale + XBetween * 3 - XBetween / 2, XScaleY, paint);// x刻度2
            canvas.drawText(txtx[3], XScale + XBetween * 4 - XBetween / 2, XScaleY, paint);// x刻度3
            canvas.drawText(txtx[4], XScale + XBetween * 5 - XBetween / 2, XScaleY, paint);// x刻度4
            canvas.drawText(txtx[5], XScale + XBetween * 6 - XBetween / 2, XScaleY, paint);// x刻度5
            canvas.drawText(txtx[6], XScale + XBetween * 7 - XBetween / 2, XScaleY, paint);// x刻度6

// 折线区间的总高度
            float hei = YBetween * 4;
            float top = Float.parseFloat(txty[4]);
            float bottom = Float.parseFloat(txty[0]);
            c1y = (float) (hei * (top - Float.parseFloat(values[0])) / (top - bottom) + (YPoint - YBetween * 4));// 5-4.625
            c2y = (float) (hei * (top - Float.parseFloat(values[1])) / (top - bottom) + (YPoint - YBetween * 4));
            c3y = (float) (hei * (top - Float.parseFloat(values[2])) / (top - bottom) + (YPoint - YBetween * 4));
            c4y = (float) (hei * (top - Float.parseFloat(values[3])) / (top - bottom) + (YPoint - YBetween * 4));
            c5y = (float) (hei * (top - Float.parseFloat(values[4])) / (top - bottom) + (YPoint - YBetween * 4));
            c6y = (float) (hei * (top - Float.parseFloat(values[5])) / (top - bottom) + (YPoint - YBetween * 4));
            c7y = (float) (hei * (top - Float.parseFloat(values[6])) / (top - bottom) + (YPoint - YBetween * 4));

            c1x = XScale + XBetween;
            c2x = XScale + XBetween * 2;
            c3x = XScale + XBetween * 3;
            c4x = XScale + XBetween * 4;
            c5x = XScale + XBetween * 5;
            c6x = XScale + XBetween * 6;
            c7x = XScale + XBetween * 7;

            canvas.drawPoint(c1x, c1y, paint1);
            canvas.drawPoint(c2x, c2y, paint1);
            canvas.drawPoint(c3x, c3y, paint1);
            canvas.drawPoint(c4x, c4y, paint1);
            canvas.drawPoint(c5x, c5y, paint1);
            canvas.drawPoint(c6x, c6y, paint1);
            canvas.drawPoint(c7x, c7y, paint1);

            if (cx == 0) {
                cx = XScale + XBetween * 7;
            }
            if (cy == 0) {
                cy = c7y;
            }
            canvas.drawLine(cx, 0, cx, YPoint + 20, paint);

            paint1.setStyle(Paint.Style.FILL);

            int minR = (int) (MainActivity.SCREEN_WIDTH * 7 / 1080);
            int maxR = (int) (MainActivity.SCREEN_WIDTH * 11 / 1080);

            canvas.drawCircle(c1x, c1y, minR, paint1);
            canvas.drawCircle(c2x, c2y, minR, paint1);
            canvas.drawCircle(c3x, c3y, minR, paint1);
            canvas.drawCircle(c4x, c4y, minR, paint1);
            canvas.drawCircle(c5x, c5y, minR, paint1);
            canvas.drawCircle(c6x, c6y, minR, paint1);
            canvas.drawCircle(c7x, c7y, minR, paint1);
            canvas.drawCircle(cx, cy, maxR, paint1);

            canvas.drawLine(XScale + XBetween, c1y, XScale + XBetween * 2, c2y, paint1);
            canvas.drawLine(XScale + XBetween * 2, c2y, XScale + XBetween * 3, c3y, paint1);
            canvas.drawLine(XScale + XBetween * 3, c3y, XScale + XBetween * 4, c4y, paint1);
            canvas.drawLine(XScale + XBetween * 4, c4y, XScale + XBetween * 5, c5y, paint1);
            canvas.drawLine(XScale + XBetween * 5, c5y, XScale + XBetween * 6, c6y, paint1);
            canvas.drawLine(XScale + XBetween * 6, c6y, XScale + XBetween * 7, c7y, paint1);

            k1 = getK(XScale + XBetween, XScale + XBetween * 2, c1y, c2y);
            k2 = getK(XScale + XBetween * 2, XScale + XBetween * 3, c2y, c3y);
            k3 = getK(XScale + XBetween * 3, XScale + XBetween * 4, c3y, c4y);
            k4 = getK(XScale + XBetween * 4, XScale + XBetween * 5, c4y, c5y);
            k5 = getK(XScale + XBetween * 5, XScale + XBetween * 6, c5y, c6y);
            k6 = getK(XScale + XBetween * 6, XScale + XBetween * 7, c6y, c7y);

            b1 = getB(XScale + XBetween, c1y, k1);
            b2 = getB(XScale + XBetween * 2, c2y, k2);
            b3 = getB(XScale + XBetween * 3, c3y, k3);
            b4 = getB(XScale + XBetween * 4, c4y, k4);
            b5 = getB(XScale + XBetween * 5, c5y, k5);
            b6 = getB(XScale + XBetween * 6, c6y, k6);

            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setAntiAlias(true);// 去锯齿
            paint2.setColor(Color.WHITE);
// paint2.setStrokeWidth(30);
            paint2.setTextSize(textSize);

            Paint paint3 = new Paint();
            paint3.setStyle(Paint.Style.STROKE);
            paint3.setAntiAlias(true);// 去锯齿
            paint3.setColor(context.getResources().getColor(R.color.green));
// paint2.setStrokeWidth(30);
            paint3.setStyle(Paint.Style.FILL);
            paint3.setTextSize(30);

// canvas.drawRect(90, 70, 200, 110, paint3);
// canvas.drawText("4.4960", 100, 100, paint2);
// canvas.drawRect(cx - 55, cy - 60, cx + 55, cy - 20, paint3);
            canvas.drawRect(cx - textSize * 2, cy - (float) (textSize * 2.5), cx + textSize * 2, cy - textSize, paint3);

            if (value == null) {
                value = values[6];
            }

            if ("0".equals(value)) {
                value = "0.0000";
            }
// canvas.drawText(value, cx - 45, cy - 30, paint2);
            canvas.drawText(value, (float) (cx - (textSize * 1.5)), cy - (float) (textSize * 1.4), paint2);
        }

    }

    /**
     * 计算圆的y坐标
     *
     * @param x
     *            圆的x坐标
     * @param k
     *            y=kx+b
     * @param b
     *            y=kx+b
     * @return
     */
    private float getCY(float x, float k, float b) {
        return x * k + b;
    }

    /**
     * 计算直线的k值 y=kx+b
     *
     * @param x1
     *            第一个点的x坐标
     * @param x2
     *            第二个点的x坐标
     * @param y1
     *            第一个点的y坐标
     * @param y2
     *            第二个点的y坐标
     * @return
     */
    private float getK(float x1, float x2, float y1, float y2) {
        return (y2 - y1) / (x2 - x1);
    }

    /**
     * 计算直线的b值 y=kx+b
     *
     * @param x1
     *            任意一点的x坐标
     * @param y1
     *            任意一点的y坐标
     * @param k
     *            直线的k值
     * @return
     */
    private float getB(float x1, float y1, float k) {
        return y1 - k * x1;
    }

//点击滑动时间，处理点击按下和抬起的动作

    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getY() <= YPoint + 20 && event.getY() >= 0 && event.getX() <= cx + 30 && event.getX() >= cx - 30) {
                    isMove = true;
                    downX = event.getX();
                    downY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                if (isMove) {
                    if (mX <= c7x && mX > c6x) {// 默认圆在c7点，只能向左滑动
                        cx = mX;
                        cy = getCY(cx, k6, b6);
                    } else if (mX <= c6x && mX > c5x) {//
                        cx = mX;
                        cy = getCY(cx, k5, b5);
                    } else if (mX <= c5x && mX > c4x) {
                        cx = mX;
                        cy = getCY(cx, k4, b4);
                    } else if (mX < c4x && mX > c3x) {
                        cx = mX;
                        cy = getCY(cx, k3, b3);
                    } else if (mX < c3x && mX > c2x) {
                        cx = mX;
                        cy = getCY(cx, k2, b2);
                    } else if (mX <= c2x && mX > c1x) {
                        cx = mX;
                        cy = getCY(cx, k1, b1);
                    }
                    if (cx > c7x - ((c7x - c6x) / 2)) {// 回到c7x的位置
                        value = values[6];
                    } else if (cx <= c7x - ((c7x - c6x) / 2) && cx > c6x - ((c6x - c5x) / 2)) {
                        value = values[5];
                    } else if (cx <= c6x - ((c6x - c5x) / 2) && cx > c5x - ((c5x - c4x) / 2)) {
                        value = values[4];
                    } else if (cx <= c5x - ((c5x - c4x) / 2) && cx > c4x - ((c4x - c3x) / 2)) {
                        value = values[3];
                    } else if (cx <= c4x - ((c4x - c3x) / 2) && cx > c3x - ((c3x - c2x) / 2)) {
                        value = values[2];
                    } else if (cx <= c3x - ((c3x - c2x) / 2) && cx > c2x - ((c2x - c1x) / 2)) {
                        value = values[1];
                    } else if (cx <= c2x - ((c2x - c1x) / 2) && cx >= c1x) {
                        value = values[0];
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                if (cx > c7x - ((c7x - c6x) / 2)) {// 回到c7x的位置
                    cx = c7x;
                    cy = c7y;
                    value = values[6] + "";
                } else if (cx <= c7x - ((c7x - c6x) / 2) && cx > c6x - ((c6x - c5x) / 2)) {
                    cx = c6x;
                    cy = c6y;
                    value = values[5] + "";
                } else if (cx <= c6x - ((c6x - c5x) / 2) && cx > c5x - ((c5x - c4x) / 2)) {
                    cx = c5x;
                    cy = c5y;
                    value = values[4] + "";
                } else if (cx <= c5x - ((c5x - c4x) / 2) && cx > c4x - ((c4x - c3x) / 2)) {
                    cx = c4x;
                    cy = c4y;
                    value = values[3] + "";
                } else if (cx <= c4x - ((c4x - c3x) / 2) && cx > c3x - ((c3x - c2x) / 2)) {
                    cx = c3x;
                    cy = c3y;
                    value = values[2] + "";
                } else if (cx <= c3x - ((c3x - c2x) / 2) && cx > c2x - ((c2x - c1x) / 2)) {
                    cx = c2x;
                    cy = c2y;
                    value = values[1] + "";
                } else if (cx <= c2x - ((c2x - c1x) / 2) && cx >= c1x) {
                    cx = c1x;
                    cy = c1y;
                    value = values[0] + "";
                }
                invalidate();
                break;
        }
        return true;
    }

    private void setPositions() {
        XPoint = MainActivity.SCREEN_WIDTH * 157 / 720;
        YPoint = MainActivity.SCREEN_HEIGHT * 289 / 1230;
        XLength = MainActivity.SCREEN_WIDTH * 533 / 720;
        YLength = MainActivity.SCREEN_HEIGHT * 289 / 1230;
        YBetween = MainActivity.SCREEN_HEIGHT * 70 / 1230;
        XBetween = (float) (MainActivity.SCREEN_WIDTH * 66.625 / 720);
        YScale = MainActivity.SCREEN_WIDTH * 51 / 720;
        XScale = MainActivity.SCREEN_WIDTH * 157 / 720;
        XScaleY = MainActivity.SCREEN_HEIGHT * 325 / 1230;
    }

}


