package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * Created by MIDO on 03/12/2015.
 */
public  class MoviesFragment extends Fragment {
    private Movie_Adapter MoviesAdapter;
    private static final String POPULARITY_sort = "popularity.desc";
    private static final String RATING_sort = "vote_average.desc";
    private String sort_by = POPULARITY_sort;

    public static boolean twoplane=false;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(
                R.id.GridViewMovies);
        MoviesAdapter = new Movie_Adapter(getActivity());
        gridView.setAdapter(MoviesAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movies movies = MoviesAdapter.getItem(position);
                Util.currentMovie=movies;
             if(twoplane){

                Bundle bundle=new Bundle();
                 bundle.putInt("MoviePosition", position);
                     getFragmentManager().beginTransaction()
                         .replace(R.id.movie_detail_container, new DetailFragment())
                         .commit();
             }else{
                 Intent intent = new Intent(getActivity(), DetailActivity.class);

                 startActivity(intent);
             }

            }
        });

        return rootView;
    }

    private void updateMovies(String sort_by) {
        if(sort_by=="FAV_DESC"){
            FetchFavTask favTask = new FetchFavTask();
            favTask.execute(sort_by);

        }
        else
        {
        FetchMovieTask movieTask = new FetchMovieTask();

        movieTask.execute(sort_by);}
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies(sort_by);
    }


    public class FetchFavTask extends  AsyncTask<String,Void,List<Movies>>{

        private final String LOG_TAG = FetchFavTask.class.getSimpleName();

        private List<Movies> getFavFromJson(JSONArray jsonStr) throws JSONException{

            List<Movies> results=new ArrayList<>();
            for(int i=0; i<jsonStr.length();i++){
                JSONObject favourite=jsonStr.getJSONObject(i);
                Movies reviewResult=new Movies(favourite);
                results.add(reviewResult);
            }
            return  results;
        }


        protected List<Movies> doInBackground (String... params){
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;
            final fav_table fav =new fav_table(getActivity());

            int[] a;
            a=fav.getData();
            JSONArray s=new JSONArray();

            for(int i=0 ; i<a.length;i++) {
                try {
                    final String BASE_URL = "http://api.themoviedb.org/3/movie/" + a[i];
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
                    JSONObject obj =new JSONObject(jsonStr);
                    s.put(obj);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
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
            }
            try {
                return getFavFromJson(s);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
        @Override

        protected void onPostExecute(List<Movies> result) {
            if (result != null && MoviesAdapter != null) {
                MoviesAdapter.clear();
                for (Movies newMovie : result) {
                    MoviesAdapter.add(newMovie);
                }
            }

        }
    }


    public class FetchMovieTask extends AsyncTask<String, Void, List<Movies>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        private List<Movies> getMoviesDataFromJson(String movieJsonStr)
                throws JSONException

        {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");
            List<Movies> results = new ArrayList<>();
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movies movieModel = new Movies(movie);
                results.add(movieModel);
            }

            return results;
        }




        @Override
        protected List<Movies> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;

            try {

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";

                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";




                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
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
                moviesJsonStr = buffer.toString();

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
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override

        protected void onPostExecute(List<Movies> result) {
            if (result != null && MoviesAdapter != null) {
                MoviesAdapter.clear();
                for (Movies newMovie : result) {
                    MoviesAdapter.add(newMovie);
                }
            }
        }
    }

        @Override


        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }



        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
           MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);

            if (sort_by.contentEquals(POPULARITY_sort)) {
                if (!action_sort_by_popularity.isChecked())
                    action_sort_by_popularity.setChecked(true);
            } else {
                if (!action_sort_by_rating.isChecked())
                    action_sort_by_rating.setChecked(true);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();
            switch (id) {
                case R.id.action_sort_by_popularity:
                    if (item.isChecked())
                        item.setChecked(false);
                    else
                        item.setChecked(true);
                    sort_by = POPULARITY_sort;
                    updateMovies(sort_by);
                    return true;
                case R.id.action_sort_by_rating:
                    if (item.isChecked())
                        item.setChecked(false);
                    else
                        item.setChecked(true);
                    sort_by = RATING_sort;
                    updateMovies(sort_by);
                    return true;

                case R.id.action_sort_by_fav:
                    if (item.isChecked())
                        item.setChecked(false);
                    else
                        item.setChecked(true);
                    sort_by = "FAV_DESC";
                    updateMovies(sort_by);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        }













