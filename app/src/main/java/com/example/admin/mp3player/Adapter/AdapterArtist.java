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

import static com.example.admin.mp3player.Fragments.MusicFragment.listArtist;
import static com.example.admin.mp3player.Fragments.MusicFragment.mList;


public class AdapterArtist extends BaseAdapter implements Filterable {

    private Imusic mInter;
    private Context mContext;
    private Activity activity;
    private String path;
    public static Bitmap mBitmap;
    private List<Item> displayedList;
    private List<Item> orig;
    private ImageView img;
    private TextView tvName, tvSong, tvAlbums;

    public AdapterArtist(Imusic imusic, Activity act, Context context) {
        mInter = imusic;
        mContext = context;
        activity = act;
    }


    @Override
    public int getCount() {
        return mInter.getCountArtist();
    }

    @Override
    public Object getItem(int position) {
        return mInter.getItemArtist(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_artist, parent, false);

        img = (ImageView) convertView.findViewById(R.id.musicArtist);
        tvName = (TextView) convertView.findViewById(R.id.nameArtist);
        tvSong = (TextView) convertView.findViewById(R.id.tv_songArtist);
        tvAlbums = (TextView) convertView.findViewById(R.id.tv_albumsSearch);

        tvName.setText(listArtist.get(position).getArtist());
        tvAlbums.setText("Album " + listArtist.get(position).getNumAl()+"");
        tvSong.setText("Songs " + listArtist.get(position).getNumTrack()+"");
        for (int i = 0; i < mList.size(); i++) {

            if ((mList.get(i).getArtist()).equals(listArtist.get(position).getArtist())) {
                mBitmap = getAlbumart(Uri.parse(mList.get(i).getPath()));
                if(mBitmap != null && mList.get(i).getPath() != null) {
                    img.setImageBitmap(mBitmap);
                }
            }

        }
        return convertView;
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
        int getCountArtist();

        com.example.admin.mp3player.Common.Artist getItemArtist(int position);
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
