package com.learn.swl.zhsz;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import com.learn.swl.zhsz.Fragment.SettingFragment;

public class LeftActivity extends FragmentActivity {
    private Fragment[] fragments ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        int position = getIntent().getIntExtra("position", 0);
        initFragment(position);
    }
    private void initFragment(int position){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction= fm.beginTransaction();
        transaction.replace(R.id.layout_left_activity,new SettingFragment());
        transaction.commit();
    }

}
