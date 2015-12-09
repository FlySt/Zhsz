package com.learn.swl.zhsz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bmob.utils.BmobLog;
import com.learn.swl.zhsz.Bean.User;
import com.learn.swl.zhsz.utils.PerfUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;


public class LoginActivity extends Activity implements View.OnClickListener,PlatformActionListener {
    private RelativeLayout layout_register;
    private EditText et_account,et_password;
    private Button btn_login;
    private ImageButton ib_qq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView(){
        layout_register = (RelativeLayout)findViewById(R.id.layout_register);
        et_account = (EditText)findViewById(R.id.et_account);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        ib_qq = (ImageButton)findViewById(R.id.ib_qq);
        layout_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ib_qq.setOnClickListener(this);
    }
    private void login(){
        String name = et_account.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            PerfUtils.ShowToast(LoginActivity.this, "用户名为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            PerfUtils.ShowToast(LoginActivity.this, "密码为空");
            return;
        }
        final ProgressDialog progress = new ProgressDialog(
                LoginActivity.this);
        progress.setMessage("正在登陆...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
            /*    new Thread(new Runnable(){
                    @Override
                    public void run() {
                        progress.setMessage("正在获取好友列表...");
                    }
                }); */
                progress.dismiss();
                Intent intent = new Intent(LoginActivity.this, LeftActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progress.dismiss();
                PerfUtils.ShowToast(LoginActivity.this, s);
            }
        });
    }
    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {
        if (plat == null) {
          //  popupOthers();
            return;
        }
        if(plat.isValid()){
            plat.removeAccount();
        }
        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                boolean isNetConnected = PerfUtils.isNetworkConnected(LoginActivity.this);
                if(!isNetConnected){
                    PerfUtils.ShowToast(LoginActivity.this,"网络连接错误");
                    return;
                }
                login();
                break;
            case R.id.ib_qq:
                //QQ空间
                Platform qzone = ShareSDK.getPlatform(QQ.NAME);
                authorize(qzone);
               // qqAuthorize();
                break;
        }
    }
    /**
     * @method loginWithAuth
     * @param context
     * @param authInfo
     * @return void
     * @exception
     */
    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo){
        System.out.println("第三方登陆loginWithAuth");
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.i("smile", authInfo.getSnsType() + "登陆成功返回:" + userAuth);
                System.out.println("登陆成功返回");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("json", userAuth.toString());
                intent.putExtra("from", authInfo.getSnsType());
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                System.out.println("第三方登陆失败");
                PerfUtils.ShowToast(LoginActivity.this,"第三方登陆失败：" + msg);
            }

        });
    }
    public static Tencent mTencent;

    private void qqAuthorize(){
      //  if(mTencent==null){
            mTencent = Tencent.createInstance(Config.QQ_APP_ID, getApplicationContext());
      //  }
        if(mTencent == null){
            System.out.println("第三方登陆mTencent");
        }
        mTencent.logout(this);
        System.out.println("第三方登陆qqAuthorize");
        mTencent.login(this, "all", new IUiListener() {

            @Override
            public void onComplete(Object arg0) {
                System.out.println("第三方登陆onComplete");
                toast("第三方登陆onComplete"+arg0);
                // TODO Auto-generated method stub
                if(arg0!=null){
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                        loginWithAuth(authInfo);
                    } catch (JSONException e) {
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                System.out.println("第三方登陆onError");
                // TODO Auto-generated method stub
                PerfUtils.ShowToast(LoginActivity.this,"QQ授权出错：" + arg0.errorCode + "--" + arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                System.out.println("第三方登陆onCancel");
                // TODO Auto-generated method stub
                PerfUtils.ShowToast(LoginActivity.this,"取消qq授权");
            }
        });
    }
    private void signUp(String nikc,String username){
     /*   PlatformDb platDB = platform.getDb();//获取数平台数据DB
        System.out.println("登录成功1");
       final BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, platDB.getToken(),platDB.exportData(),platDB.getUserId());
        System.out.println("登录成功2");
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo,new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                System.out.println("signUp登录成功");
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("signUp登录失败" + s);
            }
        });*/
        final User user = new User();
        user.setNick(nikc);
        user.setUsername(username);
        user.setPassword("123456");
        //��user���豸id���а�aa

        user.signUp(LoginActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                PerfUtils.ShowToast(LoginActivity.this, "注册成功");
                System.out.println("注册成功");
                // ���豸��username���а�
                //���µ���λ����Ϣ
                // updateUserLocation();
                //���㲥֪ͨ��½ҳ���˳�
                // sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
                // ������ҳ
                Intent intent = new Intent(LoginActivity.this, LeftActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                BmobLog.i(arg1);
                PerfUtils.ShowToast(LoginActivity.this, "注册失败" + arg1);
                System.out.println("注册失败" + arg1);
                Intent intent = new Intent(LoginActivity.this, LeftActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String nick,username;
        System.out.println("登录成功");
        //解析部分用户资料字段
        System.out.println("登录成功" + hashMap.toString());
        System.out.println("action:" + i);
        if ( i== Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
           System.out.println(" platDB.getToken()" + platDB.getToken());
            //   platDB.getToken();
            System.out.println("   platDB.getUserGender()" + platDB.getUserGender());
            //  platDB.getUserGender();
            System.out.println("   platDB.getUserIcon()" + platDB.getUserIcon());
            //   platDB.getUserIcon();
            username =     platDB.getUserId();
            System.out.println("    platDB.getUserId()" + platDB.getUserId());
            nick =    platDB.getUserName();
            System.out.println("   nick 111" + nick);
        //    System.out.println("   platDB.getUserName()" + platDB.getUserName());
            signUp(nick,username);
        }

   /*     id=hashMap.get("id").toString();//ID
        name=hashMap.get("name").toString();//用户名
        System.out.println("用户名"+name);
        description=hashMap.get("description").toString();//描述
        profile_image_url=hashMap.get("profile_image_url").toString();//头像链接
        String str="ID: "+id+";\n"+
                "用户名： "+name+";\n"+
                "描述："+description+";\n"+
                "用户头像地址："+profile_image_url;
        System.out.println("用户资料");
        System.out.println("用户资料: "+str);*/
        //用户资源都保存到res
//通过打印res数据看看有哪些数据是你想要的


    }


    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    private void toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
