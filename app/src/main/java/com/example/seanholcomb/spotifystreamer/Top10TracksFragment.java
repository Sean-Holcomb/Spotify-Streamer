package com.example.seanholcomb.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksFragment extends Fragment {


    public Top10TracksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10_tracks, container, false);



        return rootView;
    }

    public class TopTenTask extends AsyncTask<String, Void, Void> {



        @Override
        protected Void doInBackground(String... search){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            //ArtistsPager results = spotify.getArtistTopTrack(search[0]);








            return null;
        }

    }
}
