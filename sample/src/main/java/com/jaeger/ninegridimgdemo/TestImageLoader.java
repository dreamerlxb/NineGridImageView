package com.jaeger.ninegridimgdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lxb.imagepreview.loader.IZoomMediaLoader;
import com.lxb.imagepreview.loader.MySimpleTarget;

/**
 * 作者：tiger on 18/5/22 15:17
 */

public class TestImageLoader implements IZoomMediaLoader {

    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, final @NonNull MySimpleTarget<Bitmap> simpleTarget) {
        GlideApp.with(context)
                .asBitmap()
                .load(path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.ic_default_image)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        simpleTarget.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        simpleTarget.onLoadStarted();
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        simpleTarget.onLoadFailed(errorDrawable);
                    }
                });
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        GlideApp.with(context).onStop();
    }

    @Override
    public void clearMemory(@NonNull Context c) {
        GlideApp.get(c).clearMemory();
    }
}
