package com.example.admin.mp3player.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3player.Common.Item;
import com.example.admin.mp3player.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.mp3player.Fragments.MusicFragment.mList;

/**
 * Created by Admin on 4/24/2017.
 */

public class AdapterList extends BaseAdapter implements Filterable {

    private Imusic mInter;
    private Context mContext;
    private Activity activity;
    private String path;
    public static Bitmap mBitmap;
    private List<Item> displayedList;
    private List<Item> orig;

    public AdapterList(Imusic imusic, Activity act, Context context) {
        mInter = imusic;
        mContext = context;
        activity = act;
    }


    @Override
    public int getCount() {
        return mInter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mInter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_music, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.music);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.artist);

        Item item = mInter.getItem(position);
        if (mList.get(position).getPath() != null)
        mBitmap = getAlbumart(Uri.parse(item.getPath()));
        if (getAlbumart(Uri.parse(item.getPath())) != null) {
            img.setImageBitmap(mBitmap);
        }
        tvName.setText(item.getTitle());
        tvArtist.setText(item.getArtist());


        return convertView;
    }



    public void updateList(List<Item> filteredList) {
        this.displayedList = filteredList;
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Item> results = new ArrayList<Item>();
                if (orig == null)
                    orig = mList;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final Item g : orig) {
                            if (g.getTitle().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<Item>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public interface Imusic {
        int getCount();

        Item getItem(int position);
    }

    public Bitmap getAlbumart(Uri uri) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(mContext, uri);
        rawArt = mmr.getEmbeddedPicture();


        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        }
        return art;
    }

}
