package com.learn.swl.zhsz.Base;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.learn.swl.zhsz.MainActivity;
import com.learn.swl.zhsz.R;

/**
 * Created by ADM on 2015/11/27.
 */
public class BasePager extends FragmentActivity{
    public Activity mActivity;
    public View mRootView;
    public FrameLayout layout_base_content;
    public TextView tv_title;
    private ImageButton btn ;
    public BasePager(Activity activity) {
        mActivity = activity;
        initView();
    }

    public void initView(){
        mRootView = View.inflate(mActivity, R.layout.base_pager,null);
        layout_base_content= (FrameLayout)mRootView.findViewById(R.id.layout_base_content);
        tv_title = (TextView)mRootView.findViewById(R.id.tv_title);
        btn = (ImageButton)mRootView.findViewById(R.id.btn_menu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }
    private void toggleSlidingMenu(){
        MainActivity mainActivity = (MainActivity)mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }
    public void initData(){

    }
    public  void setSlidingMenuEnable(boolean isEnable){
        MainActivity mainActivity = (MainActivity)mActivity;
       SlidingMenu slidingMenu =  mainActivity.getSlidingMenu();
        if(isEnable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
