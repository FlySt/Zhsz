package com.learn.swl.zhsz.BmobFile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by ADM on 2015/12/1.
 */
public class Data extends BmobObject {
    private BmobFile categories;
    private BmobFile news;

    public BmobFile getNews() {
        return news;
    }

    public BmobFile getCategories() {
        return categories;
    }
}
