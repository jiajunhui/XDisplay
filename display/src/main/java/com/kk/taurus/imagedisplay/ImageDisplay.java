package com.kk.taurus.imagedisplay;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.kk.taurus.imagedisplay.cache.CacheManager;
import com.kk.taurus.imagedisplay.callback.OnViewTarget;
import com.kk.taurus.imagedisplay.config.SettingBuilder;
import com.kk.taurus.imagedisplay.constant.DisPlayConstant;
import com.kk.taurus.imagedisplay.entity.DisplayTask;
import com.kk.taurus.imagedisplay.entity.ThumbnailType;
import com.kk.taurus.imagedisplay.load.LoadManager;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Taurus on 2017/5/31.
 */

public class ImageDisplay {

    private static SettingBuilder settingBuilder;

    public static void display(Context context, ImageView view,String url){
        display(context, view, url, -1, null);
    }

    public static void display(Context context, ImageView view,String url, int placeHolder){
        display(context, view, url, placeHolder, null);
    }

    public static void disPlayThumbnail(Context context, ImageView view, String path, int placeHolder, ThumbnailType type){
        DisplayTask task = new DisplayTask(new WeakReference<>(context),type,view,path,placeHolder,null);
        display(context,task);
    }

    public static void display(Context context, ImageView view, String url, int placeHolder, OnViewTarget onViewTarget){
        DisplayTask task = new DisplayTask(new WeakReference<>(context),ThumbnailType.NORMAL,view,url,placeHolder,onViewTarget);
        display(context,task);
    }

    private static void display(Context context, DisplayTask task){
        checkSetting(context);
        LoadManager.getInstance().load(task);
    }

    private static void checkSetting(Context context) {
        if(settingBuilder!=null)
            return;
        buildDefaultSetting(context);
    }

    private static void initManager(SettingBuilder settingBuilder){
        CacheManager.getInstance().init(settingBuilder);
        LoadManager.getInstance().init(settingBuilder);
    }

    public static void buildDefaultSetting(Context context){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        DisPlayConstant.widthPixels = displayMetrics.widthPixels;
        DisPlayConstant.heightPixels = displayMetrics.heightPixels;
        settingBuilder = new SettingBuilder()
                .setContext(context)
                .setCacheDir(getDefaultCacheDir(context))
                .setCoreThreadCount(5)
                .setDiskCacheValueCount(1)
                .setMaxMemoryCacheSize(cacheMemory)
                .setMaxDiskCacheSize(200*1024*1024)
                .setCacheKeyProvider(new SettingBuilder.DefaultCacheKeyProvider());
        initManager(settingBuilder);
    }

    private static File getDefaultCacheDir(Context context){
        File dir = context.getCacheDir();
        if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())){
            dir = context.getExternalCacheDir();
        }
        return dir;
    }

    public static void createConfig(SettingBuilder userSetting){
        settingBuilder = userSetting;
        initManager(settingBuilder);
    }

}
