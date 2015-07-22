package com.example.seanholcomb.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
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
    private MediaPlayer mediaPlayer;
    private List<String> mTracks;


    public NowPlayingActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("NowPlaying")){


            SpotifyApplication spotifyApplication=(SpotifyApplication) getActivity().getApplicationContext();
            mParcel = spotifyApplication.getArtistParcel();
            mPosition = spotifyApplication.getPosition();
            mArtist = spotifyApplication.getArtist();
            mTracks = spotifyApplication.getMusicUrls();
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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        artist_textView=(TextView)rootView.findViewById(R.id.artist);
        album_textView=(TextView)rootView.findViewById(R.id.album);
        track_textView=(TextView)rootView.findViewById(R.id.track);
        albumArt_imageView=(ImageView)rootView.findViewById(R.id.album_art);
        ImageButton preButton=(ImageButton)rootView.findViewById(R.id.back_button);
        ImageButton nextButton =(ImageButton)rootView.findViewById(R.id.next_button);
        ImageButton playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        bindView(mPosition);
        playMusic(mTracks.get(mPosition));

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    //playButton.setImageDrawable();

                } else {
                    mediaPlayer.start();


                }
            }
        });

        preButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPosition > 0) {
                    mPosition -= 1;
                    bindView(mPosition);
                    playMusic(mTracks.get(mPosition));
                } else {
                    Toast.makeText(getActivity(), "No Previous Track", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPosition < trackNames.size() - 1) {
                    mPosition += 1;
                    bindView(mPosition);
                    playMusic(mTracks.get(mPosition));
                } else {
                    Toast.makeText(getActivity(), "No More Tracks", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void bindView(int position){
        artist_textView.setText(mArtist);
        album_textView.setText(albumNames.get(position));
        track_textView.setText(trackNames.get(position));
        Picasso.with(getActivity()).load(images.get(position)).resize(1500, 1500).centerCrop().into(albumArt_imageView);
    }

    public void playMusic(String url) {


        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException except){
            Toast.makeText(getActivity(), "Shit's fucked", Toast.LENGTH_SHORT).show();
        }
    }
}
