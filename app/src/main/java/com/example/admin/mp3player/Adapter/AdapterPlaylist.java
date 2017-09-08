package com.example.admin.mp3player.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3player.R;

import static com.example.admin.mp3player.Fragments.MusicFragment.listPlaylist;

/**
 * Created by minhd on 17/07/28.
 */

public class AdapterPlaylist extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,
                parent, false) ;

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_namePlaylist);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_Playlist);
        for (int i = 0; i < listPlaylist.size(); i++) {
            Log.d("PLAYlist", listPlaylist.get(i).getData()) ;
        }
        return view ;
    }
}
