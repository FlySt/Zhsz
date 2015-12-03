package com.learn.swl.zhsz.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.learn.swl.zhsz.Base.NewsCenterPager;
import com.learn.swl.zhsz.MainActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by swl on 2015/11/25.
 */
public class LeftFragment extends BaseFragment {
    ArrayList<NewsData.NewsMenuData> dataList ;
    private ListView lv_menu;
    MenuAdapter mAdapter;
    private int mCurrentPos;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left,null);
        lv_menu = (ListView)view.findViewById(R.id.lv_menu);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();
                setCurrentDetailPager(position);
                toggleSlidingMenu();
            }
        });
        return view;
    }
    private void toggleSlidingMenu(){
        MainActivity mainActivity = (MainActivity)mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }
    private void setCurrentDetailPager(int position){
        MainActivity mainActivity = (MainActivity)mActivity;
        ContentFragment fragment = mainActivity.getContentFragment();
        NewsCenterPager pager =  fragment.getNewsCenterPager();
        pager.setCurrentMenuDetailPager(position);
    }
    public void setNewsData(NewsData data){
        dataList = data.data;
        mAdapter = new MenuAdapter();
        lv_menu.setAdapter(mAdapter);
    }
    @Override
    public void initData() {

    }

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.left_menu_item,null);
            TextView tv_left_tile = (TextView)view.findViewById(R.id.tv_left_title);
            tv_left_tile.setText(getItem(position).title);
            if(mCurrentPos == position){
                tv_left_tile.setEnabled(true);
            }else{
                tv_left_tile.setEnabled(false);
            }
            return view;
        }
    }
}
