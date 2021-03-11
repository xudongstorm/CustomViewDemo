package com.example.customviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

public class ClockView extends View {

    private Paint mCirclePaint = new Paint();
    private Paint mDegreePaint = new Paint();
    private Paint mHourPaint = new Paint();
    private Paint mMinutePaint = new Paint();
    private int mScreenWidth;
    private int mScreenHeight;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mScreenWidth/2, mScreenHeight/2, mScreenWidth/2, mCirclePaint);   //画外圆

        for(int i=0; i<60; i++){    //画刻度
            if(i % 5 == 0){
                mDegreePaint.setStrokeWidth(5);
                canvas.drawLine(mScreenWidth/2, mScreenHeight/2-mScreenWidth/2, mScreenWidth/2, mScreenHeight/2-mScreenWidth/2+30, mDegreePaint);
                String degree = String.valueOf(i/5);
                canvas.drawText(degree, mScreenWidth/2-mDegreePaint.measureText(degree)/2, mScreenHeight/2-mScreenWidth/2+60, mDegreePaint);
            }else{
                mDegreePaint.setStrokeWidth(3);
                canvas.drawLine(mScreenWidth/2, mScreenHeight/2-mScreenWidth/2, mScreenWidth/2, mScreenHeight/2-mScreenWidth/2+15, mDegreePaint);
            }
            canvas.rotate(6, mScreenWidth/2, mScreenHeight/2);
        }

        canvas.save();
        canvas.translate(mScreenWidth/2, mScreenHeight/2);
        canvas.drawLine(0 , 0, 100, 100, mHourPaint);
        canvas.drawLine(0 , 0, 100, 200, mMinutePaint);
        canvas.restore();
    }

    private void init(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mDegreePaint.setAntiAlias(true);
        mDegreePaint.setTextSize(30);

        mHourPaint.setAntiAlias(true);
        mHourPaint.setStrokeWidth(20);

        mMinutePaint.setAntiAlias(true);
        mMinutePaint.setStrokeWidth(10);
    }
}
