package com.taurus.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kk.taurus.imagedisplay.ImageDisplay;
import com.kk.taurus.imagedisplay.callback.OnImageViewTarget;
import com.kk.taurus.imagedisplay.config.SettingBuilder;
import com.kk.taurus.imagedisplay.constant.DisPlayConstant;

import java.io.File;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 * @author zhy
 *
 */
public class ListImgsFragment extends Fragment
{
	private final String TAG = "ListFragment";
	private GridView mGridView;
	private String[] mUrlStrs = Images.imageThumbUrls;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		int maxMemory = (int) Runtime.getRuntime().maxMemory();
//		int cacheMemory = maxMemory / 8;
//		ImageDisplay.createConfig(new SettingBuilder()
//				.setContext(getContext())
//				.setCacheDir(getContext().getExternalCacheDir())
//				.setCoreThreadCount(5)
//				.setDiskCacheValueCount(1)
//				.setMaxMemoryCacheSize(cacheMemory)
//				.setMaxDiskCacheSize(200*1024*1024)
//				.setCacheKeyProvider(new SettingBuilder.DefaultCacheKeyProvider()));
		File dcim = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"DCIM");
		File path = new File(dcim,"test1.jpg");
		ImageDisplay.display(getContext(), null, Images.imageThumbUrls[0], R.mipmap.ic_launcher,new OnImageViewTarget(null){
			@Override
			public void onProgress(int progress, int max) {
				super.onProgress(progress, max);
				Log.d(TAG,"progress = " + progress + " max = " + max);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_list_imgs, container,
				false);
		mGridView = (GridView) view.findViewById(R.id.id_gridview);
		setUpAdapter();
		return view;
	}

	private void setUpAdapter()
	{
		if (getActivity() == null || mGridView == null)
			return;

		if (mUrlStrs != null)
		{
			mGridView.setAdapter(new ListImgItemAdaper(getActivity(), 0,
					mUrlStrs));
		} else
		{
			mGridView.setAdapter(null);
		}

	}

	private class ListImgItemAdaper extends ArrayAdapter<String>
	{

		public ListImgItemAdaper(Context context, int resource, String[] datas)
		{
			super(getActivity(), 0, datas);
			Log.e("TAG", "ListImgItemAdaper");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_fragment_list_imgs, parent, false);
			}
			ImageView imageview = (ImageView) convertView
					.findViewById(R.id.id_img);
//			imageview.setImageResource(R.mipmap.ic_launcher);
			ImageDisplay.display(getContext(),imageview,getItem(position), R.mipmap.ic_launcher);

//			Glide.with(getContext())
//					.load(getItem(position))
//					.centerCrop()
//					.placeholder(R.mipmap.ic_launcher)
//					.crossFade()
//					.into(imageview);

			return convertView;
		}

	}

}
