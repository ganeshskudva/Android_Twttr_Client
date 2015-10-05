package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.ui.fragments.CurrentUserTimelineFragment;
import com.codepath.apps.basictwitter.ui.fragments.TweetsListFragment.OnProgressListener;
import com.codepath.apps.basictwitter.ui.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends Activity implements OnProgressListener {
	private static final String LOG_TAG = ProfileActivity.class.getSimpleName();
	
	protected TextView tvName;
	protected TextView tvTagline;
	protected TextView tvFollowers;
	protected TextView tvFollowing;
	protected ImageView ivProfileImage;
	protected ProgressBar pbLoading;
	
	private CurrentUserTimelineFragment mCurrentUserTimeline;
	private UserTimelineFragment mUserTimeline;
	private FragmentTransaction mFt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		tvName = (TextView) findViewById(R.id.tvName);
		tvTagline = (TextView) findViewById(R.id.tvTagLine);
		tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
		int profileType = getIntent().getIntExtra("profileType", 0);
		Log.d(LOG_TAG, "Profile Type:" + profileType);
		if (profileType == 0) {
			loadUserTimeline();
		    loadProfileInfo();
		} else {
			String userName = getIntent().getStringExtra("screen_name");
			Log.d(LOG_TAG, "User Name:" + userName);
			loadUserTimeline(userName);
			loadUserInfo(userName);
		}
	}
	
	private void loadUserTimeline(String name) {
		Log.d(LOG_TAG, "loadUserTimeline(). User name:" + name);
		if (mUserTimeline == null) {
			mUserTimeline = new UserTimelineFragment();
		}
		mUserTimeline.setScreenName(name);
		mFt = getFragmentManager().beginTransaction();
		mFt.replace(R.id.flUserTimeline, mUserTimeline);
		mFt.commit();
	}

	private void loadUserTimeline() {
		if (mCurrentUserTimeline == null) {
			mCurrentUserTimeline = new CurrentUserTimelineFragment();
		}
		mFt = getFragmentManager().beginTransaction();
		mFt.replace(R.id.flUserTimeline, mCurrentUserTimeline);
		mFt.commit();
	}

	private void loadUserInfo(String screenName) {
		showProgressBar();
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable t, JSONObject errorMessage) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorMessage);
				hideProgressBar();
			}

			@Override
			public void onSuccess(JSONObject result) {
				User user = User.fromJSON(result);
				Log.d(LOG_TAG, "User profile result:" + result.toString());
				getActionBar().setTitle("@" + user.getScreenName());
				populateProfileHeader(user);
				hideProgressBar();
			}
		}, screenName);
	}

	public void loadProfileInfo() {
		showProgressBar();
		TwitterClientApp.getRestClient().getCurrentUser(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable t, JSONObject errorMessage) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorMessage);
				hideProgressBar();
			}

			@Override
			public void onSuccess(JSONObject result) {
				User user = User.fromJSON(result);
				Log.d(LOG_TAG, "Current User profile result:" + result.toString());
				getActionBar().setTitle("@" + user.getScreenName());
				populateProfileHeader(user);
				hideProgressBar();
			}
		});
	}
	
	private void populateProfileHeader(User user) {
		tvName.setText(user.getName());
		tvFollowers.setText(String.valueOf(user.getFollowersCount()) + " Followers");
		tvFollowing.setText(String.valueOf(user.getFriendsCount()) + " Following");
		tvTagline.setText(user.getTagline());
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
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
	
	public void showProgressBar() {
		pbLoading.setVisibility(ProgressBar.VISIBLE);	
	}
	
    public void hideProgressBar() {
    	pbLoading.setVisibility(ProgressBar.INVISIBLE);	
	}

	@Override
	public void onProgressStart() {
		showProgressBar();
	}

	@Override
	public void onProgressEnd() {
		hideProgressBar();
	}

}
