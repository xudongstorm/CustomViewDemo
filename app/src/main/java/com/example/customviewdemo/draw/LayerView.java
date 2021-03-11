package com.example.customviewdemo.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class LayerView extends View {

    private Paint mPaint = new Paint();
    private static final int LAYER_FLAGS = Canvas.ALL_SAVE_FLAG;

    public LayerView(Context context) {
        super(context);
    }

    public LayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(150, 150, 100, mPaint);

        canvas.saveLayerAlpha(0, 0, 400, 400, 0, LAYER_FLAGS);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(200, 200, 100, mPaint);
    //    canvas.restore();
    }
}
