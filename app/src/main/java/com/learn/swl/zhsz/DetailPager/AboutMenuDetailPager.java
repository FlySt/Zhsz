package com.learn.swl.zhsz.DetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.learn.swl.zhsz.Base.BaseMenuDetailPager;

/**
 * Created by ADM on 2015/12/1.
 */
public class AboutMenuDetailPager extends BaseMenuDetailPager {

    public AboutMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("关于");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        return textView;
    }
}
