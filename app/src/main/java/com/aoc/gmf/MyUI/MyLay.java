package com.aoc.gmf.MyUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by giser on 2015/8/3.
 * 跳转下一页的编辑框
 */
public class MyLay extends LinearLayout {

    public MyLay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  true;
    }
}
