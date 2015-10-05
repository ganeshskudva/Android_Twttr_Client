package com.codepath.apps.TwttrClient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwttrClient.models.TwttrModel;
import com.codepath.apps.restclienttemplate.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

public class TwttrDetailActivity extends AppCompatActivity {

    private ImageView TweetProfile_pic;
    private TextView  TweetUsrname;
    private TextView  TweetScreenName;
    private TextView  TweetText;
    private TextView  TweetTime;
    private TextView  RetweetValue;
    private TextView  TweetFavValue;
    private TwttrModel item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twttr_detail);

        Bundle extras = getIntent().getExtras();
        if (null == extras)
        {
            return;
        }

        TweetProfile_pic = (ImageView)findViewById(R.id.ivTweetProfile_pic);
        TweetUsrname = (TextView) findViewById(R.id.tvTweetUsrname);
        TweetScreenName = (TextView) findViewById(R.id.tvTweetScreenName);
        TweetText = (TextView) findViewById(R.id.tvTweetText);
        TweetTime = (TextView) findViewById(R.id.tvTweetTime);
        RetweetValue = (TextView) findViewById(R.id.tvRetweetValue);
        TweetFavValue = (TextView) findViewById(R.id.tvTweetFav);

        item = (TwttrModel)extras.getSerializable("Item");
        TweetProfile_pic.setImageResource(0);
        Picasso.with(getApplicationContext())
                .load(item.getImg_url())
                .fit()
                .into(TweetProfile_pic);

        TweetUsrname.setText(item.getUser());
        TweetScreenName.setText("@" + item.getScreen_name());
        TweetText.setText(Html.fromHtml(item.getText()));
        TweetTime.setText(getLocalTimestamp(item.getCreated_at()));
        RetweetValue.setText(Integer.toString(item.getRetweet_count()) + " RETWEET");
        TweetFavValue.setText(Integer.toString(item.getFav_count()) + " FAVORITE");

    }

    public String getLocalTimestamp(String UTCtimestamp)
    {
        String localTime = "";
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(twitterFormat);
            DateTime dt = formatter.parseDateTime(UTCtimestamp);
            DateTimeZone ActualZone = dt.getZone();
            TimeZone tz2 = TimeZone.getDefault();
            DateTime localdt = new DateTime(dt, DateTimeZone.forID(tz2.getID()));
            Log.d("Ganesh", "Time: " + localdt.toLocalTime().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return localTime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twttr_detail, menu);
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
}
