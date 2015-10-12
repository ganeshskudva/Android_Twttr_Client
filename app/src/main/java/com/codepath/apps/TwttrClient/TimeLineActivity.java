package com.codepath.apps.TwttrClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.TwttrClient.adapters.TweetFragmentPagerAdapter;
import com.codepath.apps.TwttrClient.fragments.TweetListFragment;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class TimeLineActivity extends AppCompatActivity implements TweetComposeDialogListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TweetListFragment fragment_time_line;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabLayout;
    TwttrClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = RestApplication.getRestClient();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabLayout.setViewPager(viewPager);

//        getData();
/*        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onComposeTweet(MenuItem mi) {
        Log.d("Ganesh", "onComposeTweet");
        FragmentManager fm = getSupportFragmentManager();
        TweetComposeDialog composeTweetDialog = TweetComposeDialog.newInstance("Compose Tweet");
        composeTweetDialog.show(fm, "fragment_edit_name");


    }

    public void onProfileView (MenuItem mi)
    {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void onProfileView (View view)
    {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        String scrName = (String)view.getTag();
        Log.d("Ganesh", "Screen Name:"+scrName);
        profileIntent.putExtra("screen_name", scrName);
        startActivity(profileIntent);
    }

    @Override
    public void onFinishedComposingTweet(String text) {
        Log.d("Ganesh", "Tweet:" + text);
       client.postTweet(text, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getApplicationContext(), "Tweet posted successfully", Toast.LENGTH_SHORT).show();
    //            getData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(), "Tweet failed Error:" + errorResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.clearAccessToken();
    }

}
