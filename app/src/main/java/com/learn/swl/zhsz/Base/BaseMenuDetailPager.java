package com.learn.swl.zhsz.Base;

import android.app.Activity;
import android.view.View;

/**
 * Created by ADM on 2015/12/1.
 */
public abstract class BaseMenuDetailPager {
    protected Activity mActivity;
    protected View mRootView;

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        this.mRootView = initView();
    }

    public abstract View initView();
}
