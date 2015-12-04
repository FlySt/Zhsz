package com.learn.swl.zhsz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

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
    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }
    private static Toast mToast;
    public static void ShowToast(final Context context, final String text){
        if (!TextUtils.isEmpty(text)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mToast == null) {
                        mToast = Toast.makeText(context, text,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }
}
