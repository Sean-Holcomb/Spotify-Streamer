package com.example.seanholcomb.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int mPosition;


    public NowPlayingActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("NowPlaying")){

            mParcel = new ArtistParcel(trackNames, albumNames, images);

        }else{
            mParcel=savedInstanceState.getParcelable("NowPlaying");
        }
//Sean here I can access the bundle
        Bundle bundle = getActivity().getIntent().getExtras();
        //mParcel = getActivity().getIntent().getExtras().getParcelable("bundle");
        //trackNames = mParcel.getArtists();
        //albumNames = mParcel.getIds();
        //images = mParcel.getImages();
//But this line causes the app to crash
        mArtist = bundle.getString(Intent.EXTRA_TEXT);
        //mPosition = getActivity().getIntent().getExtras().getInt("position");

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
        //artist_textView.setText(mArtist);
        //album_textView.setText(albumNames.get(position));
        //track_textView.setText(trackNames.get(position));
        //Picasso.with(getActivity()).load(images.get(position)).resize(300, 300).centerCrop().into(albumArt_imageView);
    }
}
