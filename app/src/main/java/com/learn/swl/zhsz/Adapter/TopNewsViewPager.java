package com.learn.swl.zhsz.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.learn.swl.zhsz.MainActivity;

/**
 * Created by ADM on 2015/11/27.
 */
public class TopNewsViewPager extends ViewPager {
    int startX;
    int startY;
    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**右划而且是第一个页面，父控件拦截
    * 左划，最后一个页面，父空间拦截
    * 上下滑，父控件拦截
    * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,
                // 这样是为了保证ACTION_MOVE调用
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
                    if (endX > startX) {// 右划
                        if (getCurrentItem() == 0) {// 第一个页面, 需要父控件拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {// 左划
                        if (getCurrentItem() == getAdapter().getCount() - 1) {// 最后一个页面,
                            // 需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {// 上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
