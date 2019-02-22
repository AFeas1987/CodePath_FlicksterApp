package com.af1987.codepath.flickster.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.af1987.codepath.flickster.R;
import com.af1987.codepath.flickster.activity.MovieDetailActivity;
import com.af1987.codepath.flickster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import static com.af1987.codepath.flickster.util.GlideUtil.*;
import static com.af1987.codepath.flickster.util.GlideUtil.Options.*;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private Activity activity;
    private List<Movie> movies;

    public MovieAdapter(Activity context, List<Movie> movies) {
        this.activity = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity)
                    .inflate(R.layout.rv_movie_plain, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {holder.bind(movies.get(i));}

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).isPopular())
            return 1;
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivPoster, ivBackground;
        View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivBackground = itemView.findViewById(R.id.ivBackground);
            this.itemView = itemView;
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvDesc.setText(movie.getDesc());
            if (activity.getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_PORTRAIT) {
                into(ivPoster, glide(activity, movie.getPosterPath(1), POSTER_LARGE,
                        glide(activity, movie.getPosterPath(0), POSTER_LARGE)));
                glide(activity, movie.getPosterPath(1), POSTER_LARGE_SKETCH).preload();
            }
            else {
                into(ivPoster, glide(activity, movie.getBackdropPath(1), BACKDROP_SMALL,
                        glide(activity, movie.getBackdropPath(0), BACKDROP_SMALL)));
                glide(activity, movie.getPosterPath(1), BACKDROP_SMALL_SKETCH).preload();
            }
            if (movie.isPopular()) {
                ivBackground.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.ivPlayIcon).setVisibility(View.VISIBLE);
                ivBackground.setAlpha(0.6f);
                into(ivBackground, glide(activity, movie.getBackdropPath(1), BACKGROUND,
                        glide(activity, movie.getBackdropPath(0), BACKGROUND)));
                glide(activity, movie.getBackdropPath(1), BACKGROUND_SKETCH).preload();
            }
            itemView.setOnClickListener(v -> {
                ActivityOptionsCompat intentOptions;
                if (movie.isPopular())
                    intentOptions = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(activity, Pair.create(ivPoster, ivPoster.getTransitionName()),
                                    Pair.create(tvTitle, tvTitle.getTransitionName()),
                                    Pair.create(tvDesc, tvDesc.getTransitionName()),
                                    Pair.create(ivBackground, ivBackground.getTransitionName()));
                else
                    intentOptions = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(activity, Pair.create(ivPoster, ivPoster.getTransitionName()),
                                    Pair.create(tvTitle, tvTitle.getTransitionName()),
                                    Pair.create(tvDesc, tvDesc.getTransitionName()));
                Intent i = new Intent(activity, MovieDetailActivity.class);
                i.putExtra("MOVIE", Parcels.wrap(movie));
                activity.startActivity(i, intentOptions.toBundle());
            });
            itemView.setOnTouchListener(createTouchListener(movie));
        }

        private View.OnTouchListener createTouchListener(Movie movie){
            return (v, e) -> {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (movie.isPopular())
                            into(ivBackground, glide(activity, movie.getBackdropPath(1), BACKGROUND_SKETCH,
                                    glide(activity, movie.getBackdropPath(1), BACKGROUND)));
                        if (activity.getResources().getConfiguration()
                                .orientation == Configuration.ORIENTATION_PORTRAIT)
                            into(ivPoster, glide(activity, movie.getPosterPath(1), POSTER_LARGE_SKETCH,
                                    glide(activity, movie.getPosterPath(1), POSTER_LARGE)));
                        else
                            into(ivPoster, glide(activity, movie.getBackdropPath(1), BACKDROP_SMALL_SKETCH,
                                    glide(activity, movie.getBackdropPath(1), BACKDROP_SMALL)));
                        return true;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        if (movie.isPopular())
                            into(ivBackground, glide(activity, movie.getBackdropPath(1), BACKGROUND));
                        if (activity.getResources().getConfiguration()
                                .orientation == Configuration.ORIENTATION_PORTRAIT)
                            into(ivPoster, glide(activity, movie.getPosterPath(1), POSTER_LARGE));
                        else
                            into(ivPoster, glide(activity, movie.getBackdropPath(1), BACKDROP_SMALL));
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        if (movie.isPopular())
                            into(ivBackground, glide(activity, movie.getBackdropPath(1), BACKGROUND));
                        if (activity.getResources().getConfiguration()
                                .orientation == Configuration.ORIENTATION_PORTRAIT)
                            into(ivPoster, glide(activity, movie.getPosterPath(1), POSTER_LARGE));
                        else
                            into(ivPoster, glide(activity, movie.getBackdropPath(1), BACKDROP_SMALL));
                        return false;
                    default:
                        return false;
                }
            };
        }
    }
}
