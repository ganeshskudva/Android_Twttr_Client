package com.codepath.apps.basictwitter.ui.fragments;

import com.codepath.apps.basictwitter.TweetFetcher.FETCH_MODE;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.Tweet.TWEET_TYPE;

import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentUserTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = CurrentUserTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "OnCreate()+");
		mTweetFetcher.loadTweets(Tweet.TWEET_TYPE.USER, FETCH_MODE.ALL, null, -1, -1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvTweetsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadTweets(TWEET_TYPE.USER, FETCH_MODE.NEW, null, mTweetAdapter.getItem(0).getUid(), -1);
				}
				lvTweetsList.onRefreshComplete();
			}
		});		
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::CurrentUserTimelineFragment");
		if (!mTweetAdapter.isEmpty()) {
			mTweetFetcher.loadTweets(TWEET_TYPE.USER, FETCH_MODE.OLD, null, -1, 
					mTweetAdapter.getItem(mTweetAdapter.getCount()-1).getUid()-1);
		} else {
			mTweetFetcher.loadTweets(TWEET_TYPE.USER, FETCH_MODE.ALL, null, -1,-1);
		}
	}

}
