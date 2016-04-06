package com.example.android.sunshine.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MIDO on 14/12/2015.
 */
public class Movies {

    private int id;
    private String title;
    private String image;
    private String overview;
    private double rating;
    private String date;

    public Movies() {

    }

    public  Movies(JSONObject movie)throws JSONException {
        this.id=movie.getInt("id");
        this.title=movie.getString("original_title");
        this.image=movie.getString("poster_path");
        this.overview= movie.getString("overview");
        this.rating =    movie.getDouble("vote_average");
        this.date=  movie.getString("release_date");


    }
    public Movies(int id, String title, String image, String overview, int rating, String date) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }





}