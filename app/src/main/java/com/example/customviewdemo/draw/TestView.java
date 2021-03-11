package com.example.customviewdemo.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint generatePaint(int color, Paint.Style style, int width){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*Paint greenPaint = generatePaint(Color.GREEN, Paint.Style.STROKE, 3);
        Paint redPaint = generatePaint(Color.RED, Paint.Style.STROKE, 3);

        Rect rect = new Rect(0, 0, 400, 200);
        canvas.drawRect(rect, greenPaint);

        canvas.translate(100, 100);
        canvas.drawRect(rect, redPaint);*/

        /*setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawColor(Color.RED);
        Canvas.clipRect*/
    }
}
