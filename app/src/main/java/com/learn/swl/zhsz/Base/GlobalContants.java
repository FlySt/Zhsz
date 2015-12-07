package com.learn.swl.zhsz.Base;

/**
 * Created by ADM on 2015/11/27.
 */
public class GlobalContants {
    public static final String SERVER_URL = "http://192.168.1.110/zhbj";
    public static final String CATEGORIES_URL = SERVER_URL+"/categories.json";

    public static String MyAvatarDir = "/sdcard/smart/avatar/";
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
}
