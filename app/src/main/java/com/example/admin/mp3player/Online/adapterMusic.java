package com.example.admin.mp3player.Online;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.mp3player.Activity.MainActivity;
import com.example.admin.mp3player.Fragments.PlayOnlineFragment;
import com.example.admin.mp3player.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.example.admin.mp3player.Fragments.MusicFragment.mediaPlayer;
import static com.example.admin.mp3player.Fragments.OnlineSearchFragment.mList;

/**
 * Created by VuMinhDuc on 17/05/04.
 */

public class adapterMusic extends RecyclerView.Adapter<adapterMusic.Music> {

    private Iinter mInter;
    private MainActivity mContext;
    private MediaPlayerOnline mPlayerOnline;
    public itemSongOnline songOnline = new itemSongOnline();
    public static MediaPlayer mPlayer;
    public static String linkOnline;

    public adapterMusic(Iinter inter, MainActivity context) {
        mInter = inter;
        mContext = context;
    }

    @Override
    public Music onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

        }

        mPlayer = new MediaPlayer();
        return new Music(view);
    }

    @Override
    public void onBindViewHolder(Music holder, final int position) {
        MusicItem item = mInter.getItem(position);
        holder.tvName.setText(item.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;

                }

                if (mediaPlayer != null  &&  mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                }

                mPlayer = new MediaPlayer();

                getSongItem(mList.get(position).getDataCode());
                Log.d("DUCCCCCCCCCCC", mList.get(position).getDataCode());

                if (mPlayer.getCurrentPosition() == mPlayer.getDuration()) {
                    int pos = position + 1;
                    getSongItem(mList.get(pos).getDataCode());
                }

                Bundle bundle = new Bundle();
                bundle.putString("Name", mInter.getItem(position).getTitle());

                PlayOnlineFragment play = new PlayOnlineFragment();
                FragmentManager manager = mContext.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                play.setArguments(bundle);
                transaction.addToBackStack("AHIHI");
                transaction.replace(R.id.OnlineXML, play);
                transaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mInter.getCount();
    }


    public interface Iinter {
        int getCount();

        MusicItem getItem(int position);
    }

    static class Music extends RecyclerView.ViewHolder {

        public TextView tvName;

        public Music(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.title_music);
        }
    }

    public void getSongItem(String datacode) {

        final String link = "http://mp3.zing.vn/html5xml/song-xml/" + datacode;
        linkOnline = link;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    URL url = new URL(link);
                    InputStream in = url.openStream();

                    byte b[] = new byte[1024];
                    int le = in.read(b);

                    String content = "";

                    while (le >= 0) {
                        content = content + new String(b, 0, le);
                        le = in.read(b);
                    }

                    Gson gson = new Gson();
                    songOnline = gson.fromJson(content, itemSongOnline.class);

                    //Log.d("TAGGGGGGGGGGGGGGGG", "getSongItem......." + songOnline.getSource_list().get(0));

                    StringBuilder getLink = new StringBuilder("http://");


                    try {
                        getLink.append(songOnline.getData().get(0)
                                .getSource_list().get(0));
                        getLink.append(songOnline.getData().get(0)
                                .getSource_list().get(1));


                    } catch (Exception e) {
                    }

                    Log.d("TAGGGGGGGGG", getLink.toString());


                    try {
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mPlayer.reset();
                        mPlayer.setDataSource(getLink.toString());
                        try {
                            mPlayer.prepareAsync();

                        } catch (IllegalStateException e) {}
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }


}
