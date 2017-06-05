package com.kk.taurus.imagedisplay.load;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kk.taurus.imagedisplay.cache.CacheManager;
import com.kk.taurus.imagedisplay.callback.OnViewTarget;
import com.kk.taurus.imagedisplay.config.SettingBuilder;
import com.kk.taurus.imagedisplay.entity.DisplayTask;
import com.kk.taurus.imagedisplay.net.LoadTask;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Taurus on 2017/5/31.
 */

public class LoadManager {

    private final String TAG = "LoadManager";
    private static LoadManager instance;
    private LoadManager(){}

    private ExecutorService mExecutor;

    private LinkedList<DisplayTask> mQueue = new LinkedList<>();

    private Semaphore mThreadSemaphore;

    public static LoadManager getInstance(){
        if(null==instance){
            synchronized (LoadManager.class){
                if(null==instance){
                    instance = new LoadManager();
                }
            }
        }
        return instance;
    }

    private Handler mThreadHandler;

    public void init(SettingBuilder settingBuilder){
        mExecutor = Executors.newFixedThreadPool(settingBuilder.getCoreThreadCount());
        mThreadSemaphore = new Semaphore(settingBuilder.getCoreThreadCount());
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                mThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try {
                            mThreadSemaphore.acquire();
                            Log.d(TAG,"ThreadSemaphore acquire...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final DisplayTask task = mQueue.pollLast();
                        final OnViewTarget viewTarget = task.getOnViewTarget();
                        if(task!=null){
                            mExecutor.execute(new LoadTask(task, new LoadTask.OnTaskCallBack() {
                                @Override
                                public void onLoadStart() {
                                    if(viewTarget!=null){
                                        viewTarget.onLoadStart();
                                    }
                                }
                                @Override
                                public void onProgress(int progress, int max) {
                                    if(viewTarget!=null){
                                        viewTarget.onProgress(progress, max);
                                    }
                                }
                                @Override
                                public void onLoadFinish() {
                                    if(viewTarget!=null){
                                        viewTarget.onLoadFinish();
                                    }
                                }
                                @Override
                                public void onResourceReady(Bitmap bitmap) {
                                    if(bitmap!=null){
                                        refreshResult(bitmap,task);
                                    }
                                    releaseSemaphore();
                                }
                            }));
                        }else{
                            releaseSemaphore();
                        }
                    }
                };
                if(onThreadHandlerListener!=null){
                    onThreadHandlerListener.onInitFinish();
                }
                Looper.loop();
            }
        }.start();

    }

    private interface OnThreadHandlerListener{
        void onInitFinish();
    }

    private OnThreadHandlerListener onThreadHandlerListener;

    public void setOnThreadHandlerListener(OnThreadHandlerListener onThreadHandlerListener){
        this.onThreadHandlerListener = onThreadHandlerListener;
    }

    public void load(final DisplayTask task){
        Bitmap bitmap = queryFromLruCache(task);
        if(bitmap!=null){
            Log.d(TAG,"from LruCache ...");
            refreshResult(bitmap,task);
        }else{
            settingPlaceHolder(task);
            addTask(task);
        }
    }

    private void settingPlaceHolder(DisplayTask task) {
        if(task.getPlaceHolder()!=-1 && task.getTarget()!=null){
            final View view = task.getTarget();
            if(view instanceof ImageView){
                WeakReference<Context> weakReference = task.getContext();
                if(task.isLegalTag() && weakReference!=null){
                    ImageView imageView = (ImageView) view;
                    imageView.setImageDrawable(weakReference.get().getResources().getDrawable(task.getPlaceHolder()));
                    imageView.refreshDrawableState();
                }
            }
        }
    }

    private synchronized void addTask(DisplayTask task) {
        mQueue.offer(task);
        if(mThreadHandler==null){
            synchronized (LoadManager.class){
                setOnThreadHandlerListener(new OnThreadHandlerListener() {
                    @Override
                    public void onInitFinish() {
                        mThreadHandler.sendEmptyMessage(0);
                    }
                });
            }
        }else{
            mThreadHandler.sendEmptyMessage(0);
        }
    }

    private void refreshResult(final Bitmap bitmap, final DisplayTask task) {
        final OnViewTarget onViewTarget = task.getOnViewTarget();
        if(onViewTarget!=null){
            onViewTarget.onResourceReady(bitmap, task.getTag());
        }else{
            final View view = task.getTarget();
            if(view instanceof ImageView){
                if(task.isLegalTag()){
                    ImageView targetView = ((ImageView)view);
                    Drawable resource = new BitmapDrawable(task.getContext().get().getResources(),bitmap);
                    targetView.setImageDrawable(resource);
                }
            }
        }
    }

    private void releaseSemaphore() {
        mThreadSemaphore.release();
        Log.d(TAG,"***ThreadSemaphore release...");
    }

    private Bitmap queryFromLruCache(DisplayTask task) {
        return CacheManager.getInstance().getLruCache(task.getUrl());
    }

}
