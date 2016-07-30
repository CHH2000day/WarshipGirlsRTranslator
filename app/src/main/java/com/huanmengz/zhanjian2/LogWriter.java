package com.huanmengz.zhanjian2;
import android.content.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class LogWriter extends Object
{
    private static SimpleDateFormat sdf;
    private static StringBuilder sb;
    private static BufferedOutputStream bos;
    private static boolean inited=false;
    private static Context mctx ;
    private static Date date;
    public static void init(Context ctx) throws FileNotFoundException{
        boolean v=SettingManger.getboolean("log_isenabled",false);
        if(!v)
        {
            return;}
        mctx=ctx;
        inited=true;
        sdf=new SimpleDateFormat();
        File f=new File(SettingManger.getstring("log_path",new File(ctx.getExternalFilesDir("log"),"log.log").getAbsolutePath()));
        if(!f.getParentFile().exists()){f.getParentFile().mkdirs();}
        bos=new BufferedOutputStream(new FileOutputStream(f,true));
        sdf.applyLocalizedPattern("yyyy-MM-dd/HH:mm:ss:SSS");
        writeLog("I","Log started");
    }
    public static void writeLog(String tag,String msg){
        if(inited){
        new Processer(tag,msg).start();}
    }
    protected static void finalize() 
    {try{bos.close();}catch(Throwable t){}
        // TODO: Implement this method
     }
    
    static class Processer extends Thread
    {

        private String mtag,mmsg;
        public Processer(String tag,String msg){
            mmsg=msg;
            mtag=tag;
        }
        @Override
        public void run()
        {sb=new StringBuilder();
        date=new Date(System.currentTimeMillis());
        sb.append(sdf.format(date));
        sb.append(" ");
        sb.append(mtag);
        sb.append(" ");
        sb.append(mmsg);
        sb.append("\n");
            try
            {
                bos.write(sb.toString().getBytes());
                bos.flush();
                
            }
            catch (IOException e)
            {MainActivity.showERROR(e);}

            // TODO: Implement this method
            }
        
    }
}
