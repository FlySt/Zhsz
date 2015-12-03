package com.learn.swl.zhsz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.learn.swl.zhsz.BmobFile.Pic;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.utils.PerfUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;


public class GuideActivity extends Activity {
    private static final int imagesId[] = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private ViewPager vpGuide;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout layout_point;
    private View pointRed;
    private int pointWidth;
    private Button btn_start;
    private BmobFile[] bmobFile = new BmobFile[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        getGuidePic();
        pointRed = findViewById(R.id.red);
        btn_start = (Button)findViewById(R.id.btn_start);
        vpGuide = (ViewPager)findViewById(R.id.vp_guide);


        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("当前位置：" + position + ":百分比:" + positionOffset + ":偏移像素:" + positionOffsetPixels);
                int len = (int) (pointWidth * positionOffset + pointWidth * position);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pointRed.getLayoutParams();
                params.leftMargin = len;
                System.out.println("len:" + len);
                pointRed.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == imagesId.length - 1) {
                    btn_start.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                PerfUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }
    private void getGuidePic(){
        BmobQuery<Pic> query = new BmobQuery<Pic>();
        query.addQueryKeys("guide_1,guide_2,guide_3");
        System.out.println("开始查询");
        query.getObject(this, "bHyqMMMU", new GetListener<Pic>() {

            @Override
            public void onSuccess(Pic object) {
                // TODO Auto-generated method stub
                //toast("查询成功：");
              //  Toast.makeText(GuideActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                System.out.println("查询成功:");
                bmobFile[0] = object.getGuide_1();
                bmobFile[1] = object.getGuide_2();
                bmobFile[2] = object.getGuide_3();
                System.out.println("filename:" + bmobFile[1].getFilename());
                System.out.println("filenameurl:" + bmobFile[1].getFileUrl(GuideActivity.this));
                //获得数据的objectId信息
                //  object.getObjectId();
                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                //   object.getCreatedAt();
                initView();
                vpGuide.setAdapter(new GuideAdapter(mImageViewList));
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                //   toast("查询失败："+arg0);
                Toast.makeText(GuideActivity.this, "查询失败：" + arg0, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void initView(){
        layout_point = (LinearLayout)findViewById(R.id.layout_point);
        mImageViewList = new ArrayList<ImageView>();
        for(int i=0;i<imagesId.length;i++){
            final ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
           // image.setImageResource(imagesId[i]);
            //image.setImageURI(bmobFile[i].getFileUrl(GuideActivity.this));
          //  bmobFile[i].loadImage(this,image);
            System.out.println("bmobFile[i].getFilename()："+bmobFile[i].getFilename());
            System.out.println("地址："+getCacheDir());
            final File file = new File(getCacheDir(), bmobFile[i].getFilename());
            if(file.exists()){
                System.out.println("从缓存读取图片");
                Bitmap bm= BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setImageBitmap(bm);
            }else{
                HttpUtils utils = new HttpUtils();
                utils.download(bmobFile[i].getFileUrl(GuideActivity.this), file.getAbsolutePath(), new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        File result = (File)responseInfo.result;
                        System.out.println("图片路径："+result.getAbsolutePath());
                        Bitmap bm = BitmapFactory.decodeFile(result.getAbsolutePath());
                        image.setImageBitmap(bm);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("下载图片失败："+msg);
                    }
                });
            }
            mImageViewList.add(image);
        }
        //动态原点初始化
        for (int i=0;i<imagesId.length;i++){
            View ponit = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
            if(i>0){
                params.leftMargin = 10;
            }
            ponit.setBackgroundResource(R.drawable.shape_point_gray);
            ponit.setLayoutParams(params);
            layout_point.addView(ponit);
        }
        layout_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                pointWidth = layout_point.getChildAt(1).getLeft() - layout_point.getChildAt(0).getLeft();
                System.out.println("圆点距离:" + pointWidth);
                layout_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    class GuideAdapter extends PagerAdapter{
        private ArrayList<ImageView> ViewList;

        public GuideAdapter(ArrayList<ImageView> viewList) {
            this.ViewList = viewList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ViewList.get(position));
            return ViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ViewList.get(position));
        }

        @Override
        public int getCount() {
            return ViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
