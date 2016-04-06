package com.example.android.sunshine.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MIDO on 15/01/2016.
 */
public class Trailers
{


        private String key;
        private String name;


        public Trailers() {

        }

        public Trailers(JSONObject trailer) throws JSONException {
            this.key = trailer.getString("key");
            this.name = trailer.getString("name");

        }


        public String getKey() { return key; }

        public String getName() { return name; }


    }



