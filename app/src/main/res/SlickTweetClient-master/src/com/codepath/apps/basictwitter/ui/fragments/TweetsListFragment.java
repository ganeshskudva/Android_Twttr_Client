package com.codepath.apps.basictwitter.ui.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TweetDetailsActivity;
import com.codepath.apps.basictwitter.TweetFetcher;
import com.codepath.apps.basictwitter.TweetFetcher.FETCH_MODE;
import com.codepath.apps.basictwitter.TweetFetcher.ResultsCallback;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.EndlessScrollListener;
import com.codepath.apps.basictwitter.utils.Utils;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment implements ResultsCallback {
	private static final String LOG_TAG = TweetsListFragment.class.getSimpleName();
	
	protected PullToRefreshListView lvTweetsList;
	protected TweetArrayAdapter mTweetAdapter;
	
	protected TweetFetcher mTweetFetcher;
	protected ConnectivityManager mConnectivityManager;
	private AtomicBoolean isLoadingTweets = new AtomicBoolean(false);
	protected OnProgressListener mProgressListener;
	
	public interface OnProgressListener {
		public void onProgressStart();
		public void onProgressEnd();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnProgressListener) {
			mProgressListener = (OnProgressListener) activity;
		} else {
	        throw new ClassCastException(activity.toString()
	                + " must implement TweetsListFragment.OnProgressListener");
	    }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "OnCreate()+");
		mTweetAdapter = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
		mTweetFetcher = new TweetFetcher(getActivity(), this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// Assign our view references
		lvTweetsList = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweetsList.setAdapter(mTweetAdapter);
		lvTweetsList.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
			    customLoadMore(page, totalItemsCount);	
			}
		});
		lvTweetsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Log.d(LOG_TAG, "onItemClicked");
				Intent detailIntent = new Intent(getActivity(), TweetDetailsActivity.class);
				detailIntent.putExtra("tweet", mTweetAdapter.getItem(position));
				startActivity(detailIntent);
			}
		});

		// Return the layout view
		return v;
	}
	
	public void customLoadMore(int page, int totalItemsCount) {
		Log.d(LOG_TAG, "customLoadMore::doNothing");
	}
	
	public void insert(Tweet tweet, int index) {
		mTweetFetcher.loadTweets(Tweet.TWEET_TYPE.HOME, FETCH_MODE.NEW, null, mTweetAdapter.getItem(0).getUid(), -1);
		if (isLoadingTweets.compareAndSet(false, true)) {
			try {
				Utils.waitForBooleanFlag(isLoadingTweets, false, 1000);
			} catch (Exception e) {
				Log.e(LOG_TAG, "Interrupted", e);
			}
			mTweetAdapter.insert(tweet, index);
			mTweetAdapter.notifyDataSetChanged();
		}
	}

	
	public void refreshComplete() {
		lvTweetsList.onRefreshComplete();
	}
	
	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) 
				getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mTweetFetcher.saveTweets(mTweetAdapter);
	}

	@Override
	public void onResults(List<Tweet> tweets, FETCH_MODE mode) {
		switch(mode) {
		case ALL : 
			mTweetAdapter.clear();
			mTweetAdapter.addAll(tweets);
			break;
		case NEW :
			List<Tweet> newTweets = new ArrayList<Tweet>(mTweetAdapter.getCount() + tweets.size());
			newTweets.addAll(tweets);
			for (int i=0;i<mTweetAdapter.getCount();++i) {
				newTweets.add(mTweetAdapter.getItem(i));
			}
			mTweetAdapter.clear();
			mTweetAdapter.addAll(newTweets);
			break;
		case OLD :
			mTweetAdapter.addAll(tweets);
			break;
	    default :
		}
		isLoadingTweets.compareAndSet(true, false);
		mProgressListener.onProgressEnd();
	}
	
	@Override
	public void onFailure(String message) {
		Log.e(LOG_TAG, message);
		isLoadingTweets.compareAndSet(true, false);
		mProgressListener.onProgressEnd();
	}
}
