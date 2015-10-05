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

public class MentionsTimelineFragment extends TweetsListFragment {
	private static final String LOG_TAG = MentionsTimelineFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "OnCreate()+");
		mProgressListener.onProgressStart();
		if (isNetworkAvailable()) {
		    mTweetFetcher.loadTweets(TWEET_TYPE.MENTIONS, FETCH_MODE.ALL, null, -1, -1);
		} else {
			mTweetFetcher.loadSavedTweets(TWEET_TYPE.MENTIONS.ordinal());
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvTweetsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (isNetworkAvailable()) {
					mTweetFetcher.loadTweets(TWEET_TYPE.MENTIONS, FETCH_MODE.NEW, null, mTweetAdapter.getItem(0).getUid(), -1);
				}
				lvTweetsList.onRefreshComplete();
			}
		});		
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::MentionsTimelineFragment");
		mProgressListener.onProgressStart();
		if (!mTweetAdapter.isEmpty()) {
			mTweetFetcher.loadTweets(TWEET_TYPE.MENTIONS, FETCH_MODE.OLD, null, -1, 
					mTweetAdapter.getItem(mTweetAdapter.getCount()-1).getUid()-1);
		} else {
			mTweetFetcher.loadTweets(TWEET_TYPE.MENTIONS, FETCH_MODE.ALL, null, -1,-1);
		}
	}
	
//	
	public void saveTweets() {
		mTweetFetcher.saveTweets(mTweetAdapter);
	}

}
