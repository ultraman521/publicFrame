package com.news.guagua.utils;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.style.ReplacementSpan;

public class LinearGradientFontSpan extends ReplacementSpan {

    private int mSize;
    private int startColor;
    private int endColor;


    public LinearGradientFontSpan(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;

    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end));

        //这段不可以去掉，字体高度没设置，可能出现draw（）方法没有被调用，如果你调用SpannableStringBuilder后append一个字符串，效果也是会出来，下面这段就不需要了
        // 原因详见https://stackoverflow.com/questions/20069537/replacementspans-draw-method-isnt-called
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        if (fm != null) {
            fm.top = metrics.top;
            fm.ascent = metrics.ascent;
            fm.descent = metrics.descent;
            fm.bottom = metrics.bottom;
        }

        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        LinearGradient lg = new LinearGradient(0, 0, 0, paint.descent() - paint.ascent(),
                startColor,
                endColor,
                Shader.TileMode.REPEAT); //从上到下渐变
        paint.setShader(lg);

        canvas.drawText(text, start, end, x, y, paint);//绘制文字
    }
}
