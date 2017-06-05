package com.kk.taurus.imagedisplay.net;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.kk.taurus.imagedisplay.cache.CacheManager;
import com.kk.taurus.imagedisplay.entity.DisplayTask;
import com.kk.taurus.imagedisplay.entity.ThumbnailType;
import com.kk.taurus.imagedisplay.utils.AudioCoverUtil;
import com.kk.taurus.imagedisplay.utils.DownloadUtil;
import com.kk.taurus.imagedisplay.utils.Util;
import com.kk.taurus.imagedisplay.utils.VideoThumbnailUtil;

/**
 * Created by Taurus on 2017/5/31.
 */

public class LoadTask extends Thread {

    private DisplayTask mTask;
    private OnTaskCallBack mCallBack;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public LoadTask(DisplayTask displayTask,OnTaskCallBack callBack){
        this.mTask = displayTask;
        this.mCallBack = callBack;
    }

    @Override
    public void run() {
        super.run();
        try {
            if (!isInterrupted()){
                invokeLoad();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void invokeLoad() throws InterruptedException{
        String key = mTask.getUrl();
        final LoadResult result = new LoadResult();
        //query from disk cache
        result.bitmap = CacheManager.getInstance().getDiskCache(key);
        try {
            boolean needDiskCache = true;
            if(result.bitmap==null){

                if(mTask.getThumbnailType()!=ThumbnailType.NORMAL){
                    //load thumbnail by type
                    ThumbnailType thumbnailType = mTask.getThumbnailType();
                    switch (thumbnailType){
                        case VIDEO_MINI_KIND:
                        case VIDEO_MICRO_KIND:
                        case VIDEO_FULL_SCREEN_KIND:
                            result.bitmap = VideoThumbnailUtil.getVideoThumb(key, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                            break;
                        case AUDIO:
                            result.bitmap = AudioCoverUtil.createAlbumArt(key);
                            break;
                        case APK:
                            if(mTask.getContext()!=null){
                                result.bitmap = Util.getApkIconThumbnail(mTask.getContext().get(),key);
                            }
                            break;
                    }
                }else{
                    //check is local file
                    if(Util.isLocalFile(key)){
                        needDiskCache = false;
                        //get bitmap by local image
                        result.bitmap = Util.loadImageFromLocal(key,mTask.getTarget());
                    }
                }

                if(result.bitmap==null){
                    //download from network
                    result.bitmap = DownloadUtil.downloadBitmap(key, new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onStart() {
                            callBackOnStart();
                        }
                        @Override
                        public void onProgress(int progress, int max) {
                            callBackOnProgress(progress, max);
                        }
                        @Override
                        public void onFinish() {
                            callBackOnFinish();
                        }
                    });
                }
            }
            callBackOnResourceReady(result.bitmap);
            if(result.bitmap!=null){
                //put lru cache
                CacheManager.getInstance().putLruCache(key,result.bitmap);
                if(needDiskCache){
                    //put disk cache
                    CacheManager.getInstance().putDiskCache(key,result.bitmap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callBackOnStart(){
        if(mCallBack!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onLoadStart();
                }
            });
        }
    }

    private void callBackOnProgress(final int progress , final int max){
        if(mCallBack!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onProgress(progress, max);
                }
            });
        }
    }

    private void callBackOnFinish(){
        if(mCallBack!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onLoadFinish();
                }
            });
        }
    }

    private void callBackOnResourceReady(final Bitmap bitmap){
        if(mCallBack!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onResourceReady(bitmap);
                }
            });
        }
    }

    private static class LoadResult{
        public Bitmap bitmap;
    }

    public interface OnTaskCallBack{
        void onResourceReady(Bitmap bitmap);
        void onLoadStart();
        void onProgress(int progress, int max);
        void onLoadFinish();
    }

}
