package com.example.admin.mp3player.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.admin.mp3player.R;

import static com.example.admin.mp3player.Fragments.MusicFragment.listPlaylist;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        for (int i = 0; i < listPlaylist.size(); i++) {
            Log.d("PLAYlist", listPlaylist.get(i).getData()) ;
        }
    }
}
