package com.example.customviewdemo.readpoint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chinamobile.app.utils.AndroidUtil;
import com.cmic.module_base.R;
import com.rcsbusiness.common.utils.LogF;
import com.rcsbusiness.common.utils.SystemUtil;

public class RoundNumber extends android.support.v7.widget.AppCompatImageView {

    private static String TAG = "RoundNumber";

    private final float mTextMargin;

    private float mRoundRadius; // 圆形半径
    private int mRoundColor;
    private float mCircleX; // 圆心x坐标
    private float mCircleY; // 圆心y坐标
    private Rect mTextRect;
//    手指触摸的坐标
    private float mDownMotionX;
    private float mDownMotionY;

    private Paint mCirclePaint; // 圆形画笔
    private TextPaint mTextPaint; // 文字画笔
    private float mTextSize; // 字体大小，单位SP
    private Paint.FontMetrics mTextFontMetrics; // 字体
    private float mTextMove; // 为了让文字居中，需要移动的距离

    private String mTextString;
    private Context mContext;

    private RectF mRoundRect = new RectF();

    private DragBubbleView mDragBubble;

    private DragStateListener mListener;

    private boolean isCanDrag = true; //控制是否可以拖拽
//    是否是消息Tab
    private boolean isMessageTab;
//    是否正在移动
    private boolean isOnMove;

    public interface DragStateListener {
        void onDismiss(RoundNumber roundNumber); // 手指抬起
    }

    public RoundNumber(Context context) {
        this(context, null);
    }

    public RoundNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mTextMargin = AndroidUtil.dip2px(mContext, 3);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundNumber, defStyleAttr, 0);
        mRoundRadius = ta.getDimension(R.styleable.RoundNumber_roundRadius, AndroidUtil.dip2px(context, 12));//12
        mRoundColor = ta.getColor(R.styleable.RoundNumber_roundColor, getResources().getColor(R.color.transparent));
        mTextSize = ta.getDimension(R.styleable.RoundNumber_roundTextSize, AndroidUtil.dip2px(context, 11));
        mTextString = ta.getString(R.styleable.RoundNumber_roundText);
        if(TextUtils.isEmpty(mTextString)){
            mTextString = "99";
        }
        ta.recycle();

        initPaint();
    }

    private void initPaint() {
        mTextRect = new Rect();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mRoundColor);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mCirclePaint.setMaskFilter(new BlurMaskFilter(10f, Blur.SOLID));
