package com.learn.swl.zhsz.Base;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.swl.zhsz.BmobFile.Data;
import com.learn.swl.zhsz.DetailPager.NewsMenuDetailPager;
import com.learn.swl.zhsz.Fragment.LeftFragment;
import com.learn.swl.zhsz.MainActivity;
import com.learn.swl.zhsz.domain.NewsData;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by ADM on 2015/11/27.
 */
public class NewsCenterPager extends BasePager {
    ArrayList<BaseMenuDetailPager> mPagers ;
    public NewsCenterPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {
        System.out.println("新闻中心初始化");
        tv_title.setText("新闻中心");
        setSlidingMenuEnable(true);
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0);
    }

    public void setCurrentMenuDetailPager(int position){
         BaseMenuDetailPager pager = mPagers.get(position);
         layout_base_content.removeAllViews();
         layout_base_content.addView(pager.mRootView);
    }
}
