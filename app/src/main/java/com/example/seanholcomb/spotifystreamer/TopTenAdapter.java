package com.example.seanholcomb.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seanholcomb on 7/11/15.
 */
public class TopTenAdapter extends BaseAdapter {

    private List<String> mTracks = new ArrayList<>();
    private List<String> mAlbums = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();

    private final Context context;

    public TopTenAdapter(Context context, ArtistParcel parcel){
        this.context=context;
        mTracks = parcel.artists;
        mAlbums = parcel.ids;
        mUrls = parcel.images;
    }

    @Override
    public int getCount(){
        return mTracks.size();
    }

    @Override
    public String getItem(int position){
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    /*
    //got help from
    //http://stackoverflow.com/questions/10120119/how-does-the-getview-method-work-when-creating-your-own-custom-adapter
    // and https://www.youtube.com/watch?v=wDBM6wVEO70
    **/
    public View getView(int position, View convertView, ViewGroup group){

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder;

        if( convertView == null ) {
            convertView = inflater.inflate(R.layout.list_item_toptrack, group, false);
            holder = new ViewHolder();
            holder.track= (TextView)convertView.findViewById(R.id.song);
            holder.album= (TextView)convertView.findViewById(R.id.album);
            holder.img= (ImageView)convertView.findViewById(R.id.album_art);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.track.setText(mTracks.get(position));
        holder.album.setText(mAlbums.get(position));
        Picasso.with(context).load(mUrls.get(position)).resize(300,300).centerCrop().into(holder.img);

        return convertView;

    }
    //https://www.youtube.com/watch?v=wDBM6wVEO70
    static class ViewHolder{
        TextView track;
        TextView album;
        ImageView img;
    }

}
