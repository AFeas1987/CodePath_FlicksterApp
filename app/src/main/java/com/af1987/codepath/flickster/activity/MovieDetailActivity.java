package com.af1987.codepath.flickster.activity;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.af1987.codepath.flickster.R;
import com.af1987.codepath.flickster.models.Movie;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.af1987.codepath.flickster.util.GlideUtil.*;
import static com.af1987.codepath.flickster.util.GlideUtil.Options.*;

public class MovieDetailActivity extends AppCompatActivity implements
        RequestListener<Drawable> {

    private final static String _TRAILERS_API_ =
            "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private Movie movie;
    private YouTubePlayerFragment ytFragment;
    private ImageView ivPoster, ivBackground;
    private RatingBar rbRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        movie = Parcels.unwrap(getIntent().getParcelableExtra("MOVIE"));
        setContentView(R.layout.activity_movie_detail);
        ivPoster = findViewById(R.id.ivDetailPoster);
        ivBackground = findViewById(R.id.ivDetailBackground);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        tvTitle.setText(movie.getTitle());
        tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView desc = findViewById(R.id.tvDetailDesc);
        desc.setText(movie.getDesc());
        desc.setMovementMethod(new ScrollingMovementMethod());
        rbRating = findViewById(R.id.rbRating);
        rbRating.setRating((float) (movie.getRating()));
        if (movie.isPopular()) {
            ivBackground.setVisibility(View.VISIBLE);
            ivBackground.setAlpha(0.7f);
            into(ivBackground, glide(this, movie.getBackdropPath(1),
                    BACKGROUND.onlyRetrieveFromCache(true)).listener(this));
        if (getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT)
            into(ivPoster, glide(this, movie.getPosterPath(1), POSTER_SMALL.onlyRetrieveFromCache(true)));
        else
            into(ivPoster, glide(this, movie.getBackdropPath(1), BACKDROP_SMALL.onlyRetrieveFromCache(true)));
        }
        new AsyncHttpClient().get(String.format(Locale.ENGLISH, _TRAILERS_API_, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray movieResults = response.getJSONArray("results");
                    movie.setTrailerKey(movieResults.getJSONObject(0).getString("key"));
                    ytFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.ytPlayer);
                    getFragmentManager().beginTransaction().show(ytFragment).commit();
                    ytFragment.initialize("AIzaSyCvwIK0fib-KZ7GL4gbv08JmT6qaIqlxGc",
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer ytp, boolean b) {
                                    if (movie.isPopular())
                                        ytp.loadVideo(movie.getTrailerKey());
                                    else {
                                        startPostponedEnterTransition();
                                        playOpeningAnimations();
                                        ytp.cueVideo(movie.getTrailerKey());
                                    }
                                    ytp.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                                }
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {}
                            });
                } catch (JSONException e) {Log.d("_AF", e.getMessage());}
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {Log.d("_AF", responseString);}
        });
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                Target<Drawable> target, boolean isFirstResource) {
        Log.d("_AF", e != null ? e.getMessage() : "null GlideException encountered");
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                   DataSource dataSource, boolean isFirstResource) {
        startPostponedEnterTransition();
        playOpeningAnimations();
        return false;
    }

    @Override
    protected void onStop() {
//        ytFragment.getRootView().setVisibility(View.INVISIBLE);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//            ytFragment.getRootView().setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    private void playOpeningAnimations(){
        rbRating.setVisibility(View.VISIBLE);
        rbRating.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom));
    }


}
