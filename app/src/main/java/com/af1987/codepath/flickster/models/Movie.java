package com.af1987.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String posterPath, backdropPath;
    private String title, desc;

    private Movie(JSONObject json) throws JSONException {
        posterPath = json.getString("poster_path");
        title = json.getString("title");
        desc = json.getString("overview");
        backdropPath = json.getString("backdrop_path");
    }

    public String getTitle() {return title;}
    public String getDesc() {return desc;}


    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }


    public static List<Movie> fromJSONArray (JSONArray array) throws JSONException{
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            movies.add(new Movie(array.getJSONObject(i)));
        return movies;
    }
}
