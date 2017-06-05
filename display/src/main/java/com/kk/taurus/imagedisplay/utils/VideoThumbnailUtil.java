package com.kk.taurus.imagedisplay.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2017/5/22.
 */

public class VideoThumbnailUtil {

    /**
     * get video thumb from path
     * @param path
     * @return
     */
    public static Bitmap getVideoThumb(String path, Map<String, String> headers) {
        if(Util.isLocalFile(path)){
            return getVideoThumb(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        }
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        if(headers==null){
            headers = new HashMap<>();
        }
        media.setDataSource(path,headers);
        return media.getFrameAtTime();
    }

    /**
     * get thumb from system ThumbnailUtils
     * @param path
     * @param kind such as MINI_KIND、MICRO_KIND、FULL_SCREEN_KIND.
     * @return
     */
    public static Bitmap getVideoThumb(String path, int kind) {
        if(!Util.isLocalFile(path)){
            return getVideoThumb(path,null);
        }
        return ThumbnailUtils.createVideoThumbnail(path, kind);
    }

}
