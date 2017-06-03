package com.kk.taurus.imagedisplay.utils;

import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.kk.taurus.imagedisplay.constant.DisPlayConstant;

import java.lang.reflect.Field;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 * @author zhy
 *
 */
public class ImageSizeUtil
{

	public static int caculateInSampleSize(Options options, int reqWidth,
                                           int reqHeight)
	{
		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;

		if (width > reqWidth || height > reqHeight)
		{
			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);

			inSampleSize = Math.max(widthRadio, heightRadio);
		}

		return inSampleSize;
	}

	/**
	 * 根据ImageView获适当的压缩的宽和高
	 * 
	 * @param view
	 * @return
	 */
	public static ImageSize getImageViewSize(View view)
	{

		int width = DisPlayConstant.widthPixels;
		int height = DisPlayConstant.heightPixels;
		ImageSize imageSize = new ImageSize();
		if(view!=null){
			DisplayMetrics displayMetrics = view.getContext().getResources()
					.getDisplayMetrics();


			LayoutParams lp = view.getLayoutParams();

			width = view.getWidth();// 获取imageview的实际宽度
			if (width <= 0)
			{
				width = lp.width;// 获取imageview在layout中声明的宽度
			}
			if (width <= 0)
			{
				//width = imageView.getMaxWidth();// 检查最大值
				width = getImageViewFieldValue(view, "mMaxWidth");
			}
			if (width <= 0)
			{
				width = displayMetrics.widthPixels;
			}

			height = view.getHeight();// 获取imageview的实际高度
			if (height <= 0)
			{
				height = lp.height;// 获取imageview在layout中声明的宽度
			}
			if (height <= 0)
			{
				height = getImageViewFieldValue(view, "mMaxHeight");// 检查最大值
			}
			if (height <= 0)
			{
				height = displayMetrics.heightPixels;
			}
		}

		imageSize.width = width;
		imageSize.height = height;

		return imageSize;
	}

	public static class ImageSize
	{
		public int width;
		public int height;
	}
	
	/**
	 * 通过反射获取imageview的某个属性值
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		} catch (Exception e)
		{
		}
		return value;

	}

	
}
