package com.learn.swl.zhsz.DetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learn.swl.zhsz.Base.BaseMenuDetailPager;
import com.learn.swl.zhsz.Base.GlobalContants;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.domain.JokesData;
import com.learn.swl.zhsz.domain.TabNewsData;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by ADM on 2015/12/1.
 */
public class HomeMenuDetailPager extends BaseMenuDetailPager{
    private String TAG = "HomeMenuDetailPager";
    private ListView lv_jokes;
    private JokesData jokesData;
    private TextView tv_joke_content;
    private ImageButton ib_share;
    private JokeAdapter mAdapter;
    ArrayList<JokesData.TabJokesDataList>  tabJokesDataLists;
    public HomeMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.acticity_jokes,null);
        lv_jokes = (ListView)view.findViewById(R.id.lv_jokes);
        initData();
        return view;
    }
    private void initData(){
        getDataFromApi(1);
    }

    public void getDataFromApi(int page) {
        String URL = GlobalContants.JUHE_JOKE_URL+"key="+GlobalContants.JUHE_JOKE_APPKEY+"&page"+page+"&pagesize=20";
       // Log.i(TAG,URL);
        System.out.println("URL：" + URL);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
               // Log.i(TAG, result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
    private void parseData(String result){
        Gson gson = new Gson();

        jokesData = gson.fromJson(result,JokesData.class);
        System.out.println("解析结果："+jokesData);
        tabJokesDataLists = jokesData.result.data;
        System.out.println("解析结果：" + tabJokesDataLists);
        //准备四个菜单
        refreshList();
    }

    private void refreshList() {
        mAdapter = new JokeAdapter();
        lv_jokes.setAdapter(mAdapter);
    }



    class JokeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tabJokesDataLists.size();
        }

        @Override
        public JokesData.TabJokesDataList getItem(int position) {
            return tabJokesDataLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final String content;
            View view = View.inflate(mActivity,R.layout.jokes_item,null);
            tv_joke_content = (TextView)view.findViewById(R.id.tv_joke_content);
            tv_joke_content.setText(getItem(position).content);
            content = getItem(position).content.toString();
            ib_share = (ImageButton)view.findViewById(R.id.ib_share);
            ib_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("解析结果："+content);
                    showShare(content);
                }
            });
            return view;
        }
    }
    private void showShare(String content) {
        System.out.println("分享");

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("段子"+content);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("http://sharesdk.cn");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(mActivity);
    }
}
