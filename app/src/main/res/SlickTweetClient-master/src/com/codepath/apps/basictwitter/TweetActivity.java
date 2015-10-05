package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class TweetActivity extends Activity {
	private static final String LOG_TAG = TweetActivity.class.getSimpleName();
	
	private TwitterRestClient mClient;
	
	private TextView tvUserName;
	private TextView tvUserScreenName;
	private EditText etUserStatus;
	private ImageView ivUserImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
		etUserStatus = (EditText) findViewById(R.id.etUserStatus);
		etUserStatus.setBackgroundDrawable(null);
		mClient = TwitterClientApp.getRestClient();
		mClient.getCurrentUser(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.e(LOG_TAG, "onFailure");
			}

			@Override
			public void onSuccess(JSONObject result) {
				Log.d(LOG_TAG, "onSuccess");
				User currentUser = User.fromJSON(result);
				ImageLoader imageLoader = ImageLoader.getInstance();
			    imageLoader.displayImage(currentUser.getProfileImageUrl(), ivUserImage);
			    tvUserName.setText(currentUser.getName());
			    tvUserScreenName.setText("@" + currentUser.getScreenName());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
		final MenuItem tweetCharCountMenu = menu.findItem(R.id.tweet_chars);
        etUserStatus.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				String tweetCharCount = String.valueOf(140-(text.length()));
				tweetCharCountMenu.setTitle(tweetCharCount);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		return true;
	}
	
	public void postTweet(MenuItem mView) {
		String tweetString = etUserStatus.getText().toString();
		mClient.postTweets(tweetString, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, JSONObject response) {
				Log.e(LOG_TAG, "onFailure:" + response.toString(), e);
			}

			@Override
			public void onSuccess(JSONObject response) {
				Log.d(LOG_TAG, "onSuccess:" + response.toString());
				Tweet newTweet = Tweet.fromJSON(response, Tweet.TWEET_TYPE.HOME.ordinal());
				Intent data = new Intent();
				data.putExtra("tweet", newTweet);
				setResult(RESULT_OK, data);
				finish();
			}
			
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	      // Respond to the action bar's Up/Home button
	      case android.R.id.home:
	        Intent intent = NavUtils.getParentActivityIntent(this); 
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
	        NavUtils.navigateUpTo(this, intent);
	        return true;        
	    }

	    return super.onOptionsItemSelected(item);
	}
}
