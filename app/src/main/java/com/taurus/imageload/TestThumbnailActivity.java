package com.taurus.imageload;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.kk.taurus.imagedisplay.ImageDisplay;
import com.kk.taurus.imagedisplay.entity.ThumbnailType;

/**
 * Created by Taurus on 2017/6/5.
 */

public class TestThumbnailActivity extends Activity {

    private ImageView mThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thumbnail);

        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);

        ImageDisplay.disPlayThumbnail(this,mThumbnail,"http://172.16.216.70:8080/batamu_trans19.mp4"
                ,R.mipmap.ic_launcher, ThumbnailType.VIDEO_FULL_SCREEN_KIND);
    }
}
