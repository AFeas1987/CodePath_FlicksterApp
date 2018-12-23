package com.af1987.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.af1987.codepath.flickster.R;
import com.af1987.codepath.flickster.models.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

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
        if (movies.get(position).getPopularity() > 200)
            return 1;
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivPoster, ivBackdrop, ivBackground;
        View itemView;

//        private SimpleTarget<Drawable> target = new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource,
//                        @Nullable Transition<? super Drawable> transition) {
//                itemView.setBackground(resource);
//                itemView.getBackground().setAlpha(100);
//            }
//        };

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            ivBackground = itemView.findViewById(R.id.ivBackground);
            this.itemView = itemView;
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvDesc.setText(movie.getDesc());
            if (context.getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_PORTRAIT)
                Glide.with(context).load(movie.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                        .override(480, 720)
                        .transform(new RoundedCorners(8)))
                        .into(ivPoster);
            else
                Glide.with(context).load(movie.getBackdropPath())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                        .override(800, 400)
                        .transform(new RoundedCorners(8)))
                        .into(ivBackdrop);
            if (movie.getPopularity() > 200) {
                ivBackground.setVisibility(View.VISIBLE);
                ivBackground.setAlpha(0.3f);
                Glide.with(context).load(movie.getBackdropPath()).into(ivBackground);
            }
        }
    }
}
