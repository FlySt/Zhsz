package com.learn.swl.zhsz;

import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.learn.swl.zhsz.Fragment.ContentFragment;
import com.learn.swl.zhsz.Fragment.LeftFragment;
import com.learn.swl.zhsz.R;


public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x;
        slidingMenu.setBehindOffset(width*2/3);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction= fm.beginTransaction();
        transaction.replace(R.id.left_layout,new LeftFragment(),FRAGMENT_LEFT_MENU);
        transaction.replace(R.id.content_layout,new ContentFragment(),FRAGMENT_CONTENT);
        transaction.commit();
    }
    public LeftFragment getLeftFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        LeftFragment leftFragment = (LeftFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return leftFragment;
    }
    public ContentFragment getContentFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return contentFragment;
    }
}
