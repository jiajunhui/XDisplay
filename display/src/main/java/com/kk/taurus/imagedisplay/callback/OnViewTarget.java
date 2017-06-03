package com.kk.taurus.imagedisplay.callback;

/**
 * Created by Taurus on 2017/5/31.
 */

public interface OnViewTarget<R> {
    void onLoadStart();
    void onProgress(int progress, int max);
    void onResourceReady(R resource, String tag);
    void onLoadFinish();
}
