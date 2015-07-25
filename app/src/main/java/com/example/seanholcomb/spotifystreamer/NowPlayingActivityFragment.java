package com.example.seanholcomb.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
public class NowPlayingActivityFragment extends Fragment {

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



    public NowPlayingActivityFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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
        bindView(mPosition);
        playMusic(mTracks.get(mPosition));


        OnSeekBarChangeListener listener = new OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){
                if (touchLock) {
                    mediaPlayer.seekTo(progress*1000);
                }
                //amountPlayed.setText("0:" + progress);
                //amountLeft.setText("0:"+(SONG_LENGTH-progress));
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
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mPosition -= 1;
                    bindView(mPosition);
                    playMusic(mTracks.get(mPosition));
                    mParcel.setPosition(mPosition);
                } else {
                    Toast.makeText(getActivity(), "No Previous Track", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPosition < trackNames.size() - 1) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mPosition += 1;
                    bindView(mPosition);
                    playMusic(mTracks.get(mPosition));
                    mParcel.setPosition(mPosition);
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
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
            syncSeekBar();
        }catch(IOException except){
            Toast.makeText(getActivity(), "Connection Problem", Toast.LENGTH_SHORT).show();
        }
    }

    public void syncSeekBar() {
        int position = mediaPlayer.getCurrentPosition() / 1000;
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
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            syncSeekBar();
        }
    };


/*
    public class SpotifyService extends Service implements MediaPlayer.OnPreparedListener {
        private static final String ACTION_PLAY = "com.example.action.PLAY";
        MediaPlayer mediaPlayer = null;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public int onStartCommand(Intent intent, int flags, int startId) {

            if (intent.getAction().equals(ACTION_PLAY)) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync(); // prepare async to not block main thread
            }
        }

        // Called when MediaPlayer is ready
        public void onPrepared(MediaPlayer player) {
            player.start();
        }
    }*/
}
