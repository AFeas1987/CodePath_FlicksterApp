package com.af1987.codepath.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.af1987.codepath.flickster.adapters.MovieAdapter;
import com.af1987.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.af1987.codepath.flickster.models.Movie.fromJSONArray;


public class MovieActivity extends AppCompatActivity {

    final static String _MOVIEURL_ = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        final RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        final MovieAdapter adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        rvMovies.setAdapter(adapter);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(_MOVIEURL_, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray movieResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieResults));
                    Log.d("_AF", movies.toString());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("_AF", e.getMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                Log.d("_AF", responseString);
            }
        });
    }
}
