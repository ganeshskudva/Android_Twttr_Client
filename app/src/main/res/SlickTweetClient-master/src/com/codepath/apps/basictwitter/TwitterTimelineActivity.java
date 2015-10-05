package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.ui.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.ui.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.ui.fragments.TweetsListFragment.OnProgressListener;

public class TwitterTimelineActivity extends Activity implements OnProgressListener {
	private static final String LOG_TAG = TwitterTimelineActivity.class.getSimpleName();
	private static final String HOME_TAB_TAG = "HomeTimelineFragment";
	private static final String MENTIONS_TAB_TAG = "MentionsTimelineFragment";
	
	private Tab mHomeTab;
	private Tab mMentionsTab;
	private ProgressBar pbHomeLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate()+");
		setContentView(R.layout.activity_twitter_timeline);
		pbHomeLoading = (ProgressBar) findViewById(R.id.pbHomeLoading);
		int currentNavigationIndex = 0;
		if (savedInstanceState != null) {
			currentNavigationIndex = savedInstanceState.getInt("currentTabIndex");
			Log.d(LOG_TAG, "Current Selected Tab Index:" + currentNavigationIndex);
			Log.d(LOG_TAG, "Number of tabs in action bar:" + getActionBar().getTabCount());
		}
		setupTabs(currentNavigationIndex);
	}
	
	private void setupTabs(int selectedTabIndex) {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        if (mHomeTab == null) {
        	mHomeTab = actionBar
        			.newTab()
        			.setText("Home")
        			.setIcon(R.drawable.ic_home)
        			.setTag(HOME_TAB_TAG)
        			.setTabListener(
        					new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
        							HomeTimelineFragment.class));
        }

        if (mMentionsTab == null) {
        	mMentionsTab = actionBar
        			.newTab()
        			.setText("Mentions")
        			.setIcon(R.drawable.ic_mentions)
        			.setTag(MENTIONS_TAB_TAG)
        			.setTabListener(
        					new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
        							MentionsTimelineFragment.class));	
        }

        actionBar.addTab(mHomeTab);
        
        actionBar.addTab(mMentionsTab);
        
        if (selectedTabIndex == 0) {
            actionBar.selectTab(mHomeTab);
        } else {
        	actionBar.selectTab(mMentionsTab);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.twitter_timeline, menu);
		return true;
	}

	public void onComposeAction(MenuItem mv) {
		Intent intent = new Intent(this, TweetActivity.class);
		startActivityForResult(intent, 40);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 40 && resultCode == RESULT_OK) {
//			Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
//			fragmentTweetsList.insert(newTweet, 0);
//		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		fragmentTweetsList.saveTweets();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int currentTabIndex = getActionBar().getSelectedNavigationIndex(); 
		outState.putInt("currentTabIndex", currentTabIndex);
		Log.d(LOG_TAG, "Saving current selected tab index:" + currentTabIndex);
	}

	public void onProfileView(MenuItem menuItem) {
		Log.d(LOG_TAG, "Profile View Selected");
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("profileType", 0);
		startActivity(i);
	}

	@Override
	public void onProgressStart() {
		pbHomeLoading.setVisibility(ProgressBar.VISIBLE);
		
	}

	@Override
	public void onProgressEnd() {
		pbHomeLoading.setVisibility(ProgressBar.INVISIBLE);
	}
}
