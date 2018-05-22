package com.jaeger.ninegridimgdemo;

import android.app.Application;

import com.lxb.imagepreview.ZoomMediaLoader;

/**
 * 作者：tiger on 18/5/22 15:16
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZoomMediaLoader.getInstance().init(new TestImageLoader());
    }
}
