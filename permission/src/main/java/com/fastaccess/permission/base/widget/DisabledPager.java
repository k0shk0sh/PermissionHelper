package com.fastaccess.permission.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisabledPager extends ViewPager {

    public DisabledPager(Context context) {
        super(context);
    }

    public DisabledPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