//        mCirclePaint.setShadowLayer(0.5f ,0,5f,mRoundColor);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.SANS_SERIF);//设置roboto regular字体
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextFontMetrics = mTextPaint.getFontMetrics();
        mTextMove = -mTextFontMetrics.ascent - (-mTextFontMetrics.ascent + mTextFontMetrics.descent) / 2; // drawText从baseline开始，baseline的值为0，baseline的上面为负值，baseline的下面为正值，即这里ascent为负值，descent为正值,比如ascent为-20，descent为5，那需要移动的距离就是20 - （20 + 5）/ 2
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    private int measuredDimension(int measureSpec) {
        int result;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size;
        } else {
            mTextPaint.getTextBounds(mTextString, 0, mTextString.length(), mTextRect);

            if (mTextString.length() > 2) {
                result = (int) (mTextRect.width() + mTextMargin * 2);
            } else {
                result = (int) (2 * mRoundRadius);
            }
            if (mode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCircleX = w / 2f;
        mCircleY = h / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mTextPaint.getTextBounds(mTextString, 0, mTextString.length(), mTextRect);

//        if (mTextString.length() > 2) {   // 文本为"99+"的时候，显示圆角矩阵
//
//            mRoundRect.left = 0;
//            mRoundRect.top = mCircleY - mRoundRadius;
//            mRoundRect.right = getWidth();
//            mRoundRect.bottom = mCircleY + mRoundRadius;
//
//            canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mCirclePaint);//第二个参数是x半径，第三个参数是y半径
//        } else {
//            canvas.drawCircle(mCircleX, mCircleY, mRoundRadius, mCirclePaint);
//        }
        if (mTextString.length() > 2) {
            canvas.drawText(mTextString, mCircleX, mCircleY + mTextMove, mTextPaint);
        }else {                                     //在领导的机子上，当气泡中的数字大于二十，尾数为 1 时，要往右偏一下，
            if (Integer.parseInt(mTextString) > 19){

                if (Integer.parseInt(mTextString) % 10 == 1){
                    canvas.drawText(mTextString, mCircleX+1, mCircleY + mTextMove, mTextPaint);
                }else {
                    canvas.drawText(mTextString, mCircleX, mCircleY + mTextMove, mTextPaint);
                }
            } else{ //在领导的机子上，当气泡中的数字为个位数时，要忘左偏一点
                canvas.drawText(mTextString, mCircleX-1, mCircleY + mTextMove, mTextPaint);
            }

        }

//        canvas.drawText(mTextString, mCircleX, mCircleY + mTextMove, mTextPaint);//这里是最原本的设置，如果有问题把上面的设置去掉，解封这句即可。在其他普通机子上这设置是没问题的，除了领导的！

//        canvas.drawText(mTextString, mCircleX - mTextRect.width() / 2f - mTextRect.left,
//                mCircleY + mTextRect.height() / 2f, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isMessageTab){
            return false;
        }
        if(!isCanDrag){
            if(mDragBubble != null && mDragBubble.isTouchable()){//拖动的时候，不能直接返回false
                return false;
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDragBubble != null) {
                    if (mDragBubble.isTouchable()) {
                        setVisibility(View.INVISIBLE);
                        getParent().requestDisallowInterceptTouchEvent(true); // 不允许父控件处理TouchEvent，当父控件为ListView这种本身可滑动的控件时必须要控制

                        mDragBubble.downAction(this);
                        return true;
                    }
                }

                return false;

            case MotionEvent.ACTION_MOVE:
//                if (mClickListener != null) { // 注意这里要用getRaw来获取手指当前所处的相对整个屏幕的坐标
//                    mClickListener.onMove(event.getRawX(), event.getRawY() - Util.getTopBarHeight((Activity)  mContext));
//                }

                if (mDragBubble != null) {
                    mDragBubble.moveAction(event.getRawX(), event.getRawY() - getStatusBarHeight());
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                if (mClickListener != null) {
//                    getParent().requestDisallowInterceptTouchEvent(false); // 将控制权还给父控件
//                    mClickListener.onUp();
//                }
                if (mDragBubble != null) {
                    mDragBubble.upAction();
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 消息Tab上的红点被点击
     * @param event  事件
     */
    public void onMessageTabRoundNumberTouch(MotionEvent event){
        if(isMessageTab){

            if(!isCanDrag){
                if(mDragBubble != null && mDragBubble.isTouchable()){//拖动的时候，不能直接返回false
                    return;
                }
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int[] location = new int[2];
                    this.getLocationOnScreen(location);
                    int width = getMeasuredWidth();
                    int height = getMeasuredHeight();
//                    判断触摸区域是否在红点数字范围
                    LogF.e(TAG, "红点测试点击：x: " + event.getRawX() + " y: " + event.getRawY());
                    LogF.e(TAG, "红点测试：红点控件的right x: " + (location[0] + width) + " 红点控件的bottom y: " + (location[1] + height));
                    if(event.getRawX() < location[0] ||
                            event.getRawX() > location[0] + width ||
                            event.getRawY() < location[1] ||
                            event.getRawY() > location[1] + height){
                        return;
                    }
//                    if (event.getX() < getLeft() || event.getX() > getRight() ||
//                            event.getY() < getTop() || event.getY() > getBottom()){
//                        return;
//                    }
                    mDownMotionX = event.getRawX();
                    mDownMotionY = event.getRawY();
                    if (mDragBubble != null) {
                        if (mDragBubble.isTouchable()) {
                            setVisibility(View.INVISIBLE);
                            mDragBubble.downAction(this);
                            mDragBubble.setIsTouchable(true);
                        }
                    }
                    isOnMove = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mDragBubble != null &&
                            Math.abs(event.getRawX() - mDownMotionX) > 4 && Math.abs(event.getRawY() - mDownMotionY) > 4) {
                        LogF.e(TAG, "红点测试滑动【屏幕】：x: " + event.getRawX() + " y: " + event.getRawY());
                        mDragBubble.moveAction(event.getRawX(), event.getRawY() - getStatusBarHeight());
                        mDragBubble.setIsTouchable(false);
                    }
                    isOnMove = true;
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mDragBubble != null) {
                        mDragBubble.upAction();
                        mDragBubble.setIsTouchable(true);
                    }
                    isOnMove = false;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (mDragBubble != null) {
            if (mDragBubble.isTouchable()) {
                super.setVisibility(visibility);
            }
        } else {
            super.setVisibility(visibility);
        }
    }

    /**
     * 设置显示内容
     *
     * @param text
     */
    public void setText(DragBubbleView dragBubble, String text) {
        this.mDragBubble = dragBubble;
        String tempText = mTextString;
        mTextString = text;
        if (text != null) {
            if (!text.equals(tempText)) {
                requestLayout();
                invalidate();
            } else {
                invalidate();
            }
        }
    }

    public void dismissAction() {
        if (mListener != null) {
            mListener.onDismiss(this);
        }
    }

    public void setDragListener(DragStateListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取显示内容
     *
     * @return
     */
    public String getText() {
        return mTextString;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = SystemUtil.sp2px(textSize);
        mTextPaint.setTextSize(mTextSize);

        mTextFontMetrics = mTextPaint.getFontMetrics();
        mTextMove = -mTextFontMetrics.ascent - (-mTextFontMetrics.ascent + mTextFontMetrics.descent) / 2;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isCanDrag() {
        return isCanDrag;
    }

    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
    }

    public boolean isMessageTab() {
        return isMessageTab;
    }

    public void setMessageTab(boolean messageTab) {
        isMessageTab = messageTab;
    }

    public boolean isOnMove() {
        return isOnMove;
    }
}
