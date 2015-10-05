package com.codepath.apps.basictwitter;

import org.apache.http.client.protocol.RequestAddCookies;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.app.Application;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.util.Log;

import com.codepath.apps.basictwitter.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	private static final String LOG_TAG = TwitterRestClient.class.getSimpleName();
	
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "NmsWHTu6OpKJjUf42qnMCkETB";       // Change this
    public static final String REST_CONSUMER_SECRET = "NauJLX8eUiumZGhCpxuI4ZW42RUYdPorQluYHXnZ1Mj6xHQ9F2"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasicTweets"; // Change this (here and in manifest)
    
	private int mAPIRequestCount;
    
    public TwitterRestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
    	Log.d(LOG_TAG, "getHomeTimeline");
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	Log.d(LOG_TAG, "Api Url:" + apiUrl);
    	if (params != null) {
    	    Log.d(LOG_TAG, "RequestParams:" + params.toString());
    	}
    	++mAPIRequestCount;
    	Log.d(LOG_TAG, "Number of API calls:" + mAPIRequestCount);
    	client.get(apiUrl, params, handler);
    }
    
    public void getMentionsTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
    	Log.d(LOG_TAG, "getMentionsTimeline");
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	Log.d(LOG_TAG, "Api Url:" + apiUrl);
    	if (params != null) {
    	    Log.d(LOG_TAG, "RequestParams:" + params.toString());
    	}
    	++mAPIRequestCount;
    	Log.d(LOG_TAG, "Number of API calls:" + mAPIRequestCount);
    	client.get(apiUrl, params, handler);
    }
    
    public void postTweets(String tweetString, AsyncHttpResponseHandler handler) {
    	Log.d(LOG_TAG, "postTweets");
    	String postTweetApiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweetString);
    	++mAPIRequestCount;
    	client.post(postTweetApiUrl, params, handler);
    }
    
    public void getCurrentUser(AsyncHttpResponseHandler handler) {
    	Log.d(LOG_TAG, "getCurrentUser");
    	String currentUserApiUrl = getApiUrl("account/verify_credentials.json");
    	RequestParams params = new RequestParams();
    	params.put("skip_status", String.valueOf(true));
    	++mAPIRequestCount;
    	client.get(currentUserApiUrl, handler);
    }
    
    public void getUserTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
    	Log.d(LOG_TAG, "getUserTimeline");
    	String currentUserApiUrl = getApiUrl("statuses/user_timeline.json");
    	Log.d(LOG_TAG, "Api Url:" + currentUserApiUrl);
    	client.get(currentUserApiUrl, params, handler);
    }
    
    public void getUserInfo(AsyncHttpResponseHandler handler, long uid) {
    	Log.d(LOG_TAG, "getUserInfo");
    	String userApiUrl = getApiUrl("users/show.json");
    	RequestParams params = new RequestParams();
    	params.put("user_id", String.valueOf(uid));
    	client.get(userApiUrl, params, handler);
    }
    
    public void getUserInfo(AsyncHttpResponseHandler handler, String screen_name) {
    	Log.d(LOG_TAG, "getUserInfo");
    	String userApiUrl = getApiUrl("users/show.json");
    	RequestParams params = new RequestParams();
    	params.put("screen_name", screen_name);
    	client.get(userApiUrl, params, handler);
    }
    
    public int getApiCount() {
    	return mAPIRequestCount;
	}
    
    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
//    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
//        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
//        // Can specify query string params directly or through RequestParams.
//        RequestParams params = new RequestParams();
//        params.put("format", "json");
//        client.get(apiUrl, params, handler);
//    }
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}