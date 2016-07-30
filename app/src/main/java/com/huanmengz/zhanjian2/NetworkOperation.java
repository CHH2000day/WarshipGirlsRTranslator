package com.huanmengz.zhanjian2;
import java.io.*;
import org.json.*;
import java.net.*;

public class NetworkOperation
{
    private static final String SERVER_IP="121.42.44.58";
    protected static JSONObject getManifest() throws IOException, JSONException{
        String s;
        InputStream i;
        if(BuildConfig.DEBUG){s="file:///storage/sdcard1/testmanifest";i=new FileInputStream(s);}
        else{s="http://"+SERVER_IP+"/Translater/manifest";}
        URL u=new URL(s);
        LogWriter.writeLog("I","getting data from "+s);
        i=u.openStream();
        byte[] h=readalldata(i);
        String str=new String(h);
        return new JSONObject(str);
    }
 
    

public static byte[] readalldata(InputStream is) throws IOException{
    byte[] cache=new byte[1024];
    ByteArrayOutputStream baos=new ByteArrayOutputStream();
    int g;
    while((g=is.read(cache))!=-1){
        baos.write(cache,0,g);
        
    }
    return baos.toByteArray();
}
}
