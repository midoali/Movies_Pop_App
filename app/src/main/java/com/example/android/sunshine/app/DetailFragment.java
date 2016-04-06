package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIDO on 17/12/2015.
 */
public  class DetailFragment extends Fragment {


    private Movies curr_movie;



    private Trailer_Adapter TrailerAdapter;
    private Review_Adapter ReviewAdabter;
    private ImageView PlayImage;
    private ImageView ImageView;
    private TextView Title;
    private TextView Overview;
    private TextView Date;
    private TextView VoteAverage;
    private ListView TrailersView;
    private ListView ReviewsView;
    private ScrollView base_scroll;


    public DetailFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        curr_movie = Util.currentMovie;



        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        base_scroll = (ScrollView) rootView.findViewById(R.id.detail_scroll);

        if (curr_movie != null) {
            base_scroll.setVisibility(View.VISIBLE);
        } else {
            base_scroll.setVisibility(View.INVISIBLE);
        }
        PlayImage = (ImageView) rootView.findViewById(R.id.video);

        final fav_table fav =new fav_table(getActivity());
        final ImageButton button = (ImageButton) rootView.findViewById(R.id.set_pref);
        final ImageButton button2 = (ImageButton) rootView.findViewById(R.id.del_pref);


        button2.setVisibility(View.GONE);

if(curr_movie !=null) {
    int[] a;
    a = fav.getData();
    for (int i = 0; i < a.length; i++) {
        if (curr_movie.getId() == (a[i])) {
            button.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
        }
    }


    button2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            fav.delete(curr_movie.getId());
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    });
    button.setOnClickListener(new View.OnClickListener() {
        //
        public void onClick(View v) {
            int mov_id = curr_movie.getId();
            fav.insert_fav(mov_id);
            getActivity().finish();
          startActivity(getActivity().getIntent());
        }
    });
}



        ImageView = (ImageView) rootView.findViewById(R.id.detail_image);
        Title = (TextView) rootView.findViewById(R.id.detail_title);
        Overview = (TextView) rootView.findViewById(R.id.detail_overview);
        Date = (TextView) rootView.findViewById(R.id.detail_date);
        VoteAverage = (TextView) rootView.findViewById(R.id.detail_vote_average);
        TrailersView = (ListView) rootView.findViewById(R.id.detail_trailers);
        ReviewsView = (ListView) rootView.findViewById (R.id.detail_reviews);
        TrailerAdapter = new Trailer_Adapter(getActivity());

        ReviewAdabter = new Review_Adapter(getActivity());


            TrailersView.setAdapter(TrailerAdapter);
            ReviewsView.setAdapter(ReviewAdabter);
            TrailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Trailers trailer = TrailerAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    startActivity(intent);
                }
            });
if(curr_movie !=null){
            String image_url = "http://image.tmdb.org/t/p/w342" + curr_movie.getImage();
            Picasso.with(getActivity())
                    .load(image_url).into(ImageView);

            Title.setText(curr_movie.getTitle());
            Overview.setText(curr_movie.getOverview());

            String movie_date = curr_movie.getDate();
            Date.setText(movie_date);


            VoteAverage.setText(Double.toString(curr_movie.getRating()));}
         return rootView;
}
    @Override
    public void onStart() {
        super.onStart();
        if (curr_movie != null) {
            new FetchTrailersTask().execute(Integer.toString(curr_movie.getId()));

            new FetchReviewsTask().execute(Integer.toString(curr_movie.getId()));
        }


    }

public  class FetchReviewsTask extends AsyncTask <String,Void,List<Reviews>>{

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private List<Reviews> getReviewsFromJson(String jsonStr) throws JSONException{
        JSONObject reviewJson=new JSONObject(jsonStr);
        JSONArray reviewsArray=reviewJson.getJSONArray("results");
        List<Reviews> results=new ArrayList<>();
                for(int i=0; i<reviewsArray.length();i++){
                    JSONObject review=reviewsArray.getJSONObject(i);
                    Reviews reviewResult=new Reviews(review);
                    results.add(reviewResult);
                }
        return  results;
    }
    @Override
    protected List<Reviews> doInBackground (String... params){
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;
        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/"+ params[0] + "/reviews";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM,BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                  buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error close", e);
                }
            }
        }

        try {
            return getReviewsFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(List<Reviews> reviews) {
        if (reviews != null) {
            if (reviews.size() > 0) {

                if (ReviewAdabter != null) {
                    ReviewAdabter.clear();
                    for (Reviews review : reviews) {
                        ReviewAdabter.add(review);
                    }
                }

            }
        }

    }
}
    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailers>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailers> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailers> results = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);

                  Trailers trailerModel = new Trailers(trailer);
                    results.add(trailerModel);
            }

            return results;
        }

        @Override
        protected List<Trailers> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+ params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                       .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error close", e);
                    }
                }
            }

            try {
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(List<Trailers> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    if (TrailerAdapter != null) {
                        TrailerAdapter.clear();
                        for (Trailers trailer : trailers) {
                            TrailerAdapter.add(trailer);
                        }
                    }

                }
            }
        }
    }

}
