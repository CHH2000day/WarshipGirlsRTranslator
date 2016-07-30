package com.huanmengz.zhanjian2;
import android.app.Fragment;
import android.os.*;
import android.view.*;
import android.support.v7.widget.*;
import android.support.v7.app.*;
import android.util.*;
import android.support.design.widget.*;
import org.json.*;
import java.io.*;
import android.*;
import android.content.*;
import java.util.*;
import android.widget.*;
import android.view.View.*;

public class MainRecyclerView extends Fragment
{

    private static LayoutInflater mli;
    private RecyclerView rv;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {   // TODO: Implement this method
        MainActivity.mtoolbar.setNavigationIcon(null);
        MainActivity.mtoolbar.setTitle(R.string.app_name);
        MainActivity.currfragment=this;
        mli=inflater;
        rv= new RecyclerView(getActivity());
        GetInfofromServer g=new GetInfofromServer();
        g.execute();
        return rv;}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // TODO: Implement this method
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    private class GetInfofromServer extends AsyncTask
    {

        private AlertDialog ad;
        private Handler hdl;
        private AlertDialog.Builder et;
        private String[] infos,mkers,manifesturl,modurl,mname;
        @Override
        protected void onPreExecute()
        {
           AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
           adb.setMessage(R.string.please_wait);
           adb.setCancelable(false);
           ad=adb.create();
           ad.setCanceledOnTouchOutside(false);
           ad.show();
           hdl=new Handler(){
               public void handleMessage(Message msg){
                   if(msg.what==1){
                   TheAdapter a=new TheAdapter(mname,modurl,manifesturl,mkers,infos);
                   rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                   rv.setAdapter(a);}
                   if(msg.what==2){
                       et.create().show();
                   }
                   
                   ad.dismiss();
               }
           };
            
            
            // TODO: Implement this method
        }

        void quit(Throwable t){
            MainActivity.showERROR(t);
            cancel(true);
        }
        
        @Override
        protected Object doInBackground(Object[] p1)
        {
            try
            {
                JSONObject src=NetworkOperation.getManifest();
                int newver=src.getInt("Version");
                if(newver>MainActivity.VERSION_CODE){
                    String msg=src.getString("Info");
                    final String u=src.getString("UpdateURL");
                    et=new AlertDialog.Builder(getActivity());
                    et.setTitle(R.string.notice);
                    String sh=getActivity().getString(R.string.updatemsg)+msg;
                    et.setMessage(sh);
                    et.setNegativeButton(R.string.cancel,null);
                    et.setPositiveButton(R.string.download, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface p1, int p2)
                            {
                                try{
                                Downloader q=new Downloader(getActivity(),u,File.createTempFile("pkg",".apk",getActivity().getExternalCacheDir()),true);
                                q.execute();
                                }
                                catch(Exception e){
                                    quit(e);
                                }
                                // TODO: Implement this method
                            }
                        });
                        hdl.sendEmptyMessage(2);
                     }
                JSONArray mod=src.getJSONArray("Mod");
                int l=mod.length();
                infos=new String[l];
                mkers=new String[l];
                manifesturl=new String[l];
                modurl=new String[l];
                mname=new String[l];
                for(int i=0;i<l;i++){
                    JSONObject j=mod.getJSONObject(i);
                    infos[i]=j.getString("Info");
                    mkers[i]=j.getString("Maker");
                    manifesturl[i]=j.getString("ManifestURL");
                    modurl[i]=j.getString("ModURL");
                    mname[i]=j.getString("Name");
                    
                }
                hdl.sendEmptyMessage(1);
            }
            catch(Throwable t){
                quit(t);
            }
            return null;
        }

        @Override
        protected void onCancelled()
        {
            hdl.sendEmptyMessage(0);
            // TODO: Implement this method
            super.onCancelled();
        }
        

        
    }
    private class TheAdapter extends RecyclerView.Adapter
    {
        private LayoutInflater mli;
        private String[] mname,minfo,mmker,mmodurl,mmanifest;
        private OnClickListener listener;
        
        protected TheAdapter(String[] name,final String[] modurl,final String[] manifesturl,String[] mkers,String[] infos){
            mname=name;
            minfo=infos;
            mmker=mkers;
            mmodurl=modurl;
            mmanifest=manifesturl;
            mli=getActivity().getLayoutInflater();
            listener = new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    try
                    {
                        int c=p1.getTag();
                        String maniur=manifesturl[c];
                        String modur=modurl[c];
                        String[] url=new String[]{maniur,modur};
                        File manifile=File.createTempFile("manifest", ".manifest",getActivity().getExternalCacheDir());
                        File modfile=File.createTempFile("mod",".mod",getActivity().getExternalCacheDir());
                        File[] tgt=new File[]{manifile,modfile};
                        Downloader d=new Downloader(getActivity(),url,tgt);
                        d.execute();
                    }
                    catch (IOException e)
                    {MainActivity.showERROR(e);
                    }
                    // TODO: Implement this method
                }
            };
            
           
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int p2)
        {
            View vv=mli.inflate(R.layout.opt,null);
            TextView name=(TextView)vv.findViewById(R.id.optName);
            name.setText(mname[p2]);
            TextView info=(TextView)vv.findViewById(R.id.optInfo);
            info.setText(minfo[p2]);
            TextView mker=(TextView)vv.findViewById(R.id.optMaker);
            mker.setText(mmker[p2]);
            Button btn=(Button)vv.findViewById(R.id.optButton);
            btn.setTag(p2);
            btn.setOnClickListener(listener);
            TheViewHolder v=new TheViewHolder(vv);
            // TODO: Implement this method
            return v;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder p1, int p2)
        {
            // TODO: Implement this method
        }

        @Override
        public int getItemCount()
        {
            // TODO: Implement this method
            return mname.length;
        }

         
     }
     static class TheViewHolder extends RecyclerView.ViewHolder{
         public TheViewHolder(View v){
         super(v);
         }
     }
}
