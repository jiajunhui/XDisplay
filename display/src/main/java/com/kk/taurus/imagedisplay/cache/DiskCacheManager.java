package com.kk.taurus.imagedisplay.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kk.taurus.imagedisplay.config.SettingBuilder;
import com.kk.taurus.imagedisplay.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Taurus on 2017/6/2.
 */

public class DiskCacheManager {

    private DiskLruCache mDiskLruCache;
    private SettingBuilder settingBuilder;

    public DiskCacheManager(SettingBuilder settingBuilder){
        this(settingBuilder.getCacheDir(),Util.getAppVersion(settingBuilder.getContext()),settingBuilder.getDiskCacheValueCount(),settingBuilder.getMaxDiskCacheSize());
        this.settingBuilder = settingBuilder;
    }

    private DiskCacheManager(File cacheDirectory, int appVersion,int valueCount, long maxSize){
        try {
            if(!cacheDirectory.exists()){
                cacheDirectory.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDirectory, appVersion, valueCount, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getCacheDirectory(){
        return settingBuilder.getCacheDir();
    }

    private String getCacheKey(String key){
        return settingBuilder.getCacheKeyProvider().getCacheKey(key);
    }

    public boolean putDiskCache(String key, Bitmap bitmap){
        DiskLruCache.Editor editor = null;
        try {
            String md5Key = getCacheKey(key);
            if(mDiskLruCache.get(md5Key)!=null){
                return true;
            }
            editor = mDiskLruCache.edit(md5Key);
            if(editor!=null){
                OutputStream outputStream= editor.newOutputStream(0);
                Util.Bitmap2OutputStream(bitmap,outputStream);
                if(outputStream!=null){
                    editor.commit();
                    return true;
                }
                else {
                    editor.abort();
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            if(editor!=null){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getDiskCache(String key){
        String md5Key = getCacheKey(key);
        Bitmap bitmap=null;
        try {
            if(mDiskLruCache!=null){
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(md5Key);
                if(snapshot!=null){
                    bitmap= BitmapFactory.decodeStream(snapshot.getInputStream(0)) ;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void deleteDiskCache(){
        try {
            if(mDiskLruCache!=null){
                mDiskLruCache.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeDiskCache(String key){
        if(mDiskLruCache!=null){
            try {
                mDiskLruCache.remove(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int size(){
        int size=0;
        if(mDiskLruCache!=null){
            size=(int) mDiskLruCache.size();
        }
        return size;
    }

    public void close(){
        if(mDiskLruCache!=null){
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
