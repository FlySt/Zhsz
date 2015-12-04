package com.learn.swl.zhsz.DetailPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learn.swl.zhsz.Base.BaseMenuDetailPager;
import com.learn.swl.zhsz.LoginActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.SettingActivity;

/**
 * Created by ADM on 2015/12/1.
 */
public class SettingMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{
    private RelativeLayout layout_user_icon;
    public SettingMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.activity_setting,null);
        layout_user_icon = (RelativeLayout)view.findViewById(R.id.layout_user_icon);
        layout_user_icon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_user_icon:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }
}
