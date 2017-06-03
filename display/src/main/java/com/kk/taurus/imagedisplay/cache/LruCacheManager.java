package com.kk.taurus.imagedisplay.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.kk.taurus.imagedisplay.config.SettingBuilder;

/**
 * Created by Taurus on 2017/6/2.
 */

public class LruCacheManager {

    private LruCache<String,Bitmap> mLruCache;

    public LruCacheManager(SettingBuilder settingBuilder){
        mLruCache = new LruCache<String, Bitmap>(settingBuilder.getMaxMemoryCacheSize()){
            @Override
            protected int sizeOf(String key, Bitmap value){
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public Bitmap putCache(String key,Bitmap bitmap){
        Bitmap bitmapValue = getCache(key);
        if(bitmapValue==null){
            if(mLruCache!=null&&bitmap!=null)
                bitmapValue= mLruCache.put(key, bitmap);
        }
        return bitmapValue;
    }

    public Bitmap getCache(String key){
        if(mLruCache!=null){
            return mLruCache.get(key);
        }
        return null;
    }

    public void deleteCache(){
        if(mLruCache!=null)
            mLruCache.evictAll();
    }

    public void removeCache(String key){
        if(mLruCache!=null)
            mLruCache.remove(key);
    }

    public int size(){
        int size=0;
        if(mLruCache!=null)
            size+=mLruCache.size();
        return size;
    }

}
