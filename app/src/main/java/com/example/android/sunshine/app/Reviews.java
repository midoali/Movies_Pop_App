package com.example.android.sunshine.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MIDO on 21/01/2016.
 */
public class Reviews {

    private  String id ;
    private  String author;
    private  String content;

    public Reviews(){}

    public  Reviews(JSONObject review) throws JSONException {
        this.id=review.getString("id");
        this.author=review.getString("author");
        this.content=review.getString("content");
    }


    public  String getId(){return  id;}
    public String getAuthor (){return  author;}
    public  String getContent (){return  content;}


}
