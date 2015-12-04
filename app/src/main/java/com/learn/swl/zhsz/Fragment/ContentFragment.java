package com.learn.swl.zhsz.Fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.learn.swl.zhsz.Base.BasePager;
import com.learn.swl.zhsz.Base.HomePager;
import com.learn.swl.zhsz.Base.NewsCenterPager;
import com.learn.swl.zhsz.Base.TravelPager;
import com.learn.swl.zhsz.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by ADM on 2015/11/25.
 */
public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.bottom_group)
    private RadioGroup bottom_group;
    @ViewInject(R.id.vp_content)
    private ViewPager vp_content;

    private ArrayList<BasePager> pagerList = new ArrayList<BasePager>();
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        ViewUtils.inject(this, view);

        return view;
    }
    @Override
    public void initData() {
        bottom_group.check(R.id.rb_home);

        pagerList.add(new HomePager(mActivity));
        pagerList.add(new NewsCenterPager(mActivity));
        pagerList.add(new TravelPager(mActivity));
        vp_content.setAdapter(new ContentAdapter());
        bottom_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rb_travel:
                        vp_content.setCurrentItem(2, false);
                        break;
                }
            }
        });
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerList.get(0).initData();
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = pagerList.get(position);
            container.addView(pager.mRootView);
           // pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pagerList.get(position).mRootView);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
    public  NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager)pagerList.get(1);
    }
    public HomePager getHomePager(){
        return (HomePager)pagerList.get(0);
    }
}
