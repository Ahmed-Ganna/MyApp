package com.ganna.faceparse.managers;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.ganna.faceparse.data.models.Joke;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by ganna on 24-06-2015.
 */
public class AppController extends Application {

    private static final String APPLICATION_ID = "Nf9F5w121eouibWDabyPV5xW0SRejbn3udLuaaoi";
    private static final String CLIENT_KEY = "KiTzg9uz2J542lDV8SSUkLxV1BNmQdXRwp6QXVld";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Joke.class);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);


        // to enable offline disk cache
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }


}
