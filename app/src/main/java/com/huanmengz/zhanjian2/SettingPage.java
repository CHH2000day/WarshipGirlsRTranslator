package com.huanmengz.zhanjian2;
import android.os.*;
import android.preference.*;
import android.view.*;
import java.io.*;

public class SettingPage extends PreferenceFragment
{
    public String logfilepos(){
        return getPreferenceManager().getSharedPreferences().getString("log_parh",getActivity().getExternalFilesDirs("log").toString()+"/log.log");
    };
    public boolean isLogEnable;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("Translater_Setting");
        addPreferencesFromResource(R.xml.settinglist);
        getPreferenceManager().findPreference("log_path").setDefaultValue(getActivity().getExternalFilesDir("log").toString()+"/log.log");
       isLogEnable= getPreferenceManager().getSharedPreferences().getBoolean("log_isenabled",false);
        
        }

    
     
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MainActivity.currfragment=this;
        MainActivity.mtoolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        MainActivity.mtoolbar.setTitle(R.string.setting);
 return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    public boolean getboolean(String key,boolean defaultans){
        return getPreferenceManager().getSharedPreferences().getBoolean(key,defaultans);
    }
    public String getstring(String key,String defaultvalue){
        return getPreferenceManager().getSharedPreferences().getString(key,defaultvalue);
    }
   
}
