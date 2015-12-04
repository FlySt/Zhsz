package com.learn.swl.zhsz.Base;

import android.app.Activity;


import com.learn.swl.zhsz.DetailPager.AboutMenuDetailPager;
import com.learn.swl.zhsz.DetailPager.HomeMenuDetailPager;

import com.learn.swl.zhsz.DetailPager.IntroMenuDetailPager;

import com.learn.swl.zhsz.DetailPager.SettingMenuDetailPager;

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
        //准备四个菜单
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new HomeMenuDetailPager(mActivity));
        mPagers.add(new SettingMenuDetailPager(mActivity));
        mPagers.add(new IntroMenuDetailPager(mActivity));
        mPagers.add(new AboutMenuDetailPager(mActivity));
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
