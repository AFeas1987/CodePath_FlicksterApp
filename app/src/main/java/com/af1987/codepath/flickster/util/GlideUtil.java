package com.af1987.codepath.flickster.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;


public final class GlideUtil {

    public static void into(ImageView iv, RequestBuilder request) {
        request.into(iv);
    }

    public static RequestBuilder glide(Context context, String url) {
        return Glide.with(context).load(url);
    }

    public static RequestBuilder glide(Context context, String url, RequestOptions options) {
        return glide(context, url).apply(options);
    }

    public static RequestBuilder glide(Context context, String url, RequestOptions options, RequestBuilder thumbnail) {
        return glide(context, url, options).thumbnail(thumbnail);
    }

    public final static class Options {
        public static final RequestOptions POSTER_SMALL = new RequestOptions()
                .transform(new RoundedCorners(8));
        public static final RequestOptions POSTER_LARGE = new RequestOptions()
                .transform(new RoundedCorners(12));
        public static final RequestOptions POSTER_LARGE_SKETCH = new RequestOptions()
                .transform(new MultiTransformation<>(
                        new VignetteFilterTransformation(),
                        new SketchFilterTransformation(),
                        new RoundedCorners(12)));
        public static final RequestOptions BACKDROP_SMALL = new RequestOptions()
                .transform(new RoundedCorners(8));
        public static final RequestOptions BACKDROP_SMALL_SKETCH = new RequestOptions()
                .transform(new MultiTransformation<>(
                        new RoundedCorners(8),
                        new SketchFilterTransformation()));
        public static final RequestOptions BACKGROUND = new RequestOptions()
                .transform(new VignetteFilterTransformation());
        public static final RequestOptions BACKGROUND_SKETCH = new RequestOptions()
                .transform(new MultiTransformation<>(
                        new SketchFilterTransformation(),
                        new VignetteFilterTransformation()));
    }
}
