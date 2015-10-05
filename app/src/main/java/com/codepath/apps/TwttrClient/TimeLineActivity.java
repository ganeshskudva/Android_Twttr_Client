package com.codepath.apps.TwttrClient;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.TwttrClient.adapters.EndlessScrollListener;
import com.codepath.apps.TwttrClient.adapters.TwttrListAdapter;
import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeLineActivity extends AppCompatActivity implements TweetComposeDialogListener {
    private ArrayList<TwttrModel> twttrList;
    private TwttrListAdapter adapter;
    private ListView lvTwttTimeLine;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TwttrClient client;
    private String max_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        twttrList = new ArrayList<TwttrModel>();
        adapter = new TwttrListAdapter(this, twttrList);
        lvTwttTimeLine = (ListView) findViewById(R.id.lvTwttrTimeline);
        lvTwttTimeLine.setAdapter(adapter);
        client = RestApplication.getRestClient();
        max_id = null;

        lvTwttTimeLine.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("Ganesh", "onLoadMore");
                getData();

            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void getData()
    {
        client.getHomeTimeline(100, max_id, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("Ganesh", "onSuccess: ");
                adapter.clear();
                adapter.addAll();
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        TwttrModel model = new TwttrModel();
                        model.setText(obj.getString("text").toString());
                        model.setCreated_at(obj.getString("created_at"));
                        model.setUser(obj.getJSONObject("user").getString("name").toString());
                        model.setScreen_name(obj.getJSONObject("user").getString("screen_name").toString());
                        model.setImg_url(obj.getJSONObject("user").getString("profile_image_url_https").toString());
                        model.setRetweet_count(obj.getInt("retweet_count"));
                        model.setFav_count(obj.getInt("favorite_count"));

                        twttrList.add(model);
                    }

                    max_id = jsonArray.getJSONObject(jsonArray.length() - 1).getString("id_str");
                    Log.d("Ganesh", "max_id: " + max_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Ganesh", "Error: " + errorResponse.toString());
            }
        });

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

    public void onComposeTweet(MenuItem mi)
    {
        Log.d("Ganesh", "onComposeTweet");
        FragmentManager fm = getSupportFragmentManager();
        TweetComposeDialog composeTweetDialog = TweetComposeDialog.newInstance("Compose Tweet");
        composeTweetDialog.show(fm, "fragment_edit_name");


    }

    @Override
    public void onFinishedComposingTweet(String text)
    {
        Log.d("Ganesh", "Tweet:" + text);
        client.postTweet(text, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getApplicationContext(), "Tweet posted successfully", Toast.LENGTH_SHORT).show();
                getData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(), "Tweet failed Error:" + errorResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        client.clearAccessToken();
    }

}
