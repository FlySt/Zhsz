package com.learn.swl.zhsz.Base;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.swl.zhsz.BmobFile.Data;
import com.learn.swl.zhsz.BmobFile.Pic;
import com.learn.swl.zhsz.DetailPager.InteractMenuDetailPager;
import com.learn.swl.zhsz.DetailPager.NewsMenuDetailPager;
import com.learn.swl.zhsz.DetailPager.PhotoMenuDetailPager;
import com.learn.swl.zhsz.DetailPager.TopicMenuDetailPager;
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
    BmobFile bmobFile;
    ArrayList<BaseMenuDetailPager> mPagers ;
    public NewsCenterPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {
        System.out.println("新闻中心初始化");
        tv_title.setText("新闻中心");
        //   TextView tv_content = new TextView(mActivity);
        //    tv_content.setText("首页");
        //   tv_content.setTextSize(25);
        //   tv_content.setTextColor(Color.RED);
        //   tv_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        //   layout_base_content.addView(tv_content);
        setSlidingMenuEnable(true);
        getGuidePic();

    }
    private void getGuidePic(){
        BmobQuery<Data> query = new BmobQuery<Data>();
        query.addQueryKeys("categories");
        System.out.println("开始查询");
        query.getObject(mActivity, "ufBJZZZe", new GetListener<Data>() {

            @Override
            public void onSuccess(Data object) {
                // TODO Auto-generated method stub
                //toast("查询成功：");
                //  Toast.makeText(GuideActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                System.out.println("查询成功:");
                bmobFile = object.getCategories();
                System.out.println("filename:" + bmobFile.getFilename());
                System.out.println("filenameurl:" + bmobFile.getFileUrl(mActivity));
                //获得数据的objectId信息
                //  object.getObjectId();
                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                //   object.getCreatedAt();
                getDataFromServer();
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                //   toast("查询失败："+arg0);
                Toast.makeText(mActivity, "查询失败：" + arg0, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void getDataFromServer(){
        HttpUtils utils = new HttpUtils();

        utils.send(HttpRequest.HttpMethod.GET, bmobFile.getFileUrl(mActivity), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("返回结果：" + result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("请求失败");
            }
        });
    }

    private void parseData(String result){
        Gson gson = new Gson();

        NewsData data = gson.fromJson(result,NewsData.class);
        System.out.println("解析结果："+data);
        MainActivity mainActivity = (MainActivity)mActivity;
        LeftFragment leftFragment = mainActivity.getLeftFragment();
        leftFragment.setNewsData(data);

        //准备四个菜单
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);
    }

    public void setCurrentMenuDetailPager(int position){
         BaseMenuDetailPager pager = mPagers.get(position);
         layout_base_content.removeAllViews();
         layout_base_content.addView(pager.mRootView);
    }
}
