package com.codepath.apps.TwttrClient.models;

import java.io.Serializable;

/**
 * Created by gkudva on 10/1/15.
 */
public class TwttrModel implements Serializable{
    private String text;
    private String user;
    private String screen_name;
    private String img_url;
    private int retweet_count;
    private int fav_count;
    private String media_url;

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFav_count() {
        return fav_count;
    }

    public void setFav_count(int fav_count) {
        this.fav_count = fav_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String created_at;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public TwttrModel ()
    {
        this.text = null;
        this.user = null;
    }
    public TwttrModel(String text, String user) {
        this.text = text;
        this.user = user;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
