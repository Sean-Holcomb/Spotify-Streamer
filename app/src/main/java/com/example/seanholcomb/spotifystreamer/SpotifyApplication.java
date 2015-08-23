package com.example.seanholcomb.spotifystreamer;

import android.app.Application;
import android.content.res.Configuration;
import android.media.MediaPlayer;

import java.util.List;

/**
 * Created by seanholcomb on 7/20/15.
 */
public class SpotifyApplication extends Application {

    private static SpotifyApplication singleton;
    private ArtistParcel parcel;
    private String mArtist;
    private int position;
    private List<String> musicUrls;
    private boolean mIsTablet = false;
    private MediaPlayer mediaPlayer=null;
    private String nowPlaying = "";

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
    //below here needs to be deleted

    public void setPosition(int i){
         parcel.setPosition(i);
    }

    public int getPosition(){
        return parcel.getPosition();
    }

    public void setmIsTablet(){
        mIsTablet=true;
    }

    public boolean getIsTablet(){
        return mIsTablet;
    }

    public void setMediaPlayer(MediaPlayer m){
        mediaPlayer=m;
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public void setNowPlaying(String s){
        nowPlaying=s;
    }

    public String getNowPlaying(){
        return nowPlaying;
    }




}
