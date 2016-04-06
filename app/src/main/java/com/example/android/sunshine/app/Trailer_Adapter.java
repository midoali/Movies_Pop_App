package com.example.android.sunshine.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by MIDO on 15/01/2016.
 */

public class Trailer_Adapter extends ArrayAdapter<Trailers> {



    public Trailer_Adapter(Context context) {
        super(context, 0);}



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view =  LayoutInflater.from(getContext()).inflate(R.layout.item_movie_trailer, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

         final   Trailers trailers = getItem(position);

        viewHolder = (ViewHolder) view.getTag();


        viewHolder.trailer_name.setText(trailers.getName());

        return view;
    }

    public static class ViewHolder {
        public final TextView trailer_name;

        public ViewHolder(View view) {
            trailer_name = (TextView) view.findViewById(R.id.trailer_name);
        }
    }

}
