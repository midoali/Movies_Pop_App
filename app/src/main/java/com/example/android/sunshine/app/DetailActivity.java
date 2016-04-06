package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
                DetailFragment fragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new DetailFragment() )
                    .commit();
        }
       }



}
        