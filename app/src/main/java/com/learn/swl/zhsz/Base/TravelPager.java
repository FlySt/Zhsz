package com.learn.swl.zhsz.Base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by ADM on 2015/11/27.
 */
public class TravelPager extends BasePager {

    public TravelPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {
        tv_title.setText("旅游");
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("旅游");
        tv_content.setTextSize(25);
        tv_content.setTextColor(Color.RED);
        tv_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        layout_base_content.addView(tv_content);
        setSlidingMenuEnable(true);
    }
}
