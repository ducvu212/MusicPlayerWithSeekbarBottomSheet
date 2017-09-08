package com.example.admin.mp3player.Online;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;


/**
 * Created by ducnd on 4/27/17.
 */

public class MediaPlayerOnline implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = MediaPlayerOnline.class.getSimpleName();
    private MediaPlayer mPlayer;
    private String mLink;
    private boolean mIsPrepare;

    public MediaPlayerOnline(String link) {
        mLink = link;
    }


    public void inits(Context context) throws IOException {
        mPlayer = new MediaPlayer();
        mPlayer.setOnErrorListener(this);

        Uri uri = Uri.parse(mLink);

        mPlayer.setDataSource(context, uri);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, " onError..... ");
        return true;
    }

    public void prepaire() {
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.prepareAsync();
    }

    public void play() {
        mPlayer.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsPrepare = true;
        Log.d(TAG, " onError..... ");
    }

    public boolean isPrepare() {
        return mIsPrepare;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdate percent: " + percent);
    }
}
