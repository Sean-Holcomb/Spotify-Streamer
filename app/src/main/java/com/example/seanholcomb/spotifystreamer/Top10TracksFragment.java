package com.example.seanholcomb.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private String mArtist;
    private String mId;


    public Top10TracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("topTracks")){
            mParcel = new ArtistParcel(images, albumNames, trackNames);
        }else{
            mParcel=savedInstanceState.getParcelable("topTracks");
        }

        mId = getActivity().getIntent().getExtras().getStringArray(Intent.EXTRA_TEXT)[0];
        mArtist = getActivity().getIntent().getExtras().getStringArray(Intent.EXTRA_TEXT)[1];

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

        TopTenTask topTenTask = new TopTenTask();
        Log.e(mId, mArtist);
        topTenTask.execute(mId);

        ListView listView = (ListView) rootView.findViewById(R.id.toptracks);
        TopTenAdapter top = new TopTenAdapter(getActivity(), mParcel);
        listView.setAdapter(top);

        return rootView;
    }

    public class TopTenTask extends AsyncTask<String, Void, Void> {



        @Override
        protected Void doInBackground(String... search){

            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Tracks results = spotify.getArtistTopTrack(search[0], options);
            //problem still here maybe fixed already
            if (results.tracks.size() != 0) {

                for (Track track : results.tracks) {

                    if (track.album.images.size() > 0) {
                        images.add(track.album.images.get(0).url);
                    } else {
                        images.add("http://www.surffcs.com/Img/no_image_thumb.gif");
                    }
                    albumNames.add(track.album.name);
                    trackNames.add(track.name);
                }
                    mParcel = new ArtistParcel(trackNames, albumNames, images);



            }//else{
                //Toast.makeText(getActivity(), "Unfortunitely we have no tracks for \"" + mArtist + "\"", Toast.LENGTH_SHORT).show();
            //}






            return null;
        }

    }
}
