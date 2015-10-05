package com.codepath.apps.basictwitter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.Tweet.TWEET_TYPE;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TweetFetcher extends JsonHttpResponseHandler {
	private static final String LOG_TAG = TweetFetcher.class.getSimpleName();
	public static final int MAX_COUNT = 25;

	private TwitterRestClient mTwitterClient;
	private Context mContext;
	private ResultsCallback mCallback;
	private FETCH_MODE mLastMode;
	private TWEET_TYPE mLastType;
	
	public static enum FETCH_MODE {
		NEW, OLD, ALL
	}
	
	public interface ResultsCallback {
		public void onResults(List<Tweet> tweet, FETCH_MODE mode);
		public void onFailure(String message);
	}

	public TweetFetcher(Context context, ResultsCallback cb) {
		mTwitterClient = TwitterClientApp.getRestClient();
		mContext = context;
		mCallback = cb;
	}

	public synchronized void loadSavedTweets(final int type) {
		Log.d(LOG_TAG, "Loading saved tweets");
		new AsyncTask<Void, Void, List<Tweet>>() {

			@Override
			protected List<Tweet> doInBackground(Void... params) {
				Log.d(LOG_TAG, "Retrieving saved tweets from db");
				return Tweet.getAll(type);	
			}

			@Override
			protected void onPostExecute(List<Tweet> result) {
				Log.d(LOG_TAG, "Adding tweets from db to adapter. Number of tweets loaded:" + result.size());
				if (!result.isEmpty()) {
					mCallback.onResults(result, FETCH_MODE.ALL);
				}
			}
		}.execute();
	}
	
	public synchronized void saveTweets(final TweetArrayAdapter tweetAdapter) {
		Log.d(LOG_TAG, "Saving tweets to db. Number of tweets to save:" + tweetAdapter.getCount());
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				for (int i=0;i<tweetAdapter.getCount();++i) {
					Tweet tweet = tweetAdapter.getItem(i);
					User user = tweet.getUser();
					user.save();
					tweet.save();
				}
				return null;	
			}
		}.execute();
	}
	
	public synchronized void loadTweets(TWEET_TYPE tweetType, FETCH_MODE mode, String screenName, long newestUid, long oldestUid) {
		mLastMode = mode;
		mLastType = tweetType;
		RequestParams params = null;
		if (newestUid > 0) {
			if (params == null) {
				params = new RequestParams();
			}
			params.put("since_id", String.valueOf(newestUid));
		}
		if (oldestUid > 0) {
			if (params == null) {
				params = new RequestParams();
			}
			params.put("max_id", String.valueOf(oldestUid));
		}
		switch(tweetType) {
		case HOME :
			mTwitterClient.getHomeTimeline(params, this);
			break;
		case MENTIONS :
			mTwitterClient.getMentionsTimeline(params, this);
			break;
		case USER :	
			if (screenName != null) {
				if (params == null) {
					params = new RequestParams();
				}
				params.put("screen_name", screenName);
			} 
			mTwitterClient.getUserTimeline(params, this);
		}
	}

	@Override
	public void onFailure(Throwable t, JSONObject errorObject) {
		Log.e(LOG_TAG, "Failure:" + errorObject.toString(), t);
		try {
			JSONArray errorArray = errorObject.getJSONArray("errors");
			String message = errorArray.getJSONObject(0).getString("message");
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			mCallback.onFailure(message);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException", e);
		}
	}

	@Override
	public synchronized void onSuccess(int resultCode, JSONArray timelineArray) {
		if (resultCode != 200) {
			mCallback.onFailure("Error:" + resultCode);
			return;
		}
		
		List<Tweet> tweets = Tweet.fromJSONArray(timelineArray, mLastType.ordinal());
		mCallback.onResults(tweets, mLastMode);
	}
}
