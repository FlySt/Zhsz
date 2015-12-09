package com.learn.swl.zhsz.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learn.swl.zhsz.R;

/**
 * Created by ADM on 2015/12/9.
 */
public class RefreshListView extends ListView {
    public static final int STATE_PULL_REFRESH = 0; //下拉刷新
    public static final int STATE_RELEASE_REFRESH = 1; //松开刷新
    public static final int STATE_REFRESHING = 2; //正在刷新
    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tv_state;
    private TextView tv_time;
    private ImageView iv_arrow;
    private ProgressBar progressBar;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    public RefreshListView(Context context) {
        super(context);
        initHeaderView();

    }

    @SuppressLint("NewApi")
    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }
    private int headerViewHeight;
    private View headerView;
    private void initHeaderView(){
        headerView = View.inflate(getContext(), R.layout.refresh_header,null);
        tv_state = (TextView)headerView.findViewById(R.id.tv_state);
        tv_time = (TextView)headerView.findViewById(R.id.tv_time);
        iv_arrow = (ImageView)headerView.findViewById(R.id.iv_arrow);
        progressBar = (ProgressBar)headerView.findViewById(R.id.progress);
        this.addHeaderView(headerView);
        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0,-headerViewHeight,0,0);
        initArrowAnim();
    }
    private int startY = -1;
    private int endY,dY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY == -1){
                    startY = (int)ev.getRawY();
                }
                if(mCurrrentState == STATE_REFRESHING){//正在刷新时不处理
                    break;
                }
                endY = (int)ev.getRawY();
                dY = endY - startY;
                if(dY>0 && getFirstVisiblePosition() == 0){
                    int padding = dY -headerViewHeight;
                    headerView.setPadding(0, padding, 0, 0);// 设置当前padding
                    if(padding>0&&mCurrrentState!=STATE_RELEASE_REFRESH){
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    }else if(padding<0&&mCurrrentState!=STATE_PULL_REFRESH){
                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;//重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    headerView.setPadding(0, 0, 0, 0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);// 隐藏
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (mCurrrentState){
            case STATE_PULL_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                iv_arrow.startAnimation(animDown);
                break;
            case STATE_REFRESHING:
                tv_state.setText("正在刷新");
                iv_arrow.clearAnimation();// 必须先清除动画,才能隐藏
                iv_arrow.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                tv_state.setText("松开刷新");
                iv_arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                iv_arrow.startAnimation(animUp);
                break;
        }
    }
    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }
}
