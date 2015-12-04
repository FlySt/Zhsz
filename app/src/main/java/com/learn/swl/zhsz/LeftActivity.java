package com.learn.swl.zhsz;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.learn.swl.zhsz.Fragment.AboutFragment;
import com.learn.swl.zhsz.Fragment.IntroFragment;
import com.learn.swl.zhsz.Fragment.SettingFragment;

public class LeftActivity extends FragmentActivity {
    private Fragment[] fragments ;
    private Fragment settingFragment,introFragment,aboutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        int position = getIntent().getIntExtra("position", 0);
        initFragment(position);
    }
    private void initFragment(int position){
        settingFragment = new SettingFragment();
        introFragment = new IntroFragment();
        aboutFragment = new AboutFragment();
        fragments = new Fragment[]{settingFragment,introFragment,aboutFragment};
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction= fm.beginTransaction();
        transaction.replace(R.id.layout_left_activity,fragments[position]);
        transaction.commit();
    }

}
