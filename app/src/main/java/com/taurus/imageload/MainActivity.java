package com.taurus.imageload;

import android.support.v4.app.Fragment;

public class MainActivity extends AbsSingleFragmentActivity
{
	@Override
	protected Fragment createFragment()
	{
		return new ListImgsFragment();
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.activity_single_fragment;
	}

}
