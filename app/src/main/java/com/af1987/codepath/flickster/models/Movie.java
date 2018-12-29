package com.af1987.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie{

    private String posterPath, backdropPath;
    private String title, desc, trailerKey;
    private int popularity, id;
    private double rating;

    private Movie(){};

    private Movie(JSONObject json) throws JSONException {
        posterPath = json.getString("poster_path");
        title = json.getString("title");
        desc = json.getString("overview");
        backdropPath = json.getString("backdrop_path");
        popularity = json.getInt("popularity");
        rating = json.getDouble("vote_average");
        id = json.getInt("id");
    }

    public String getTitle() {return title;}
    public String getDesc() {return desc;}
    public boolean isPopular() {return popularity > 200;}
    public double getRating() {return rating;}
    public int getId() {return id;}
    public String getTrailerKey() {return trailerKey;}
    public void setTrailerKey(String trailerKey) {this.trailerKey = trailerKey;}

    public String getPosterPath(int quality) {
        return String.format("https://image.tmdb.org/t/p/"
                + getQuality(quality)
                + "/%s", posterPath);
    }

    public String getBackdropPath(int quality) {
        return String.format("https://image.tmdb.org/t/p/"
                + getQuality(quality)
                + "/%s", backdropPath);
    }

    private String getQuality(int quality) {
        switch (quality) {
            case 0 :
                return "w92";
            case 1:
                return "w370_and_h556_bestv2";
            default:
                return "original";
        }
    }

    public static List<Movie> fromJSONArray (JSONArray array) throws JSONException{
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            movies.add(new Movie(array.getJSONObject(i)));
        return movies;
    }
}
