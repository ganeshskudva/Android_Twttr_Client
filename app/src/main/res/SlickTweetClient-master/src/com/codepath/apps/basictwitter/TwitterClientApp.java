package com.codepath.apps.basictwitter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class TwitterClientApp extends com.activeandroid.app.Application {
	private static final String LOG_TAG = TwitterClientApp.class.getSimpleName();
	private static Context context;
	
	private TwitterClientApp() {
		
	}
	
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate()+");
        TwitterClientApp.context = this;
        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
    }
    
    public static TwitterRestClient getRestClient() {
    	return (TwitterRestClient) TwitterRestClient.getInstance(TwitterRestClient.class, TwitterClientApp.context);
    }
}