package com.superrecyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.superrecyclerview.utils.CommonUtils;

/**
 * 快速导航条
 */
public class QuickIndexBar extends View {

    private String[] indexArr = {"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private Paint paint;
    private int width;
    private float cellHeight;
    private Context context;
    private int fontsize;
    private int yellow = 0xffA18206;
    private int black = 0xff7B7B7B;
    private int mid_transparent = 0x33000000;
    private int transparent = 0x00000000;

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public QuickIndexBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        fontsize = CommonUtils.sp2px(context, 15);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        paint.setColor(black);
        paint.setTextSize(fontsize);
        paint.setTextAlign(Paint.Align.CENTER);//设置文本的起点是文字边框底边的中心
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        //得到一个格子的高度
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            float x = width / 2;
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + i * cellHeight;
            if (lastIndex == i) {
                paint.setColor(yellow);
                canvas.drawCircle(x, y - getTextHeight(indexArr[i]) / 2, fontsize / 2 + 4, paint);
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(black);
            }
            canvas.drawText(indexArr[i], x, y, paint);
        }
    }

    private int lastIndex = -1;//记录上次的触摸字母的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mid_transparent);
                float y1 = event.getY();
                int index1 = (int) (y1 / cellHeight);//得到字母对应的索引
                if (lastIndex != index1) {
                    //说明当前触摸字母和上一个不是同一个字母
                    //对index做安全性的检查
                    if (index1 >= 0 && index1 < indexArr.length) {
                        if (mChangeListener != null) {
                            mChangeListener.onLetterChange(indexArr[index1]);
                        }
                    }
                }
                lastIndex = index1;
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / cellHeight);//得到字母对应的索引
                if (lastIndex != index) {
                    //说明当前触摸字母和上一个不是同一个字母
                    //对index做安全性的检查
                    if (index >= 0 && index < indexArr.length) {
                        if (mChangeListener != null) {
                            mChangeListener.onLetterChange(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(transparent);
                //重置lastIndex
                lastIndex = -1;
                if (mChangeListener != null) {
                    mChangeListener.onLetterCancel();
                }
                break;
        }
        //引起重绘
        invalidate();
        return true;
    }

    /**
     * 获取文本的高度
     *
     * @param text
     * @return
     */
    private int getTextHeight(String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    private OnLetterChangeListener mChangeListener;

    public interface OnLetterChangeListener {
        void onLetterChange(String letter);

        void onLetterCancel();
    }

    public void setOnTouchLetterListener(OnLetterChangeListener listener) {
        this.mChangeListener = listener;
    }
}