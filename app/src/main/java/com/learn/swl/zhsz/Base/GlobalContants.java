package com.learn.swl.zhsz.Base;

/**
 * Created by ADM on 2015/11/27.
 */
public class GlobalContants {
    public static final String SERVER_URL = "http://192.168.1.110/zhbj";
    public static final String CATEGORIES_URL = SERVER_URL+"/categories.json";

    public static final String JUHE_JOKE_APPKEY = "981686baf69223c89983dea35bc592f9";
    public static final String JUHE_JOKE_URL = "http://japi.juhe.cn/joke/content/text.from?";
    public static final String JUHE_NEWS_APPKEY = "24ca4ebae94a0944158757634ecd1f01";
    public static final String JUHE_NEWS_WORDS_URL = "http://op.juhe.cn/onebox/news/words?";
    public static final String JUHE_NEWS_QUERY_URL = " http://op.juhe.cn/onebox/news/query";
    public static String MyAvatarDir = "/sdcard/smart/avatar/";
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

    public static final boolean SEX_FEMALE = false;
    public static final boolean SEX_MALE = true;
}
