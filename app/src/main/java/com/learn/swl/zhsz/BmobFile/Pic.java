package com.learn.swl.zhsz.BmobFile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by ADM on 2015/12/1.
 */
public class Pic extends BmobObject{
    BmobFile guide_1;
    BmobFile guide_2;
    BmobFile guide_3;
    BmobFile splash_1;
    BmobFile splash_2;
    public BmobFile getSplash_1() {
        return splash_1;
    }
    public BmobFile getSplash_2() {
        return splash_2;
    }
    public BmobFile getGuide_2() {
        return guide_2;
    }

    public BmobFile getGuide_3() {
        return guide_3;
    }

    public BmobFile getGuide_1() {
        return guide_1;
    }
}
