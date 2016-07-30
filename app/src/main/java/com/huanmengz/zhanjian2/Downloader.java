package com.huanmengz.zhanjian2;
import android.os.*;
import java.io.*;
import java.net.*;
import android.support.v7.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.net.*;

public class Downloader extends AsyncTask
{
    private AlertDialog ad;
    private Handler hdl;
    private static boolean isApk=false;
    private String[] murl;
    private File[] mtarget;
    private Context mctx;


    private boolean canceled=false;
    public Downloader(Context ctx, String url, File targetFile, boolean isapk)
    {
        murl = new String[]{url};
        mtarget = new File[]{targetFile};
        mctx = ctx;
        isApk = isapk;
    }
    public Downloader(Context ctx, String[] url, File[] targetFile)
    {
        mctx = ctx;
        murl = url;
        mtarget = targetFile;
    }

    @Override
    protected void onPreExecute()
    {
        LayoutInflater li=LayoutInflater.from(mctx);
        AlertDialog.Builder adb=new AlertDialog.Builder(mctx);
        View mv=li.inflate(R.layout.prog, null);
        final TextView filename=(TextView)mv.findViewById(R.id.progfile);
        final TextView status=(TextView)mv.findViewById(R.id.progstatus);
        final ProgressBar pgbar=(ProgressBar)mv.findViewById(R.id.progProgressBar1);
        adb.setTitle(R.string.download)
           .setCancelable(false)
           .setView(mv)
           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    canceled = true;
                    cancel(true);
                    // TODO: Implement this method
                }
            });
        ad = adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        hdl = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 0:
                        pgbar.setMax(Integer.parseInt(msg.obj.toString()));
                        break;
                    case 1:
                        pgbar.setProgress(Integer.parseInt(msg.obj.toString()));
                        break;
                    case 2:
                        filename.setText(msg.obj.toString());
                        break;
                    case 3:
                        status.setText(String.valueOf(pgbar.getProgress()) + "/" + String.valueOf(pgbar.getMax()));
                        break;
                    case 4:
                        ad.cancel();
                        break;
                    case 5:
                        ModInstaller mi=new ModInstaller(mctx,mtarget[0],mtarget[1]);
                        mi.execute();
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
            hdl.sendMessage(hdl.obtainMessage(0, murl.length));

            for (int i=0;i < murl.length;i++)
            {
                hdl.sendMessage(hdl.obtainMessage(1, i+1));
                hdl.sendEmptyMessage(3);
                hdl.sendMessage(hdl.obtainMessage(2, mtarget[i].getName()));
                if (canceled)
                {return null;}
                if (!mtarget[i].getParentFile().exists())
                {
                    mtarget[i].getParentFile().mkdirs();
                }
                FileOutputStream fos=new FileOutputStream(mtarget[i]);
                URL d=new URL(murl[i]);
                byte[] cache=new byte[1024];
                InputStream in=d.openStream();
                int c;
                if (canceled)
                {
                    in.close();
                    fos.close();
                    return null;}
                while ((c = in.read(cache)) != -1)
                {
                    if (canceled)
                    {
                        in.close();
                        fos.close();
                        return null;

                    }
                    fos.write(cache, 0, c);
                }
                fos.close();
                in.close();
            }
            if(isApk){
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(mtarget[0]),"application/vnd.android.package-archive");
                mctx.startActivity(intent);
            }
            else{
                hdl.sendEmptyMessage(5);
               }
        }
        catch (Throwable t)
        {
            MainActivity.showERROR(t);
        }

        cancel(false);
        // TODO: Implement this method
        return null;
    }

    @Override
    protected void onCancelled()
    {
        hdl.sendEmptyMessage(4);
        // TODO: Implement this method
        super.onCancelled();
    }
    

}
