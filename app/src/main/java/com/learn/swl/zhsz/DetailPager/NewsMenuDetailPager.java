package com.learn.swl.zhsz.DetailPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.swl.zhsz.Base.BaseMenuDetailPager;
import com.learn.swl.zhsz.Base.BasePager;
import com.learn.swl.zhsz.Base.GlobalContants;
import com.learn.swl.zhsz.Base.MyApplication;
import com.learn.swl.zhsz.BmobFile.Data;
import com.learn.swl.zhsz.BmobFile.Pic;
import com.learn.swl.zhsz.BmobFile.Web;
import com.learn.swl.zhsz.Fragment.LeftFragment;
import com.learn.swl.zhsz.MainActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.View.RefreshListView;
import com.learn.swl.zhsz.domain.NewsData;
import com.learn.swl.zhsz.domain.NewsQueryData;
import com.learn.swl.zhsz.domain.NewsWordsData;
import com.learn.swl.zhsz.domain.TabNewsData;
import com.learn.swl.zhsz.utils.WebActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by ADM on 2015/12/1.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {
    public static final int LOAD_NEWS_NUM = 8;//一次只加载8条新闻
    NewsWordsData newsWordsData;
    NewsQueryData newsQueryData;
    private ArrayList<NewsQueryData.TabNewsQueryData> tabNewsQueryData = new ArrayList<NewsQueryData.TabNewsQueryData>();
    BmobFile bmobFile;
    private RefreshListView lv_newslist;
    private TextView tv_title;
    private TextView tv_content;
    private ImageView iv_image;
    private ListAdapter mAdapter;
    private ViewPager vp_top;
    private TextView tv_top_title;
    private CirclePageIndicator mIndicator;
    private RelativeLayout layout_header;
    private int MAX_NEWS_NUM;//获取到的最大新闻条数
    private int CURRENT_LOAD_NUM = 0;//当前加载到的新闻条数
    private ArrayList<String> newsWordsList = new ArrayList<String>();
    private ArrayList<Bitmap> newsBitmap = new ArrayList<Bitmap>();
    public NewsMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.activity_news,null);
        View headerview = View.inflate(mActivity,R.layout.top_header_view,null);
        lv_newslist = (RefreshListView)view.findViewById(R.id.lv_newslist);
        lv_newslist.addHeaderView(headerview);
        vp_top = (ViewPager)view.findViewById(R.id.vp_top);
        tv_top_title = (TextView)view.findViewById(R.id.tv_top_title);
        mIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
        lv_newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=tabNewsQueryData.size()){
                    return;
                }
               Intent intent = new Intent(mActivity,WebActivity.class);
                System.out.println("position="+position);
               intent.putExtra("url",tabNewsQueryData.get(position-2).url);
                mActivity.startActivity(intent);
            }
        });
        lv_newslist.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tabNewsQueryData.clear();
                CURRENT_LOAD_NUM = 0;
                getNewsWords();//获取新闻时事热点
            }

            @Override
            public void onLoadMore() {
                if(CURRENT_LOAD_NUM < MAX_NEWS_NUM){
                    System.out.println("CURRENT_LOAD_NUM==="+CURRENT_LOAD_NUM);
                    parseData(null,true);
                }else{
                    Toast.makeText(mActivity,"最后一页了",Toast.LENGTH_SHORT).show();
                    lv_newslist.OnRefreshComplete(false);
                }
            }
        });
        vp_top.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
        //        TabNewsData.TopNewsData topNewsData = data.data.topnews.get(position);
       //         tv_top_title.setText(topNewsData.title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TextView textView = new TextView(mActivity);
       // textView.setText("新闻中心");
       // textView.setTextSize(25);
      //  textView.setTextColor(Color.RED);
      //  textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mAdapter = new ListAdapter();
        getNewsWords();//获取新闻时事热点
     //   getJsonData();
        return view;
    }

    public void getNewsWords() {
        String URL = GlobalContants.JUHE_NEWS_WORDS_URL+"dtype=&key="+GlobalContants.JUHE_NEWS_APPKEY;
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("查询成功:" + result);
                // Log.i(TAG, result);
                parseData(result, false);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

 /*   class TopNewsAdapter extends PagerAdapter{
        BitmapUtils utils;
        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return data.data.topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setImageResource(R.mipmap.image);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            TabNewsData.TopNewsData topNewsData = data.data.topnews.get(position);
            utils.display(image,topNewsData.topimage);
            container.addView(image);
            // pager.initData();
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }*/
   class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tabNewsQueryData.size();
        }

        @Override
        public NewsQueryData.TabNewsQueryData getItem(int position) {
            return tabNewsQueryData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.news_list_item,null);
            iv_image = (ImageView)view.findViewById(R.id.image);
            tv_title = (TextView)view.findViewById(R.id.tv_title);
            tv_content = (TextView)view.findViewById(R.id.tv_content);
            ImageLoader.getInstance().displayImage(
                    tabNewsQueryData.get(position).img,
                    iv_image,
                    MyApplication.getInstance().getOptions(
                            R.mipmap.user_icon),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);
                        }

                    });
       //     iv_image.setImageBitmap(tabNewsQueryData.get(position).img);
            tv_title.setText(Html.fromHtml(tabNewsQueryData.get(position).title));
            tv_content.setText(Html.fromHtml(tabNewsQueryData.get(position).content));
            return view;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_newslist.OnRefreshComplete(true);
            CURRENT_LOAD_NUM = CURRENT_LOAD_NUM+LOAD_NEWS_NUM;//改变已经加载的条数
            System.out.println("当前加载的条数：" + CURRENT_LOAD_NUM);
            switch (msg.what){
                case 0:
                    refreshList(false);
                    break;
                case 1:
                    refreshList(true);
                    break;
            }

        }
    };
    private void parseData(String result,final boolean isLoadMore){
        if(!isLoadMore){
            Gson gson = new Gson();

            newsWordsData = gson.fromJson(result,NewsWordsData.class);
            System.out.println("解析结果："+newsWordsData);
            newsWordsList = newsWordsData.result;
            MAX_NEWS_NUM = newsWordsList.size();
            System.out.println("最大新闻条数："+MAX_NEWS_NUM);
        }
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i=CURRENT_LOAD_NUM;i<CURRENT_LOAD_NUM+LOAD_NEWS_NUM;i++){
                    queryNews(newsWordsList.get(i));//检索新闻\
                    if(i == MAX_NEWS_NUM-1){//加载到最后一条时跳出
                        break;
                    }
                }
                Message msg = handler.obtainMessage();
                if(isLoadMore){
                    msg.what = 1;
                }else {
                    msg.what = 0;
                }
                handler.sendMessage(msg);
            }
        };
        thread.start();
       // System.out.println("解析结果："+data.data.topnews.size());
      //  tv_top_title.setText(data.data.topnews.get(0).title);
     //   getImageFromUrl();
        //准备四个菜单
    }
    private void parseQueryData(String result){
        Gson gson = new Gson();

        newsQueryData = gson.fromJson(result,NewsQueryData.class);
        System.out.println("解析结果："+newsQueryData);
        ArrayList<NewsQueryData.TabNewsQueryData> datalist = newsQueryData.result;
        for(NewsQueryData.TabNewsQueryData data:datalist){
            if(!data.img.isEmpty()){
                System.out.println("解析结果：" + data);
                tabNewsQueryData.add(data);
                break;
            }
        }
    }
    private void queryNews(String q) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        params.put("q",q);//需要检索的关键字,请UTF8 URLENCODE
        params.put("key", GlobalContants.JUHE_NEWS_APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json
     //   String URL = GlobalContants.JUHE_NEWS_QUERY_URL+"q="+q+"&dtype=&key="+GlobalContants.JUHE_NEWS_APPKEY;
       try {
            result =net(GlobalContants.JUHE_NEWS_QUERY_URL, params, "GET");
            System.out.println("查询成功:" + result);
            parseQueryData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("查询成功:" + result);
                // Log.i(TAG, result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });*/

    }
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    @SuppressLint("NewApi")
    public static String net(String strUrl, Map<String, Object> params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
    //将map型转为请求参数型
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    /*  private void getImageFromUrl(){

          for(int i=0;i<newsWordsList.size();i++){
              // image.setImageResource(imagesId[i]);
              //image.setImageURI(bmobFile[i].getFileUrl(GuideActivity.this));
              //  bmobFile[i].loadImage(this,image);
              System.out.println("bmobFile[i].getFilename()："+getFilename(newsWordsList.get(i).listimage));
              System.out.println("地址："+mActivity.getCacheDir());
              final File file = new File(mActivity.getCacheDir(), getFilename(newsWordsList.get(i).listimage));
              if(file.exists()){
                  System.out.println("从缓存读取图片");
                  Bitmap bm= BitmapFactory.decodeFile(file.getAbsolutePath());
                  newsBitmap.add(bm);
              }else{
                  HttpUtils utils = new HttpUtils();
                  utils.download(newsWordsList.get(i).listimage, file.getAbsolutePath(), new RequestCallBack<File>() {
                      @Override
                      public void onSuccess(ResponseInfo<File> responseInfo) {
                          File result = (File)responseInfo.result;
                          System.out.println("图片路径："+result.getAbsolutePath());
                          Bitmap bm = BitmapFactory.decodeFile(result.getAbsolutePath());
                          newsBitmap.add(bm);
                      }

                      @Override
                      public void onFailure(HttpException error, String msg) {
                          System.out.println("下载图片失败："+msg);
                      }
                  });
              }
          }
          refreshList();
      }*/
    private void refreshList(boolean isLoadMore){
        if(!isLoadMore){
            lv_newslist.setAdapter(mAdapter);
        }
     //   vp_top.setAdapter(new TopNewsAdapter());
     // mIndicator.setViewPager(vp_top);
      //  mIndicator.setSnap(true);
        lv_newslist.requestLayout();
        lv_newslist.setVisibility(View.GONE);
       mAdapter.notifyDataSetChanged();
        lv_newslist.setVisibility(View.VISIBLE);
    }
    private String getFilename(String path){
        int index = path.lastIndexOf("/");
        return path.substring(index);
    }
    private void getWebURL(){
        BmobQuery<Web> query = new BmobQuery<Web>();
        query.addQueryKeys("web_1");
        query.getObject(mActivity, "6jtp2223", new GetListener<Web>() {

            @Override
            public void onSuccess(Web object) {
                // TODO Auto-generated method stub
                //toast("查询成功：");
                Toast.makeText(mActivity, "查询成功", Toast.LENGTH_SHORT).show();
                System.out.println("查询成功:");
                bmobFile = object.getWeb_1();
                System.out.println("filename:" + bmobFile.getFilename());
                System.out.println("filenameurl:" + bmobFile.getFileUrl(mActivity));
                //获得数据的objectId信息
                //  object.getObjectId();
                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                //   object.getCreatedAt();
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url",bmobFile.getFileUrl(mActivity));
                mActivity.startActivity(intent);
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                //   toast("查询失败："+arg0);
                Toast.makeText(mActivity, "查询失败：" + arg0, Toast.LENGTH_SHORT).show();
            }

        });
    }
}
