package com.example.android.sunshine.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by MIDO on 14/12/2015.
 */
public class Movie_Adapter extends ArrayAdapter<Movies> {
    
                public static class View_getter {
                public final ImageView imageView;
                       public View_getter(View view) {
                       imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        }
            }
    
                public Movie_Adapter(Context context) {
                super(context, 0);
            }
    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                View_getter viewHolder;
        
                        if (view == null) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movies, parent, false);
                        viewHolder = new View_getter(view);
                        view.setTag(viewHolder);
                    }
        
                        final Movies movie = getItem(position);
                String image_url = "http://image.tmdb.org/t/p/w185" + movie.getImage();
        
                        viewHolder = (View_getter) view.getTag();

        Picasso.with(getContext())
                                .load(image_url)
                                .into(viewHolder.imageView);
        

                        return view;
           }}