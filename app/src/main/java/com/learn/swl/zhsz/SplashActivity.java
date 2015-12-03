package com.learn.swl.zhsz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.learn.swl.zhsz.BmobFile.Pic;
import com.learn.swl.zhsz.utils.PerfUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;


public class SplashActivity extends Activity {
    BmobFile[] bmobFile = new BmobFile[2];
    Bitmap[] bm = new Bitmap[2];
    RelativeLayout splash_layout;
    ImageView iv_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 初始化 Bmob SDK
        Bmob.initialize(this, Config.applicationId);
        splash_layout = (RelativeLayout)findViewById(R.id.splash_layout);
        iv_splash = (ImageView)findViewById(R.id.iv_splash);
        getSplashPic();
        float density = getResources().getDisplayMetrics().density;
        int densityDpi = getResources().getDisplayMetrics().densityDpi;

        System.out.println("设备密度:"+density+",像素密度："+densityDpi);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    splash_layout.setBackgroundDrawable(new BitmapDrawable(getResources(), bm[0]));
                    break;
                case 1:
                    iv_splash.setImageBitmap(bm[1]);
                    startAnim();
                    break;
            }
        }
    };
    private void initView(){
        for(int i =0;i<bmobFile.length;i++){
            final File file = new File(getCacheDir(), bmobFile[i].getFilename());
            if(file.exists()){
                System.out.println("从缓存读取图片");
                bm[i] = BitmapFactory.decodeFile(file.getAbsolutePath());
                if(i==bmobFile.length-1){
                    splash_layout.setBackgroundDrawable(new BitmapDrawable(getResources(), bm[0]));
                    iv_splash.setImageBitmap(bm[1]);
                    startAnim();
                }
                System.out.println("file："+file.getAbsolutePath());
            }else{
                HttpUtils utils = new HttpUtils();
                final int finalI = i;
                utils.download(bmobFile[i].getFileUrl(SplashActivity.this), file.getAbsolutePath(), new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        File result = (File)responseInfo.result;
                        System.out.println("图片路径："+result.getAbsolutePath());
                        bm[finalI] = BitmapFactory.decodeFile(result.getAbsolutePath());
                        Message msg = handler.obtainMessage();
                        msg.obj = bm[finalI];
                        msg.what = finalI;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("下载图片失败："+msg);
                    }
                });
            }
        }


    }
    private void getSplashPic(){
        BmobQuery<Pic> query = new BmobQuery<Pic>();
        query.addQueryKeys("splash_1,splash_2");
        query.getObject(this, "bHyqMMMU", new GetListener<Pic>() {

            @Override
            public void onSuccess(Pic object) {
                // TODO Auto-generated method stub
                //toast("查询成功：");
                Toast.makeText(SplashActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                System.out.println("查询成功:");
                bmobFile[0] = object.getSplash_1();
                bmobFile[1] = object.getSplash_2();
                System.out.println("filename:" + bmobFile[0].getFilename());
                System.out.println("filenameurl:" + bmobFile[1].getFileUrl(SplashActivity.this));
                //获得数据的objectId信息
              //  object.getObjectId();
                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
             //   object.getCreatedAt();
                initView();
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                //   toast("查询失败："+arg0);
                Toast.makeText(SplashActivity.this, "查询失败：" + arg0, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void startAnim(){
        AnimationSet set = new AnimationSet(true);
        //缩放动画
        ScaleAnimation scale = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(2000);
        scale.setFillAfter(true);
        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);
        rotate.setFillAfter(true);
        //渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(scale);
        set.addAnimation(rotate);
        splash_layout.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void jumpNextActivity(){
     //   SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
        boolean userGuide = PerfUtils.getBoolean(this,"is_user_guide_showed", false);
        if(!userGuide){
            startActivity(new Intent(SplashActivity.this,GuideActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();
    }
}
