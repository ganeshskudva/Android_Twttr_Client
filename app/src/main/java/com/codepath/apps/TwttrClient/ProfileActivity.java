package com.codepath.apps.TwttrClient;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.TwttrClient.fragments.ProfileViewHeaderFragment;
import com.codepath.apps.TwttrClient.fragments.UserTimeLineFragment;
import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private TwttrClient client;
    private TwttrModel scrName;
    private String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        screenName = getIntent().getStringExtra("screen_name");
        client = RestApplication.getRestClient();

        if (null == screenName) {
            getData();
        }
        else
        {
            getSupportActionBar().setTitle("@" + screenName);
        }

        if (null == savedInstanceState) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            UserTimeLineFragment userFragment = UserTimeLineFragment.newInstance(screenName);
            ProfileViewHeaderFragment userProfileFragment = ProfileViewHeaderFragment.newInstance(screenName);
            ft.replace(R.id.flProfileHeader, userProfileFragment);
            ft.replace(R.id.flProfileBody, userFragment);
            ft.commit();
        }

    }

    public void getData() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                scrName = TwttrModel.getUserInfofromJSONObj(response);
                getSupportActionBar().setTitle("@" + scrName.getScreen_name());
            }
        });
    }

}
