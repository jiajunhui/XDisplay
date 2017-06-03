package com.kk.taurus.imagedisplay.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

/**
 * Created by Taurus on 2017/5/31.
 */

public class Util {

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * get string md5 message digest
     * @param str
     * @return
     */
    public static String md5(String str){
        return md5(new ByteArrayInputStream(str.getBytes()));
    }

    /**
     * get file md5 message digest.
     * @param file
     * @return
     */
    public static String md5(File file){
        try {
            InputStream inputStream = new FileInputStream(file);
            return md5(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get InputStream md5 message digest.
     * @param inputStream
     * @return
     */
    public static String md5(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        int numRead;
        MessageDigest md5;
        try{
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=inputStream.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            inputStream.close();
            return toHexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static OutputStream Bitmap2OutputStream(Bitmap bm, OutputStream baos) {
        if(bm!=null){
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        return baos;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * from apk file get apk icon bitmap.
     * @param context
     * @param apkPath
     * @return
     */
    public static Bitmap getApkIconThumbnail(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        Drawable drawable = null;
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                drawable = appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        if(drawable!=null){
            return drawableToBitmap(drawable);
        }
        return null;
    }

    /**
     * drawable to bitmap.
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap loadImageFromLocal(String path,View imageView){
        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        // 2、压缩图片
        bm = decodeSampledBitmapFromPath(path, imageSize.width,
                imageSize.height);
        return bm;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int width,int height){
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
                width, height);

        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    public static boolean isLocalFile(String path){
        Uri uri = Uri.parse(path);
        if(uri==null)
            return false;
        String scheme = uri.getScheme();
        if(TextUtils.isEmpty(scheme) || scheme.equalsIgnoreCase("file")){
            File file = new File(path);
            if(file.exists())
                return true;
        }
        return false;
    }

}
