package com.kk.taurus.imagedisplay.callback;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * Created by Taurus on 2017/5/31.
 */

public abstract class OnImageViewTarget implements OnViewTarget<Bitmap> {

    private ImageView mTarget;

    public OnImageViewTarget(ImageView target){
        this.mTarget = target;
    }

    @Override
    public void onLoadStart() {

    }

    @Override
    public void onProgress(int progress, int max) {

    }

    @Override
    public void onLoadFinish() {

    }

    @Override
    public void onResourceReady(Bitmap resource, String tag, int placeHolder) {
        if(isLegalTag(tag, placeHolder)){
            mTarget.setImageBitmap(resource);
        }
    }

    private boolean isLegalTag(String tag, int placeHolder){
        if(TextUtils.isEmpty(tag) || mTarget==null)
            return false;
        if(mTarget.getTag(placeHolder)==null)
            return false;
        return tag.equals(mTarget.getTag(placeHolder).toString());
    }

}
