package com.kk.taurus.imagedisplay.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kk.taurus.imagedisplay.cache.CacheManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Taurus on 2017/5/31.
 */

public class DownloadUtil {

    private static final String TAG = "download";

    public static Bitmap downloadBitmap(String imageUrl, OnDownloadListener onDownloadListener) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        File temp = new File(CacheManager.getInstance().getDiskCacheDirectory(),UUID.randomUUID().toString());
        try {
            FileOutputStream outputStream = new FileOutputStream(temp);
            URL url = new URL(imageUrl);
            if(onDownloadListener!=null){
                onDownloadListener.onStart();
            }
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setReadTimeout(10 * 1000);
            int contentLength = con.getContentLength();
            Log.d(TAG,"contentLength = " + contentLength);
            InputStream in = con.getInputStream();
            byte[] buf = new byte[4*1024];
            int len;
            int progress = 0;
            while ((len = in.read(buf))!=-1){
                outputStream.write(buf,0,len);
                outputStream.flush();
                progress += len;
                if(onDownloadListener!=null){
                    onDownloadListener.onProgress(progress,contentLength);
                }
            }
            if(onDownloadListener!=null){
                onDownloadListener.onFinish();
            }
            FileInputStream tempIn = new FileInputStream(temp);
            bitmap = BitmapFactory.decodeFileDescriptor(tempIn.getFD());
            temp.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return bitmap;
    }

    public interface OnDownloadListener{
        void onStart();
        void onProgress(int progress,int max);
        void onFinish();
    }

}
