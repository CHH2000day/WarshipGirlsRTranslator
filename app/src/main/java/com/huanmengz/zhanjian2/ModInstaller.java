package com.huanmengz.zhanjian2;
import android.os.*;
import android.content.*;
import java.io.*;
import org.json.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

public class ModInstaller extends AsyncTask
{

    private Context mctx;
    private File maf,mof;
    private Handler hdl;
    private int count=0;
    private boolean c=false;
    private AlertDialog ad;
    public ModInstaller(Context ctx, File mod, File manifest)
    {
        mctx = ctx;
        maf = manifest;
        mof = mod;

    }
    @Override
    protected void onPreExecute()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(mctx);
        LayoutInflater li=LayoutInflater.from(mctx);
        View e=li.inflate(R.layout.prog, null);
        final TextView status =(TextView)e.findViewById(R.id.progstatus);
        final TextView file=(TextView)e.findViewById(R.id.progfile);
        final ProgressBar pb=(ProgressBar)e.findViewById(R.id.progProgressBar1);
        adb.setTitle("Please wait.......")
            .setCancelable(false)
            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {c = true;
                    cancel(true);
                    // TODO: Implement this method
                }


            })
            .setView(e);
        ad = adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        hdl = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 0:
                        pb.setMax(Integer.parseInt(msg.obj.toString()));
                        break;
                    case 1:
                        status.setText(msg.obj.toString());
                        break;
                    case 2:
                        pb.setProgress(count++);
                        file.setText(msg.obj.toString());

                        break;
                    case 3:
                        ad.cancel();
                        break;
                }
            }
        };


        // TODO: Implement this method
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] p1)
    {
        try
        {
            FileInputStream maniin=new FileInputStream(maf);
            byte[] ca=new byte[maniin.available()];
            maniin.read(ca);
            maniin.close();
            JSONObject jo=new JSONObject(new String(ca));
            JSONArray ja=jo.getJSONArray("Data");
            hdl.sendMessage(hdl.obtainMessage(0, ja.length() * 3));
            FileInputStream modin=new FileInputStream(mof);
            for (int in=0;in < ja.length();in++)
            {
                if (c)
                {break;}
                JSONObject j=ja.getJSONObject(in);
                String filename=j.getString("FileName");
                String fpath=j.getString("Path");
                String verify=j.getString("VerifyCode");
                int len=j.getInt("Length");
                hdl.sendMessage(hdl.obtainMessage(2, filename));

                hdl.sendMessage(hdl.obtainMessage(1, "Reading file"));
                if(c){
                    break;
                }
                byte[] cache=new byte[len];
                modin.read(cache);
                hdl.sendMessage(hdl.obtainMessage(1, "Verifying file"));
                if(c){break;}
                String tmp=Crypt.SHA1Encrypt(cache);
                if (!tmp.equals(verify))
                {
                    throw new Exception("File " + filename + " is incorrect.Installation aborted");

                }
                hdl.sendMessage(hdl.obtainMessage(1, "Writing file"));
                if(c){break;}
                File target=new File(mctx.getFilesDir().getParentFile().getAbsolutePath() + fpath, filename);
                if (!target.getParentFile().exists())
                {
                    target.getParentFile().mkdirs();
                }
                FileOutputStream fos=new FileOutputStream(target);
                if(c){break;}
                fos.write(cache);
                fos.flush();
                fos.close();

            }
            modin.close();
            hdl.sendEmptyMessage(3);
         }
        catch (Throwable e)
        {
            c = true;
            cancel(true);
            MainActivity.showERROR(e);

            
        }

        // TODO: Implement this method
        return null;
    }

    @Override
    protected void onCancelled()
    {
        hdl.sendEmptyMessage(3);
        // TODO: Implement this method
        super.onCancelled();
    }

}
