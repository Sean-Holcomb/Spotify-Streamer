package com.example.seanholcomb.spotifystreamer;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by seanholcomb on 7/20/15.
 */
public class SpotifyApplication extends Application {

    private static SpotifyApplication singleton;
    private ArtistParcel parcel;
    private String mArtist;
    private int position;

    public SpotifyApplication getInstance(){
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setArtistParcel(ArtistParcel artistParcel){
        parcel=artistParcel;
    }

    public ArtistParcel getArtistParcel(){
        return parcel;
    }

    public void setArtist(String s){
        mArtist=s;
    }

    public String getArtist(){
        return mArtist;
    }

    public void setPosition(int i){
        position = i;
    }

    public int getPosition(){
        return position;
    }
}
