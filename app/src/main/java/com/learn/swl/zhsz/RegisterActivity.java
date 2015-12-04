package com.learn.swl.zhsz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bmob.utils.BmobLog;
import com.learn.swl.zhsz.Bean.User;
import com.learn.swl.zhsz.utils.PerfUtils;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends Activity {
    private EditText et_nick,et_account,et_password,et_confirm;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_nick = (EditText)findViewById(R.id.et_nick);
        et_account = (EditText)findViewById(R.id.et_account);
        et_password = (EditText)findViewById(R.id.et_password);
        et_confirm = (EditText)findViewById(R.id.et_confirm);
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register(){
        String nick = et_nick.getText().toString();
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String confirm = et_confirm.getText().toString();

        if (TextUtils.isEmpty(nick)) {
            PerfUtils.ShowToast(RegisterActivity.this, "昵称为空");
            return;
        }
        if (TextUtils.isEmpty(account)) {
            PerfUtils.ShowToast(RegisterActivity.this, "用户名为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            PerfUtils.ShowToast(RegisterActivity.this, "密码为空");
            return;
        }
        if (!confirm.equals(password)) {
            PerfUtils.ShowToast(RegisterActivity.this, "两次密码不一致");
            return;
        }

        boolean isNetConnected = PerfUtils.isNetworkConnected(RegisterActivity.this);
        if(!isNetConnected){
            PerfUtils.ShowToast(RegisterActivity.this, "网络连接出错");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在注册..");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        //����ÿ��Ӧ�õ�ע����������϶���һ������IM sdkδ�ṩע�᷽�����û��ɰ���bmod SDK��ע�᷽ʽ����ע�ᡣ
        //ע���ʱ����Ҫע�����㣺1��User���а��豸id��type��2���豸���а�username�ֶ�
        final User user = new User();
        user.setNick(nick);
        user.setUsername(account);
        user.setPassword(password);
        //��user���豸id���а�aa
        user.setSex(true);

        user.signUp(RegisterActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                progress.dismiss();
                PerfUtils.ShowToast(RegisterActivity.this, "注册成功");
                // ���豸��username���а�
                //���µ���λ����Ϣ
                // updateUserLocation();
                //���㲥֪ͨ��½ҳ���˳�
                // sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
                // ������ҳ
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                BmobLog.i(arg1);
                PerfUtils.ShowToast(RegisterActivity.this, "注册失败"+arg1);
                progress.dismiss();
            }
        });
    }
}
