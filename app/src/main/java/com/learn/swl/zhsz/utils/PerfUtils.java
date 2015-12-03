package com.learn.swl.zhsz.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fly on 2015/11/25.
 */
public class PerfUtils {
    public static final String PREF_NAME = "config";
    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultValue);
    }
    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
}
