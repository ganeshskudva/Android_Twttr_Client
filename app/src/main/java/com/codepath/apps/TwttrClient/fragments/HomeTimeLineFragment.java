package com.codepath.apps.TwttrClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.TwttrClient.RestApplication;
import com.codepath.apps.TwttrClient.TwttrClient;
import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gkudva on 10/10/15.
 */
public class HomeTimeLineFragment extends TweetListFragment{
    private TwttrClient client;
    private String max_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RestApplication.getRestClient();
        max_id = null;
        getData();
/*        lvTwttTimeLine.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("Ganesh", "onLoadMore");
                getData();

            }
        });
*/
    }

    public void getData()
    {
        client.getHomeTimeline(100, max_id, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("Ganesh", "onSuccess: ");
                //max_id = jsonArray.getJSONObject(jsonArray.length() - 1).getString("id_str");

                //adapter.notifyDataSetChanged();
                //swipeRefreshLayout.setRefreshing(false);
                addAll(TwttrModel.fromJSONArr(jsonArray));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Ganesh", "Error: " + errorResponse.toString());
            }
        });

    }

}
