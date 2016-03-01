package com.hooptap.brandsampleaws;

import android.app.Application;

import com.hooptap.brandsampleaws.Utils.TinyDB;
import com.hooptap.sdkbrandclub.Api.Hooptap;


/**
 * Created by root on 16/12/15.
 */

public class HTApplication extends Application {
    public static TinyDB tinydb;
    private static HTApplication instance;
    private String API_KEY = "56cd7ef8a933937c403897c8";//46576686f6f707461702e627

    @Override
    public void onCreate() {
        super.onCreate();
        new Hooptap.Builder(this)
                .setApiKey(API_KEY)
                .enableDebug(true)
                .build();

        instance = HTApplication.this;
    }

    public static TinyDB getTinydb() {
        if (tinydb != null) {
        } else {
            tinydb = new TinyDB(instance);
        }
        return tinydb;
    }
}
