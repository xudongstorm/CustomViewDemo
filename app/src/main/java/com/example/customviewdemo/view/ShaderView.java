package com.example.customviewdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customviewdemo.R;

public class ShaderView extends View {

    private Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private Shader mShader;

    public ShaderView(Context context) {
        this(context, null);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initBimmapShadeer(Canvas canvas){
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_test);
        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        canvas.drawCircle(500, 250, 250, mPaint);
    }

    private void initLinearGradient(Canvas canvas){
        mPaint.setShader(new LinearGradient(0, 0, 400, 400, Color.BLUE, Color.YELLOW, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 0, 400, 400, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int defaultSize = dp2px(300);

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize, heightSize);
        }else if(widthMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize, defaultSize);
        }else if(heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(defaultSize, heightSize);
        }else{
            setMeasuredDimension(defaultSize, defaultSize);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initLinearGradient(canvas);

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
