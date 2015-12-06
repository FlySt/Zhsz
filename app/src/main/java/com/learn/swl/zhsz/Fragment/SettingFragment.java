package com.learn.swl.zhsz.Fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learn.swl.zhsz.Bean.User;
import com.learn.swl.zhsz.LoginActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.utils.PerfUtils;

/**
 * Created by SWan on 2015/12/4.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout layout_user_icon;
    private TextView tv_nick;
    private Button btn_logout;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting,null);
        layout_user_icon = (RelativeLayout)view.findViewById(R.id.layout_user_icon);
        tv_nick = (TextView)view.findViewById(R.id.tv_nick);
        btn_logout = (Button)view.findViewById(R.id.btn_logout);
        layout_user_icon.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        System.out.println("数据初始化");
        User user = PerfUtils.getCurrentUser(mActivity);
        if(user!=null){
            tv_nick.setText(user.getNick().toString());
            btn_logout.setText("退出登录");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_user_icon:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
        }
    }
}
