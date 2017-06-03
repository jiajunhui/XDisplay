package com.kk.taurus.imagedisplay.config;

import android.content.Context;

import com.kk.taurus.imagedisplay.utils.Util;

import java.io.File;

/**
 * Created by Taurus on 2017/6/2.
 */

public class SettingBuilder {

    public static final int DEFAULT_CORE_THREAD_COUNT = 5;
    public static final int DEFAULT_MAX_THREAD_COUNT = 20;

    public static final int DEFAULT_DISK_CACHE_VALUE_COUNT = 1;

    public static final long DEFAULT_MAX_CACHE_SIZE = 200*1024*1024;

    private Context context;
    private File cacheDir;
    private int defaultPlaceHolder;
    private int diskCacheValueCount = DEFAULT_DISK_CACHE_VALUE_COUNT;
    private int maxMemoryCacheSize;
    private long maxDiskCacheSize = DEFAULT_MAX_CACHE_SIZE;
    private int coreThreadCount = DEFAULT_CORE_THREAD_COUNT;
    private int maxThreadCount = DEFAULT_MAX_THREAD_COUNT;
    private CacheKeyProvider cacheKeyProvider;

    public Context getContext() {
        return context;
    }

    public SettingBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public SettingBuilder setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
        return this;
    }

    public int getDefaultPlaceHolder() {
        return defaultPlaceHolder;
    }

    public SettingBuilder setDefaultPlaceHolder(int defaultPlaceHolder) {
        this.defaultPlaceHolder = defaultPlaceHolder;
        return this;
    }

    public int getDiskCacheValueCount() {
        return diskCacheValueCount;
    }

    public SettingBuilder setDiskCacheValueCount(int diskCacheValueCount) {
        this.diskCacheValueCount = diskCacheValueCount;
        return this;
    }

    public int getMaxMemoryCacheSize() {
        return maxMemoryCacheSize;
    }

    public SettingBuilder setMaxMemoryCacheSize(int maxMemoryCacheSize) {
        this.maxMemoryCacheSize = maxMemoryCacheSize;
        return this;
    }

    public long getMaxDiskCacheSize() {
        return maxDiskCacheSize;
    }

    public SettingBuilder setMaxDiskCacheSize(long maxDiskCacheSize) {
        this.maxDiskCacheSize = maxDiskCacheSize;
        return this;
    }

    public int getCoreThreadCount() {
        return coreThreadCount;
    }

    public SettingBuilder setCoreThreadCount(int coreThreadCount) {
        this.coreThreadCount = coreThreadCount;
        return this;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    public SettingBuilder setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
        return this;
    }

    public CacheKeyProvider getCacheKeyProvider() {
        return cacheKeyProvider;
    }

    public SettingBuilder setCacheKeyProvider(CacheKeyProvider cacheKeyProvider) {
        this.cacheKeyProvider = cacheKeyProvider;
        return this;
    }

    public static class DefaultCacheKeyProvider implements CacheKeyProvider {
        @Override
        public String getCacheKey(String url) {
            return Util.md5(url);
        }
    }

    public interface CacheKeyProvider {
        String getCacheKey(String url);
    }

}
