package com.codepath.apps.basictwitter.listeners;

import com.activeandroid.util.Log;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class FragmentTabListener<T extends Fragment> implements TabListener {
	private static final String LOG_TAG = FragmentTabListener.class.getSimpleName();
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;
	private final int mfragmentContainerId;

	// This version defaults to replacing the entire activity content area
	// new FragmentTabListener<SomeFragment>(this, "first", SomeFragment.class))
	public FragmentTabListener(Activity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mfragmentContainerId = android.R.id.content;
	}

	// This version supports specifying the container to replace with fragment content
	// new FragmentTabListener<SomeFragment>(R.id.flContent, this, "first", SomeFragment.class))
	public FragmentTabListener(int fragmentContainerId, Activity activity, 
			String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mfragmentContainerId = fragmentContainerId;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		Log.d(LOG_TAG, "onTabSelected(). Tab - " + tab.getTag());
		FragmentTransaction sft = mActivity.getFragmentManager().beginTransaction();
		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			sft.add(mfragmentContainerId, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			sft.attach(mFragment);
		}
		sft.commit();
	}

	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		Log.d(LOG_TAG, "onTabUnSelected(). Tab - " + tab.getTag());
		FragmentTransaction sft = mActivity.getFragmentManager().beginTransaction();
		if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			sft.detach(mFragment);
		}
		sft.commit();
	}

	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		Log.d(LOG_TAG, "onTabReSelected(). Tab - " + tab.getTag());
		// User selected the already selected tab. Usually do nothing.
	}
}
