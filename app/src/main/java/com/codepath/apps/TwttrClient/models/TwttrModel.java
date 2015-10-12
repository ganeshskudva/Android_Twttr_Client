package com.codepath.apps.TwttrClient.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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
    private String id_str;
    private Double followers_count;
    private Double following_count;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Double followers_count) {
        this.followers_count = followers_count;
    }

    public Double getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(Double following_count) {
        this.following_count = following_count;
    }

    public TwttrModel ()
    {
        this.text = null;
        this.user = null;
        this.screen_name = null;
        this.img_url = null;
        this.retweet_count = 0;
        this.fav_count = 0;
        this.media_url = null;
        this.id_str = null;
        this.followers_count = 0d;
        this.following_count = 0d;
        this.description = null;
    }


    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

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

    public static TwttrModel fromJSONObj (JSONObject obj)
    {
        TwttrModel model = new TwttrModel();

        try {
            model.setText(obj.getString("text").toString());
            model.setCreated_at(obj.getString("created_at"));
            model.setUser(obj.getJSONObject("user").getString("name").toString());
            model.setScreen_name(obj.getJSONObject("user").getString("screen_name").toString());
            model.setImg_url(obj.getJSONObject("user").getString("profile_image_url_https").toString());
            model.setRetweet_count(obj.getInt("retweet_count"));
            model.setFav_count(obj.getInt("favorite_count"));
            model.setFollowers_count(obj.getJSONObject("user").getDouble("followers_count"));
            model.setFollowing_count(obj.getJSONObject("user").getDouble("friends_count"));
            model.setDescription(obj.getJSONObject("user").getString("description"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static TwttrModel getUserInfofromJSONObj (JSONObject object)
    {
        TwttrModel model = new TwttrModel();

        try
        {
            model.setScreen_name(object.getString("screen_name"));
            model.setId_str(object.getString("id_str"));
            model.setImg_url(object.getString("profile_image_url_https"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static TwttrModel getProfileHeaderInfrofromJSONArr (JSONArray jsonArray)
    {
        TwttrModel model = new TwttrModel();
        try {
            if (jsonArray.length() > 0) {
                model = fromJSONObj((JSONObject)jsonArray.get(0));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return model;
    }

    public static ArrayList<TwttrModel> fromJSONArr (JSONArray jsonArr)
    {
        ArrayList<TwttrModel> twttrArr = new ArrayList<TwttrModel>();

        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                JSONObject obj = (JSONObject)jsonArr.get(i);
                TwttrModel model = fromJSONObj(obj);

                if (model != null)
                {
                    twttrArr.add(model);
                }

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        return twttrArr;
    }

}
