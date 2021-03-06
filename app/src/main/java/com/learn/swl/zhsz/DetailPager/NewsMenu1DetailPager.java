package com.learn.swl.zhsz.DetailPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.swl.zhsz.Base.BaseMenuDetailPager;
import com.learn.swl.zhsz.BmobFile.Data;
import com.learn.swl.zhsz.BmobFile.Web;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.View.RefreshListView;
import com.learn.swl.zhsz.domain.TabNewsData;
import com.learn.swl.zhsz.utils.WebActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by ADM on 2015/12/1.
 */
public class NewsMenu1DetailPager extends BaseMenuDetailPager {
    TabNewsData data;
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
    private ArrayList<TabNewsData.TabListNewsData> newsDataArrayList = new ArrayList<TabNewsData.TabListNewsData>();
    private ArrayList<Bitmap> newsBitmap = new ArrayList<Bitmap>();
    public NewsMenu1DetailPager(Activity mActivity) {
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
                Intent intent = new Intent(mActivity,WebActivity.class);
                intent.putExtra("url",newsDataArrayList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        vp_top.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabNewsData.TopNewsData topNewsData = data.data.topnews.get(position);
                tv_top_title.setText(topNewsData.title);
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

        getJsonData();
        return view;
    }
    class TopNewsAdapter extends PagerAdapter{
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
    }
    class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsDataArrayList.size();
        }

        @Override
        public TabNewsData.TabListNewsData getItem(int position) {
            return newsDataArrayList.get(position);
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
            if(position<newsBitmap.size()){
                iv_image.setImageBitmap(newsBitmap.get(position));
            }
            tv_title.setText(newsDataArrayList.get(position).title);
            tv_content.setText(newsDataArrayList.get(position).type);
            return view;
        }
    }
    private void getJsonData(){
        BmobQuery<Data> query = new BmobQuery<Data>();
        query.addQueryKeys("news");
        query.getObject(mActivity, "ufBJZZZe", new GetListener<Data>() {

            @Override
            public void onSuccess(Data object) {
                // TODO Auto-generated method stub
                //toast("查询成功：");
                Toast.makeText(mActivity, "查询成功", Toast.LENGTH_SHORT).show();
                System.out.println("查询成功:");
                bmobFile = object.getNews();
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

        data = gson.fromJson(result,TabNewsData.class);
        System.out.println("解析结果："+data);
        newsDataArrayList = data.data.news;
        System.out.println("解析结果："+data.data.topnews.size());
        tv_top_title.setText(data.data.topnews.get(0).title);
        getImageFromUrl();
        //准备四个菜单
    }
    private void getImageFromUrl(){

        for(int i=0;i<newsDataArrayList.size();i++){
            // image.setImageResource(imagesId[i]);
            //image.setImageURI(bmobFile[i].getFileUrl(GuideActivity.this));
            //  bmobFile[i].loadImage(this,image);
            System.out.println("bmobFile[i].getFilename()："+getFilename(newsDataArrayList.get(i).listimage));
            System.out.println("地址："+mActivity.getCacheDir());
            final File file = new File(mActivity.getCacheDir(), getFilename(newsDataArrayList.get(i).listimage));
            if(file.exists()){
                System.out.println("从缓存读取图片");
                Bitmap bm= BitmapFactory.decodeFile(file.getAbsolutePath());
                newsBitmap.add(bm);
            }else{
                HttpUtils utils = new HttpUtils();
                utils.download(newsDataArrayList.get(i).listimage, file.getAbsolutePath(), new RequestCallBack<File>() {
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
    }
    private void refreshList(){
        lv_newslist.setAdapter(mAdapter);
        vp_top.setAdapter(new TopNewsAdapter());
        mIndicator.setViewPager(vp_top);
        mIndicator.setSnap(true);
        mAdapter.notifyDataSetChanged();
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
