package com.learn.swl.zhsz.Fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.learn.swl.zhsz.LoginActivity;
import com.learn.swl.zhsz.R;

/**
 * Created by SWan on 2015/12/4.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout layout_user_icon;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting,null);
        layout_user_icon = (RelativeLayout)view.findViewById(R.id.layout_user_icon);
        layout_user_icon.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_user_icon:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }
}
