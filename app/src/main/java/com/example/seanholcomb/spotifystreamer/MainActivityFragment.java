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
import android.widget.Toast;

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
    private ArtistParcel mParcel;
    private EditText searchBox;
    private ArtistSearchTask task;
    private static SpotifyAdapter spot;



    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("artists")){
            mParcel = new ArtistParcel(artistList, idList, urlList);
        }else{
            mParcel=savedInstanceState.getParcelable("artists");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("artists", mParcel);
        super.onSaveInstanceState(outState);
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
                String artist= artistList.get(position);
                String artistId = idList.get(position);
                Intent intent = new Intent(getActivity(), Top10Tracks.class)
                        .putExtra(Intent.EXTRA_TEXT, new String[]{artistId, artist});
                startActivity(intent);
            }
        });



        //Binding edit text object and setting up listener for searching artists
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
                    spot.notifyDataSetChanged();
                } else {
                    mParcel.artists.clear();
                    mParcel.ids.clear();
                    mParcel.images.clear();
                    spot.notifyDataSetChanged();
                }

            }

        });


        spot = new SpotifyAdapter(getActivity(), mParcel);
        listView.setAdapter(spot);

        return rootView;
    }
    
    public class ArtistSearchTask extends AsyncTask<String, Void, Boolean>{



        @Override
        protected Boolean doInBackground(String... search){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(search[0]);


            artistList.clear();
            idList.clear();
            urlList.clear();
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

            return artistList.size() !=0;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if (result){
                spot.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(), "No artists found, Please try another search.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
