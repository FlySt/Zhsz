package com.learn.swl.zhsz.Base;

import android.app.Activity;


import com.learn.swl.zhsz.DetailPager.AboutMenuDetailPager;
import com.learn.swl.zhsz.DetailPager.HomeMenuDetailPager;

import java.util.ArrayList;

/**
 * Created by ADM on 2015/11/27.
 */
public class HomePager extends BasePager {
    ArrayList<BaseMenuDetailPager> mPagers ;
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("段子");
        //准备四个菜单
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new HomeMenuDetailPager(mActivity));
        //layout_base_content.addView(tv_content);
        setCurrentMenuDetailPager(0);
        setSlidingMenuEnable(true);
    }
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager = mPagers.get(position);
        layout_base_content.removeAllViews();
        layout_base_content.addView(pager.mRootView);
    }

}
