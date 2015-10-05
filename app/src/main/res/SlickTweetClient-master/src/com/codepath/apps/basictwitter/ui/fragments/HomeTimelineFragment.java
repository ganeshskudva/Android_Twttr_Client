package com.codepath.apps.basictwitter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.TweetFetcher;
import com.codepath.apps.basictwitter.TweetFetcher.FETCH_MODE;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.Tweet.TWEET_TYPE;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = HomeTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "OnCreate()+");
		mProgressListener.onProgressStart();
		if (isNetworkAvailable()) {
		    mTweetFetcher.loadTweets(TWEET_TYPE.HOME, FETCH_MODE.ALL, null, -1, -1);
		} else {
			mTweetFetcher.loadSavedTweets(TWEET_TYPE.HOME.ordinal());
		}
//		mTweetFetcher.loadNewTweets(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvTweetsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadTweets(Tweet.TWEET_TYPE.HOME, FETCH_MODE.NEW, null, mTweetAdapter.getItem(0).getUid(), -1);
				}
				lvTweetsList.onRefreshComplete();
			}
		});	
//		if (!isNetworkAvailable()) {
//			mTweetFetcher.loadSavedTweets(Tweet.TWEET_TYPE.HOME.ordinal());
//		}
		
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::HomeTimelineFragment");
		mProgressListener.onProgressStart();
		if (!mTweetAdapter.isEmpty()) {
			mTweetFetcher.loadTweets(TWEET_TYPE.HOME, FETCH_MODE.OLD, null, -1, 
					mTweetAdapter.getItem(mTweetAdapter.getCount()-1).getUid()-1);
		} else {
			mTweetFetcher.loadTweets(TWEET_TYPE.HOME, FETCH_MODE.ALL, null, -1,-1);
		}
	}
}
