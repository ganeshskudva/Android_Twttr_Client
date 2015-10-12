package com.codepath.apps.TwttrClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.TwttrClient.RestApplication;
import com.codepath.apps.TwttrClient.TwttrClient;
import com.codepath.apps.TwttrClient.adapters.EndlessScrollListener;
import com.codepath.apps.TwttrClient.adapters.TwttrListAdapter;
import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gkudva on 10/10/15.
 */
public class TweetListFragment extends Fragment {

    private ArrayList<TwttrModel> twttrList;
    private TwttrListAdapter adapter;
    public ListView lvTwttTimeLine;
    private TwttrClient client;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        lvTwttTimeLine = (ListView) view.findViewById(R.id.lvTwttrTimeline);
        lvTwttTimeLine.setAdapter(adapter);
        client = RestApplication.getRestClient();
        lvTwttTimeLine.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("Ganesh", "onLoadMore");
                getData();

            }
        });


        //        getData();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
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


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twttrList = new ArrayList<TwttrModel>();
        adapter = new TwttrListAdapter(getActivity(), twttrList);


    }

    public void addAll(ArrayList<TwttrModel> twttrList)
    {
        adapter.clear();
        adapter.addAll(twttrList);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getData()
    {
        client.getHomeTimeline(100, null, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {

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
