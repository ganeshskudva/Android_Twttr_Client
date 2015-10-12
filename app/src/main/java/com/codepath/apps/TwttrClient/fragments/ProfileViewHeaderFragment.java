package com.codepath.apps.TwttrClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwttrClient.RestApplication;
import com.codepath.apps.TwttrClient.TwttrClient;
import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by gkudva on 10/11/15.
 */
public class ProfileViewHeaderFragment extends Fragment {
    private ImageView profileImage;
    private TextView  profileUsrname;
    private TextView  profileScreenName;
    private TwttrClient client;
    private String screenName;
    private TwttrModel model;
    private TextView description;
    private TextView followingCount;
    private TextView followersCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view_header, container, false);
        profileImage = (ImageView) view.findViewById(R.id.ivProfile_View_pic);
        profileUsrname = (TextView) view.findViewById(R.id.tvProfile_View_Usrname);
        profileScreenName = (TextView) view.findViewById(R.id.tvProfile_View_ScreenName);
        description = (TextView) view.findViewById(R.id.tvProfileDescription);
        followingCount = (TextView) view.findViewById(R.id.tvProfileFollowingCount);
        followersCount = (TextView) view.findViewById(R.id.tvProfileFollowersCount);

        return view;
    }

    public static ProfileViewHeaderFragment newInstance(String screenName) {
        ProfileViewHeaderFragment fragmentDemo = new ProfileViewHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RestApplication.getRestClient();
        getData();

    }

    public void getData()
    {
        screenName = getArguments().getString("screen_name");

        client.getUserTimeLine(100, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                model = TwttrModel.getProfileHeaderInfrofromJSONArr(response);
                populateViews();
            }
        });
    }

    public void populateViews ()
    {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);

        Picasso.with(getActivity()).load(model.getImg_url()).fit().into(profileImage);
        profileUsrname.setText(model.getUser());
        profileScreenName.setText("@" + model.getScreen_name());
        description.setText(model.getDescription());
        try {
            followersCount.setText(Html.fromHtml("<b>" + String.format("%.2fM", model.getFollowers_count() / 1000000.0) + "</br>") + " FOLLOWERS");
            followingCount.setText(Html.fromHtml("<b>" + String.valueOf(model.getFollowing_count().intValue()) + "</b>") + " FOLLOWING");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
