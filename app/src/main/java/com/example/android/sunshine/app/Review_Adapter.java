package com.example.android.sunshine.app;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by MIDO on 21/01/2016.
 */
public class Review_Adapter extends ArrayAdapter<Reviews> {



    public Review_Adapter(Context context) {
        super(context, 0);}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view =  LayoutInflater.from(getContext()).inflate(R.layout.item_movie_reviews, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Reviews review = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.author.setText(review.getAuthor());
        viewHolder.review_content.setText(Html.fromHtml(review.getContent()));

        return view;
    }

    public static class ViewHolder {
        public final TextView author;
        public final TextView review_content;

        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.reviewer_name);
            review_content = (TextView) view.findViewById(R.id.reviewer_content);
        }
    }


}
