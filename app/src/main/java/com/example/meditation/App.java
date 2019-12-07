package com.example.meditation;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String defaultLanguage = "en";
        Lingver.init(this, defaultLanguage);
    }
}
