package com.example.seanholcomb.spotifystreamer;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class NowPlayingDialogFragment extends DialogFragment {
    private final int MILISEC_CONVERT= 1000;
    private final int INT_DIV_FIX= 999;
    private final int SONG_LENGTH=30;
    private ArtistParcel mParcel;
    private List<String> images = new ArrayList<>();
    private List<String> albumNames = new ArrayList<>();
    private List<String> trackNames = new ArrayList<>();
    private String mArtist;
    private TextView artist_textView;
    private TextView album_textView;
    private TextView track_textView;
    private ImageView albumArt_imageView;
    private SeekBar seekBar;
    private TextView amountPlayed;
    private TextView amountLeft;
    ImageButton playButton;

    private int mPosition=0;
    private MediaPlayer mediaPlayer;
    private List<String> mTracks;
    private Handler handler = new Handler();
    private Boolean touchLock = false;



    public NowPlayingDialogFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("NowPlaying")){


            SpotifyApplication spotifyApplication=(SpotifyApplication) getActivity().getApplicationContext();
            mParcel = spotifyApplication.getArtistParcel();
            mPosition = spotifyApplication.getPosition();

        }else{
            mParcel=savedInstanceState.getParcelable("NowPlaying");
            mPosition=mParcel.getPosition();
        }

        mArtist=mParcel.getArtist();
        mTracks=mParcel.getMusicUrls();
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
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.reset();

        super.onDestroy();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        if (mediaPlayer ==null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }


        artist_textView=(TextView)rootView.findViewById(R.id.artist);
        album_textView=(TextView)rootView.findViewById(R.id.album);
        track_textView=(TextView)rootView.findViewById(R.id.track);
        albumArt_imageView=(ImageView)rootView.findViewById(R.id.album_art);
        seekBar=(SeekBar)rootView.findViewById(R.id.seekbar);
        amountPlayed=(TextView)rootView.findViewById(R.id.timePlayed);
        amountLeft=(TextView)rootView.findViewById(R.id.timeLeft);
        ImageButton preButton=(ImageButton)rootView.findViewById(R.id.back_button);
        ImageButton nextButton =(ImageButton)rootView.findViewById(R.id.next_button);
        playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        playButton.setClickable(false);
        bindView(mPosition);
        playMusic(mTracks.get(mPosition));


        OnSeekBarChangeListener listener = new OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){
                if (touchLock) {
                    mediaPlayer.seekTo(progress*MILISEC_CONVERT);
                }
            }

            @Deprecated
            public void onStartTrackingTouch (SeekBar seekBar){
                touchLock =true;
            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar){
                touchLock =false;
            }
        };
        seekBar.setMax(SONG_LENGTH);
        seekBar.setOnSeekBarChangeListener(listener);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic_media_play);

                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_media_pause);


                }
            }
        });

        preButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPosition > 0) {
                    changeSong(-1);
                } else {
                    Toast.makeText(getActivity(), "No Previous Track", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPosition < trackNames.size() - 1) {
                    changeSong(1);
                } else {
                    Toast.makeText(getActivity(), "No More Tracks", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer player){
                playButton.setClickable(true);
                player.start();
                playButton.setImageResource(R.drawable.ic_media_pause);
            }
        });

        return rootView;
    }

    public void bindView(int position){
        artist_textView.setText(mArtist);
        album_textView.setText(albumNames.get(position));
        track_textView.setText(trackNames.get(position));
        Picasso.with(getActivity()).load(images.get(position)).resize(700, 700).centerCrop().into(albumArt_imageView);
    }

    public void playMusic(String url) {

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
            syncSeekBar();
        }catch(IOException except){
            Toast.makeText(getActivity(), "Connection Problem", Toast.LENGTH_SHORT).show();
        }
    }

    public void syncSeekBar() {

        int position = (mediaPlayer.getCurrentPosition()+INT_DIV_FIX) / MILISEC_CONVERT;
        seekBar.setProgress(position);
        if (position<10) {

            amountPlayed.setText("0:0" + String.valueOf(position));
            amountLeft.setText("-0:" + String.valueOf(30 - position));
        } else if (position>20) {
            amountPlayed.setText("0:" + String.valueOf(position));
            amountLeft.setText("-0:0" + String.valueOf(30 - position));
        } else{
            amountPlayed.setText("0:" + String.valueOf(position));
            amountLeft.setText("-0:" + String.valueOf(30 - position));
        }
        if (position == 30){
            if (mPosition < trackNames.size() - 1) {
                changeSong(1);
            } else {
                mPosition=0;
                changeSong(0);
            }

        }
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            syncSeekBar();
        }
    };

    //changes track being displayed and playing
    //@param shifts song by i on track list.
    public void changeSong(int i){
        mediaPlayer.stop();
        mediaPlayer.reset();
        mPosition += i;
        bindView(mPosition);
        playMusic(mTracks.get(mPosition));
        mParcel.setPosition(mPosition);
        playButton.setImageResource(R.drawable.ic_media_play);
    }


}
