package com.example.admin.mp3player.Common;

import android.media.MediaPlayer;

import java.io.IOException;

public class ManagerAudioOffline implements MediaPlayer.OnCompletionListener {
     public MediaPlayer mPlay;

    public ManagerAudioOffline() {
        mPlay = new MediaPlayer();
    }

    public void inits(String path) throws IOException {
        try {
            mPlay.setDataSource(path);
            mPlay.setOnCompletionListener(this);
        }catch (IllegalStateException e) {
            mPlay = new MediaPlayer();
            mPlay.setDataSource(path);
            mPlay.setOnCompletionListener(this);
        }

        mPlay.prepare();
    }

    public void start() {
        mPlay.start();
    }

    public int getDuration() {
       return mPlay.getDuration() ;
    }

    public int getCurrentPosition(){
       return mPlay.getCurrentPosition() ;
    }


    public void release() {
        mPlay.release();
    }

    public void seekTo(int position) {
        mPlay.seekTo(mPlay.getCurrentPosition() + position);
    }

    public void play() {
        if (!mPlay.isPlaying()) {
            mPlay.start();
        }
    }

    public void pause() {
        if (mPlay.isPlaying()) {
            mPlay.pause();
        }
    }

    public boolean isPlaying() {
        return mPlay.isPlaying();
    }

    public void stop() {
        mPlay.stop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
