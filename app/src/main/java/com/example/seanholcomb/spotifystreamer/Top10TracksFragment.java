package com.example.seanholcomb.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksFragment extends Fragment {

    private ArtistParcel mParcel;
    private List<String> images = new ArrayList<>();
    private List<String> albumNames = new ArrayList<>();
    private List<String> trackNames = new ArrayList<>();
    private List<String> trackData = new ArrayList<>();
    private String mArtist;
    private String mId;
    private TopTenAdapter top;
    private boolean mIsTablet=false;
    private boolean isRefresh=false;




    public Top10TracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("topTracks")){
            mParcel = new ArtistParcel(trackNames, albumNames, images);
        }else{
            mParcel=savedInstanceState.getParcelable("topTracks");
            isRefresh=true;
        }
        Bundle arguments = getArguments();
        if (getActivity().getIntent().getExtras()!=null) {
            mId = getActivity().getIntent().getExtras().getStringArray(Intent.EXTRA_TEXT)[0];
            mArtist = getActivity().getIntent().getExtras().getStringArray(Intent.EXTRA_TEXT)[1];
        }else if (arguments != null){
            String[] extra = arguments.getStringArray("extra");
            mId = extra[0];
            mArtist = extra[1];
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("topTracks", mParcel);
        super.onSaveInstanceState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10_tracks, container, false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ListView listView = (ListView) rootView.findViewById(R.id.toptracks);
        TopTenTask topTenTask = new TopTenTask();
        if (mId != null) {
            if (!isRefresh) {
                topTenTask.execute(mId);
            }
            top = new TopTenAdapter(getActivity(), mParcel);
            listView.setAdapter(top);
        }




        AdapterView.OnItemClickListener listener =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NowPlayingActivityFragment playingActivityFragment =new NowPlayingActivityFragment();
                SpotifyApplication spotifyApplication = (SpotifyApplication) getActivity().getApplicationContext();
                spotifyApplication.setPosition(position);
                mIsTablet=spotifyApplication.getIsTablet();
                if (mIsTablet){
                    playingActivityFragment.show(fm, "Now Playing");
                }else {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.replace(android.R.id.content, playingActivityFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        };

        listView.setOnItemClickListener(listener);



        return rootView;
    }

    public class TopTenTask extends AsyncTask<String, Void, Boolean> {



        @Override
        protected Boolean doInBackground(String... search){

            //setting option for API Query
            Map<String, Object> setting = new HashMap<>();
            setting.put("country", "US");
            boolean areResults=false;
            Tracks results= null;
            if(isNetworkAvailable()) {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                results = spotify.getArtistTopTrack(search[0], setting);
                areResults= results.tracks.size() != 0;
            }

            if (areResults) {
                for (Track track : results.tracks) {

                    if (track.album.images.size() > 0) {
                        images.add(track.album.images.get(0).url);
                    } else {
                        images.add("http://www.surffcs.com/Img/no_image_thumb.gif");
                    }
                    albumNames.add(track.album.name);
                    trackNames.add(track.name);
                    if (track.preview_url != null){
                        trackData.add(track.preview_url);
                    }else{
                        trackData.add("");
                    }
                }
                mParcel = new ArtistParcel(trackNames, albumNames, images, mArtist, trackData);
                SpotifyApplication spotifyApplication=(SpotifyApplication) getActivity().getApplicationContext();
                spotifyApplication.setArtistParcel(mParcel);
            }


            return areResults;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if (result){
                top.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(), getString(R.string.no_tracks_toast) + mArtist, Toast.LENGTH_SHORT).show();
            }
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


    }
}
