package com.example.seanholcomb.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class NowPlayingActivityFragment extends Fragment {

    private ArtistParcel mParcel;
    private List<String> images = new ArrayList<>();
    private List<String> albumNames = new ArrayList<>();
    private List<String> trackNames = new ArrayList<>();
    private String mArtist;
    private TextView artist_textView;
    private TextView album_textView;
    private TextView track_textView;
    private ImageView albumArt_imageView;
    private int mPosition=0;


    public NowPlayingActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("NowPlaying")){

            Log.e("fuuuuuuck", "me");

            SpotifyApplication spotifyApplication=(SpotifyApplication) getActivity().getApplicationContext();
            mParcel = spotifyApplication.getArtistParcel();
            mPosition = spotifyApplication.getPosition();
            mArtist = spotifyApplication.getArtist();

        }else{
            mParcel=savedInstanceState.getParcelable("NowPlaying");
        }


        trackNames = mParcel.getArtists();
        albumNames = mParcel.getIds();
        images = mParcel.getImages();

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("NowPlaying", mParcel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        Log.e("Dirty", "Double");
        artist_textView=(TextView)rootView.findViewById(R.id.artist);
        album_textView=(TextView)rootView.findViewById(R.id.album);
        track_textView=(TextView)rootView.findViewById(R.id.track);
        albumArt_imageView=(ImageView)rootView.findViewById(R.id.album_art);
        bindView(mPosition);
        return rootView;
    }

    public void bindView(int position){
        Log.e("Dirty", "Triple");
        artist_textView.setText(mArtist);
        Log.e("Dirty", "Quad");
        album_textView.setText(albumNames.get(position));
        track_textView.setText(trackNames.get(position));
        Picasso.with(getActivity()).load(images.get(position)).resize(300, 300).centerCrop().into(albumArt_imageView);
    }
}
