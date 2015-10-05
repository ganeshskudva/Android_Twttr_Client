package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import android.text.format.DateUtils;
import android.util.Log;

@Table(name="tweets")
public class Tweet extends Model implements Serializable {
	private static final String LOG_TAG = Tweet.class.getSimpleName();
	public static enum TWEET_TYPE {
		HOME,
		MENTIONS,
		USER
	}
	
	@Column(name="body")
	private String body;
	@Column(name="uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name="createdAt")
	private String createdAt;
	@Column(name="retweetCount")
	private int retweetCount;
	@Column(name="retweeted")
	private boolean retweeted;
	@Column(name="favorited")
	private boolean favorited;
	@Column(name="favoriteCount")
	private int favoriteCount;
	@Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	@Column(name="tweetType")
	private int tweetType;
	
	public static Tweet fromJSON(JSONObject jsonObject, int type) {
		Tweet tweet = new Tweet();
		// Extract json to polupate member variables
		try {
		    tweet.body = jsonObject.getString("text");
		    tweet.uid = jsonObject.getLong("id");
		    tweet.createdAt = jsonObject.getString("created_at");
		    tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		    tweet.favorited = jsonObject.getBoolean("favorited");
		    if (tweet.favorited) {
		    	tweet.favoriteCount = jsonObject.getInt("favorite_count");
		    }
		    tweet.retweetCount = jsonObject.getInt("retweet_count");
		    tweet.retweeted = jsonObject.getBoolean("retweeted");
		    tweet.tweetType = type;
		} catch (JSONException je) {
			Log.e("Error", "JSONException", je);
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray tweetArray, int type) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (int i=0;i<tweetArray.length();++i) {
	    	JSONObject tweetObject = null;
			try {
				tweetObject = (JSONObject) tweetArray.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Tweet tweet = Tweet.fromJSON(tweetObject, type);
	    	if (tweet != null) {
	    	    tweets.add(tweet);
	    	}
	    }
		return tweets;
	}
	
	public static List<Tweet> getAll(int type) {
		return new Select()
        .from(Tweet.class)
        .where("tweetType = ?", type)
        .orderBy("createdAt DESC")
        .execute();
	}
	
	public Tweet() {
		super();
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
	
	public int getRetweetCount() {
		return retweetCount;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}
	
	public int getTweetType() {
		return tweetType;
	}

	public String getRelativeTimeAgo() {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = getRelativeTime(System.currentTimeMillis() - dateMillis);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	  private String getRelativeTime(long millis){
	    if (millis < 1000) {
	    	return "Now";
	    } 
	    long secs = millis / 1000;
	    if(secs < 60){
	      return "Now";
	    }
	    long mins = secs / 60;
	    if(mins < 60){
	      return mins+"m";
	    }
	    long hours = mins / 60;
	    if(hours < 24){
	      return hours+"h";
	    }
	    long days = hours/24;
	    return days+"d";
	  }
	
	@Override
	public String toString() {
		return getBody() + "-" + getUser().getScreenName();
	}
}
