package com.af1987.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.af1987.codepath.flickster.R;
import com.af1987.codepath.flickster.models.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.rv_movie, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(movies.get(i));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivPoster, ivBackdrop;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvDesc.setText(movie.getDesc());
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                Glide.with(context).load(movie.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background)
                        .override(200, 300))
                        .into(ivPoster);
            else
                Glide.with(context).load(movie.getBackdropPath())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background)
                        .override(400, 200))
                        .into(ivBackdrop);
        }
    }
}
