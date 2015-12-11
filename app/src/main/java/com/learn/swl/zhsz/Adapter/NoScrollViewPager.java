package com.learn.swl.zhsz.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ADM on 2015/11/27.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //false表示不拦截
 /*   @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
