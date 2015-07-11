package com.example.seanholcomb.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by seanholcomb on 7/11/15.
 */
public class ArtistParcel implements Parcelable {

    public List<String> artists;
    public List<String> ids;
    public List<String> images;

    public ArtistParcel(List<String> artists,List<String> ids, List<String> images){
        this.artists=artists;
        this.ids = ids;
        this.images = images;
    }

    private ArtistParcel(Parcel in){
        in.readList(artists, null);
        in.readList(ids, null);
        in.readList(images, null);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(artists);
        out.writeList(ids);
        out.writeList(images);
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
