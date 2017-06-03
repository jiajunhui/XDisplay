package com.kk.taurus.imagedisplay.entity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.kk.taurus.imagedisplay.callback.OnViewTarget;

import java.lang.ref.WeakReference;

/**
 * Created by Taurus on 2017/5/31.
 */

public class DisplayTask {

    private WeakReference<Context> context;
    private ThumbnailType thumbnailType = ThumbnailType.NORMAL;
    private View target;
    private String url;
    private String tag;
    private int placeHolder = -1;
    private OnViewTarget onViewTarget;

    public DisplayTask() {
    }

    public DisplayTask(WeakReference<Context> context, ThumbnailType thumbnailType, View target, String url, int placeHolder) {
        this(context,thumbnailType,target,url,placeHolder,null);
    }

    public DisplayTask(WeakReference<Context> context, ThumbnailType thumbnailType, View target, String url, int placeHolder, OnViewTarget onViewTarget) {
        this.context = context;
        this.thumbnailType = thumbnailType;
        this.target = target;
        this.url = url;
        this.placeHolder = placeHolder;
        this.onViewTarget = onViewTarget;
        //set tag
        this.setTag(url);
        if(target!=null){
            target.setTag(url);
        }
    }

    public WeakReference<Context> getContext() {
        return context;
    }

    public void setContext(WeakReference<Context> context) {
        this.context = context;
    }

    public ThumbnailType getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(ThumbnailType thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public View getTarget() {
        return target;
    }

    public void setTarget(View target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public OnViewTarget getOnViewTarget() {
        return onViewTarget;
    }

    public void setOnViewTarget(OnViewTarget onViewTarget) {
        this.onViewTarget = onViewTarget;
    }

    public boolean isLegalTag(){

        if(target!=null){

        }
        Object viewTag = target.getTag();
        if(TextUtils.isEmpty(tag) || viewTag==null)
            return false;
        return tag.equals(viewTag.toString());
    }
}
