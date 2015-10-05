package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import android.util.Log;

@Table(name="user")
public class User extends Model implements Serializable {
	private static final String LOG_TAG = User.class.getSimpleName();
	
	@Column(name="name")
	private String name;
	@Column(name="uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name="screenName")
	private String screenName;
	@Column(name="profileImageUrl")
	private String profileImageUrl;
	@Column(name="followersCount")
	private int followersCount;
	@Column(name="friendsCount")
	private int friendsCount;
	@Column(name="tagline")
	private String tagline;
	
	public static User fromJSON(JSONObject userObject) {
		User user = new User();
		try {
		user.name = userObject.getString("name");
		user.uid = userObject.getLong("id");
		user.screenName = userObject.getString("screen_name");
		user.profileImageUrl = userObject.getString("profile_image_url");
		user.followersCount = userObject.getInt("followers_count");
		user.friendsCount = userObject.getInt("friends_count");
		user.tagline = userObject.getString("description");
		} catch (JSONException je) {
			Log.e(LOG_TAG, "JSONException", je);
		}
		return user;
	}
	
	public User() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public String getTagline() {
		return tagline;
	}

	public List<Tweet> items() {
        return getMany(Tweet.class, "User");
    }
}
