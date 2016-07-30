package com.huanmengz.zhanjian2;

import android.content.*;

public class SettingManger
{
    private static SharedPreferences msp;
    private static Context mctx;
    private static String ms;
    public static void init(Context ctx,String prefname){
       ms=prefname;
       mctx=ctx;
    }
    public static boolean getboolean(String key,boolean defaultvalue){
        msp=mctx.getSharedPreferences(ms,0);
        return msp.getBoolean(key,defaultvalue);
    }
    public static String getstring(String key,String defaultvalue){
        msp=mctx.getSharedPreferences(ms,0);
        return msp.getString(key,defaultvalue);
    }
    
}
