package com.example.admin.mp3player.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3player.R;

import static com.example.admin.mp3player.Fragments.MusicFragment.mList;

/**
 * Created by minhd on 17/07/27.
 */

public class AdapterAlbums extends BaseAdapter{

    private Context mContext ;

    public AdapterAlbums(Context context) {
        mContext = context ;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_albums, parent, false) ;
        ImageView ivAlbum = (ImageView) view.findViewById(R.id.ivAlbums) ;
        TextView tvName = (TextView) view.findViewById(R.id.nameAlbums) ;
        TextView tvArtist = (TextView) view.findViewById(R.id.artistAlbums) ;
        if (mList.get(position).getPath() != null) ;
        Bitmap bitmap = getAlbumart(Uri.parse(mList.get(position).getPath()));
        if (bitmap != null ) {
            ivAlbum.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAlbum.setPadding(8, 8, 8, 8);
            ivAlbum.setImageBitmap(bitmap);
            ivAlbum.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAlbum.setPadding(8, 8, 8, 8);
            ivAlbum.setImageBitmap(bitmap);
        }

        if (mList.get(position).getAlbum() != null)
        tvName.setText(mList.get(position).getAlbum());
        tvArtist.setText(mList.get(position).getArtist());

        return view ;
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
