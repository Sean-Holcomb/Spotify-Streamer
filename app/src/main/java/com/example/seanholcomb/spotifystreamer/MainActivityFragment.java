package com.example.seanholcomb.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private List<String> artistList= new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private ArtistParcel mParcel = new ArtistParcel(artistList, idList, urlList);;
    private EditText searchBox;
    private ArtistSearchTask task;



    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.artists_listview);

        //opens top ten track view, borrowed from sunshine.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android
                Intent intent = new Intent(getActivity(), Top10Tracks.class);
                startActivity(intent);
            }
        });

        final SpotifyAdapter spot = new SpotifyAdapter(getActivity(), mParcel);


        searchBox = (EditText) rootView.findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (task != null) {
                    task.cancel(false);
                }

                if (searchBox.length() != 0) {
                    task = new ArtistSearchTask();
                    task.execute(searchBox.getText().toString());
                } else {
                    mParcel.artists.clear();
                    mParcel.ids.clear();
                    mParcel.images.clear();
                    spot.notifyDataSetChanged();
                }

            }

        });



        listView.setAdapter(spot);

        return rootView;
    }

    public class ArtistSearchTask extends AsyncTask<String, Void, Void>{



        @Override
        protected Void doInBackground(String... search){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(search[0]);



            List<Artist> searchBlock = results.artists.items;
            for(Artist artist : searchBlock){
                artistList.add(artist.name);
                idList.add(artist.id);
                if (artist.images.size() != 0) {
                    urlList.add(artist.images.get(0).url);
                }else
                    urlList.add("http://www.surffcs.com/Img/no_image_thumb.gif");
            }
            mParcel = new ArtistParcel(artistList, idList, urlList);






            return null;
        }

    }

}
