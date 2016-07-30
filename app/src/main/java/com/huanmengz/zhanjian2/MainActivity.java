package com.huanmengz.zhanjian2;

import android.os.*;
import android.support.v7.app.*;
import android.app.FragmentManager;
import android.support.v7.widget.*;
import android.view.*;
import android.content.*;
import android.support.v4.content.*;
import android.*;
import android.content.pm.*;
import android.app.Fragment;
import android.support.v4.app.*;
import android.view.View.*;
import android.util.*;
import java.io.*;
import android.support.design.widget.*;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity 
{

    public static final int VERSION_CODE=0;
    public static View mainview;
    private static String packagename;
    private static final String TAG_MAINVIEW="MAINVIEW";
    private static final String TAG_SETTING="SETTINGS";
    protected static Fragment currfragment;
	private FragmentManager mfragmentmanger;
    protected static Toolbar mtoolbar;
    protected static SettingPage msp;
    private static Handler errmsghdl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ExceptionHandler.getInstance().init(this);
        packagename = getPackageName();
        setContentView(R.layout.main);
		mfragmentmanger = getFragmentManager();
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        mainview = findViewById(R.id.ll);
        setSupportActionBar(mtoolbar);
        errmsghdl=new Handler(){
            public void handleMessage(Message msg){
                Snackbar.make(mainview,msg.obj.toString(),Snackbar.LENGTH_LONG).show();
            }
        };
        if (!hasPermission())
        {askforpermission();}
        MainRecyclerView mrc=new MainRecyclerView();
        mfragmentmanger.beginTransaction().add(R.id.mainwindow, mrc, TAG_MAINVIEW).commit();
       
        currfragment = mrc;
        mtoolbar.setNavigationOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    onBackPressed();
                }
            });
        SettingManger.init(this,"Translater_Setting");
        msp = new SettingPage(); 
        
       
            try
            {

                LogWriter.init(this);
            }
            catch (FileNotFoundException e)
            {
                Snackbar.make(mainview, "Failed to start log service", Snackbar.LENGTH_LONG).show();

            }
          
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {return true;
    }

    public static void showERROR(Throwable t){
        errmsghdl.sendMessage(errmsghdl.obtainMessage(0,t.getMessage()));
        LogWriter.writeLog("E",t.getLocalizedMessage());
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu2:
                exit();
                break;
            case R.id.menu0:

                getFragmentManager().beginTransaction().addToBackStack(currfragment.getTag())
                    .replace(R.id.mainwindow, msp, TAG_SETTING)
                    .commit();

                break;
            case R.id.menu1:
                break;
        }
        // TODO: Implement this method
        return super.onOptionsItemSelected(item);
    }
    public void exit()
    {
        AlertDialog.Builder adb =new AlertDialog.Builder(MainActivity.this);
        adb.setTitle(R.string.notice)
            .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {android.os.Process.killProcess(android.os.Process.myPid());
                    // TODO: Implement this method
                }
            })
            .setMessage(R.string.exit_msg)
            .setNegativeButton(R.string.cancel, null);
        adb.create().show();
    }

    @Override
    protected void onDestroy()
    {LogWriter.finalize();
    }
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (currfragment.getTag().equals(TAG_MAINVIEW))
            {
                exit();
            }
            else
            {onBackPressed();}
        }
        // TODO: Implement this method
        return true;}
    public boolean hasPermission()
    {
        if (Build.VERSION.SDK_INT < 22)
        {return true;}
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    public void askforpermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.STORAGE}, 7);

    }
    






}
