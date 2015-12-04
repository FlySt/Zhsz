package com.learn.swl.zhsz;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import com.learn.swl.zhsz.Fragment.SettingFragment;

public class LeftActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction= fm.beginTransaction();
        transaction.replace(R.id.layout_left_activity,new SettingFragment());
        transaction.commit();
    }


}
