package com.codepath.apps.TwttrClient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "Tweets")
public class Tweet extends Model {
	// Define database columns and associated fields
	@Column(name = "userId")
	String userId;
	@Column(name = "userHandle")
	String userHandle;
	@Column(name = "timestamp")
	String timestamp;
	@Column(name = "body")
	String body;

	// Make sure to always define this constructor with no arguments
	public Tweet() {
		super();
	}

	// Add a constructor that creates an object from the JSON response
	public Tweet(JSONObject object){
		super();

		try {
			this.userId = object.getString("user_id");
			this.userHandle = object.getString("user_username");
			this.timestamp = object.getString("timestamp");
			this.body = object.getString("body");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = new Tweet(tweetJson);
			tweet.save();
			tweets.add(tweet);
		}

		return tweets;
	}
}
