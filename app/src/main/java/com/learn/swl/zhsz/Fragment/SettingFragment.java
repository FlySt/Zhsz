package com.learn.swl.zhsz.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.swl.zhsz.Base.GlobalContants;
import com.learn.swl.zhsz.Base.MyApplication;
import com.learn.swl.zhsz.Bean.User;
import com.learn.swl.zhsz.LoginActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.utils.CacheUtils;
import com.learn.swl.zhsz.utils.PerfUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;



/**
 * Created by SWan on 2015/12/4.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "SettingFragment";
    private RelativeLayout layout_user_icon,layout_nick;
    private TextView tv_nick;
    private Button btn_logout;
    private ImageView iv_user_icon;
    private CheckBox sex_switch;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting,null);
        layout_user_icon = (RelativeLayout)view.findViewById(R.id.layout_user_icon);
        layout_nick = (RelativeLayout)view.findViewById(R.id.layout_nick);
        tv_nick = (TextView)view.findViewById(R.id.tv_nick);
        btn_logout = (Button)view.findViewById(R.id.btn_logout);
        iv_user_icon = (ImageView)view.findViewById(R.id.iv_user_icon);
        sex_switch = (CheckBox)view.findViewById(R.id.sex_choice_switch);
        layout_user_icon.setOnClickListener(this);
        layout_nick.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        sex_switch.setOnCheckedChangeListener(this);
        return view;
    }


    @Override
    public void initData() {
        System.out.println("数据初始化");
        initPersonInfo();
    }
    private void initPersonInfo(){
        User user = PerfUtils.getCurrentUser(mActivity);
        if(user!=null){
            tv_nick.setText(user.getNick().toString());
            if (user.getSex()) {
                Log.i(TAG,"SEX_MALE:");
                sex_switch.setChecked(true);
            } else {
                sex_switch.setChecked(false);
            }
            BmobFile avatarFile = user.getAvatar();
            if (null != avatarFile) {
                ImageLoader.getInstance().displayImage(
                        avatarFile.getFileUrl(mActivity),
                        iv_user_icon,
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
            }
            btn_logout.setText("退出登录");
        }else{
            tv_nick.setText(getResources().getString(R.string.click_login));
            btn_logout.setText("登录");
        }
    }
    private boolean isLogin(){
        User user = PerfUtils.getCurrentUser(mActivity);
        if(user!=null){
            return true;
        }
        return false;
    }
    private void goLogin(){
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
        PerfUtils.ShowToast(mActivity, "请先登录");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_user_icon:

                if(isLogin()){
                    showAlbumDialog();
                }else{
                    goLogin();
                }
                break;
            case R.id.layout_nick:
                if(isLogin()){

                }else{
                    goLogin();
                }
                break;
            case R.id.btn_logout:
                if(isLogin()){
                    BmobUser.logOut(mActivity);
                }else {
                    goLogin();
                }
                break;
        }
    }
    String dateTime;
    AlertDialog albumDialog;

    public void showAlbumDialog() {
        albumDialog = new AlertDialog.Builder(mActivity).create();
        albumDialog.setCanceledOnTouchOutside(true);
        View v = LayoutInflater.from(mActivity).inflate(
                R.layout.dialog_usericon, null);
        albumDialog.show();
        albumDialog.setContentView(v);
        albumDialog.getWindow().setGravity(Gravity.CENTER);

        TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
        TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
        albumPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                Date date1 = new Date(System.currentTimeMillis());
                dateTime = date1.getTime() + "";
                getAvataFromAlbum();
            }
        });
        cameraPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                getAvataFromCamera();
            }
        });
    }
    private void getAvataFromAlbum() {
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        intent2.setType("image/*");
        startActivityForResult(intent2, GlobalContants.REQUESTCODE_UPLOADAVATAR_LOCATION);
    }
    public String filePath = "";
    private void getAvataFromCamera() {
        File dir = new File(GlobalContants.MyAvatarDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 原图
        File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
                .format(new Date()));
        filePath = file.getAbsolutePath();// 获取相片的保存路径
        Uri imageUri = Uri.fromFile(file);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camera, GlobalContants.REQUESTCODE_UPLOADAVATAR_CAMERA);
    }
    String iconUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
             /*   case UPDATE_SEX:
                    initPersonalInfo();
                    break;
                case UPDATE_ICON:
                    initPersonalInfo();
                    iconLayout.performClick();
                    break;
                case UPDATE_SIGN:
                    initPersonalInfo();
                    signLayout.performClick();
                    break;
                case EDIT_SIGN:
                    initPersonalInfo();
                    break; */
                case GlobalContants.REQUESTCODE_UPLOADAVATAR_CAMERA:
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        // ShowToast("SD不可用");
                        PerfUtils.ShowToast(mActivity,"SD卡不可用");
                        // Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = new File(filePath);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                    } else {

                    }
                    break;
                case GlobalContants.REQUESTCODE_UPLOADAVATAR_LOCATION:
                    if (data == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case GlobalContants.REQUESTCODE_UPLOADAVATAR_CROP:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            // 锟斤拷锟斤拷图片
                            iconUrl = saveToSdCard(bitmap);
                            iv_user_icon.setImageBitmap(bitmap);
                            updateIcon(iconUrl);
                        }
                    }
                    break;
         /*   case GO_LOGIN:
                initPersonalInfo();
                logout.setText("退出登录");
                break;*/
                default:
                    break;
            }
        }

    }
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, GlobalContants.REQUESTCODE_UPLOADAVATAR_CROP);

    }
    private void updateIcon(String avataPath) {
        if (avataPath != null) {
            final BmobFile file = new BmobFile(new File(avataPath));
            file.upload(mActivity, new UploadFileListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "上传文件成功。" + file.getFileUrl(mActivity));
                    User currentUser = BmobUser.getCurrentUser(mActivity,
                            User.class);
                    currentUser.setAvatar(file);
                    currentUser.update(mActivity, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub

                            PerfUtils.ShowToast(mActivity, "更改头像成功。");
                        }

                        @Override
                        public void onFailure(int arg0, String arg1) {
                            // TODO Auto-generated method stub
                            PerfUtils.ShowToast(mActivity, "更新头像失败。请检查网络~");
                            Log.i(TAG, "更新失败2-->" + arg1);
                        }
                    });
                }

                @Override
                public void onProgress(Integer arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    // TODO Auto-generated method stub
                    PerfUtils.ShowToast(mActivity, "上传头像失败。请检查网络~");
                    Log.i(TAG, "上传文件失败。" + arg1);
                }
            });
        }
    }
    String path;
    public String saveToSdCard(Bitmap bitmap) {
        // 保存图片
        String filename = new SimpleDateFormat("yyMMddHHmmss")
                .format(new Date())+".jpg";
        path = GlobalContants.MyAvatarDir + filename;
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    private void updateSex(int sex){
        User user = BmobUser.getCurrentUser(mActivity,User.class);
        Log.i(TAG,"updateSex:"+sex);
        if(user!=null){
            if(sex == 0){
                user.setSex(GlobalContants.SEX_FEMALE);
            }else{
                user.setSex(GlobalContants.SEX_MALE);
            }
            user.update(mActivity, new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG,"更新信息成功:");
                    PerfUtils.ShowToast(mActivity,"更新信息成功");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG,"更新信息失败，请检查网络:");
                    PerfUtils.ShowToast(mActivity,"更新信息失败，请检查网络");
                }
            });
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.sex_choice_switch:
                if(isChecked){
                    updateSex(1);
                }else {
                    updateSex(0);
                }
                break;
        }
    }
}
