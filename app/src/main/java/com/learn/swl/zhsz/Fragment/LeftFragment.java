package com.learn.swl.zhsz.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.learn.swl.zhsz.Base.HomePager;
import com.learn.swl.zhsz.Base.NewsCenterPager;
import com.learn.swl.zhsz.LeftActivity;
import com.learn.swl.zhsz.MainActivity;
import com.learn.swl.zhsz.R;
import com.learn.swl.zhsz.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by swl on 2015/11/25.
 */
public class LeftFragment extends BaseFragment {
    ArrayList<NewsData.NewsMenuData> dataList ;
    private String[] strings = new String[]{"设置","推荐","关于"};
    private int[] bitmapId = new int[]{R.mipmap.ic_navi_settings,R.mipmap.ic_navi_intro,R.mipmap.ic_navi_about};
    private ListView lv_menu;
    private StringAdapter stringAdapter;
    private int mCurrentPos;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left,null);
        lv_menu = (ListView)view.findViewById(R.id.lv_menu);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                stringAdapter.notifyDataSetChanged();
                selectFragment(position);
                toggleSlidingMenu();
            }
        });
        stringAdapter = new StringAdapter();
        lv_menu.setAdapter(stringAdapter);
        return view;
    }
    private void toggleSlidingMenu(){
        MainActivity mainActivity = (MainActivity)mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }
    private void selectFragment(int position){
        Intent intent = new Intent(mActivity, LeftActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Override
    public void initData() {

    }
    class StringAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return strings.length;
        }

        @Override
        public String getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.left_menu_item,null);
            TextView tv_left_tile = (TextView)view.findViewById(R.id.tv_left_title);
            tv_left_tile.setText(strings[position]);
            Drawable drawable = getResources().getDrawable(bitmapId[position]);
            tv_left_tile.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
         /*   if(mCurrentPos == position){
                tv_left_tile.setEnabled(true);
            }else{
                tv_left_tile.setEnabled(false);
            } */
            return view;
        }
    }

}
