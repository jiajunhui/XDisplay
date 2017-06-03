package com.kk.taurus.imagedisplay.entity;

import android.provider.MediaStore;

/**
 * Created by Taurus on 2017/6/2.
 */

public enum  ThumbnailType {

    VIDEO_MINI_KIND(MediaStore.Video.Thumbnails.MINI_KIND),
    VIDEO_MICRO_KIND(MediaStore.Video.Thumbnails.MICRO_KIND),
    VIDEO_FULL_SCREEN_KIND(MediaStore.Video.Thumbnails.FULL_SCREEN_KIND),
    AUDIO(0),
    APK(0),
    NORMAL(0);

    private int type;

    ThumbnailType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
