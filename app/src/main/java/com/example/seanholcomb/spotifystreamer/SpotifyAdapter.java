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
 * Created by seanholcomb on 7/6/15.
 */
public class SpotifyAdapter extends BaseAdapter{

    private List<String> mArtists = new ArrayList<>();
    private List<String> mIds = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();

    private final Context context;

    public SpotifyAdapter(Context context, List artists, List ids, List urls){
        this.context=context;
        mArtists = artists;
        mIds = ids;
        mUrls = urls;
    }

    //required method to extend BaseAdapter
    public int getCount(){
        return mArtists.size();
    }

    //required method tp extend BaseAdaptor
    public String getItem(int position){
        return mArtists.get(position);
    }

    //required method to extend BaseAdapter
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
            convertView = inflater.inflate(R.layout.list_item_artist, group, false);
            holder = new ViewHolder();
            holder.text= (TextView)convertView.findViewById(R.id.artist);
            holder.img= (ImageView)convertView.findViewById(R.id.artist_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.text.setText(getItem(position));
        Picasso.with(context).load(mUrls.get(position)).resize(300,300).centerCrop().into(holder.img);

        return convertView;

    }
    //https://www.youtube.com/watch?v=wDBM6wVEO70
    static class ViewHolder{
        TextView text;
        ImageView img;
    }

}
