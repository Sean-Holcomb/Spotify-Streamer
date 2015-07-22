package com.example.seanholcomb.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by seanholcomb on 7/11/15.
 */
public class ArtistParcel implements Parcelable {

    private List<String> artists;
    private List<String> ids;
    private List<String> images;
    private String mArtist=null;
    private int position=0;
    private List<String> musicUrls=null;


    public ArtistParcel(List<String> artists, List<String> ids, List<String> images) {
        this.artists = artists;
        this.ids = ids;
        this.images = images;
    }

    public ArtistParcel(List<String> artists, List<String> ids, List<String> images, String artist, List<String> music) {
        this.artists = artists;
        this.ids = ids;
        this.images = images;
        mArtist=artist;
        musicUrls=music;
    }

    private ArtistParcel(Parcel in) {
        in.readList(artists, null);
        in.readList(ids, null);
        in.readList(images, null);
        in.readString();
        in.readInt();
        in.readList(musicUrls, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(artists);
        out.writeList(ids);
        out.writeList(images);
        out.writeString(mArtist);
        out.writeInt(position);
        out.writeList(musicUrls);
    }

    //method to clear data in object
    public void wipe() {
        artists.clear();
        ids.clear();
        images.clear();
        mArtist=null;
        position=0;
        musicUrls.clear();
    }

    //returns artists
    public List getArtists() {
        return artists;
    }

    //method to get ids
    public List getIds() {
        return ids;
    }

    //method to get images
    public List getImages() {
        return images;
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

    public List<String> getMusicUrls(){
        return musicUrls;
    }

    public void setMusicUrls(List<String> music){
        musicUrls=music;
    }

    public final Parcelable.Creator<ArtistParcel> CREATOR = new Parcelable.Creator<ArtistParcel>() {
        @Override
        public ArtistParcel createFromParcel(Parcel parcel) {
            return new ArtistParcel(parcel);
        }

        @Override
        public ArtistParcel[] newArray(int i) {
            return new ArtistParcel[i];
        }
    };
}
