package com.example.admin.mp3player.Fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.mp3player.R;

import java.util.concurrent.TimeUnit;

import static com.example.admin.mp3player.Fragments.MusicFragment.mediaPlayer;
import static com.example.admin.mp3player.Fragments.OnlineSearchFragment.mAdapter;
import static com.example.admin.mp3player.Fragments.OnlineSearchFragment.mList;
import static com.example.admin.mp3player.Online.adapterMusic.mPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayOnlineFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener {

    private TextView tvNamePlay, tvArtistRun, tvTotalTime, tvRemainTime;
    private ImageView shuffe, prev, play, next, repeat;
    private SeekBar seekBar;
    private int startTime;
    private int finalTime;
    private Handler myHandler = new Handler();
    private int count = 0;
    private int position1;
    private int position;
    private String name;
    private int time;



    public PlayOnlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_online, container, false);

        tvNamePlay = (TextView) view.findViewById(R.id.nameRun1);
        tvArtistRun = (TextView) view.findViewById(R.id.artistRun1);
        tvTotalTime = (TextView) view.findViewById(R.id.totalTime1);
        tvRemainTime = (TextView) view.findViewById(R.id.timeRemain1);

        finalTime = 300000;
        startTime = mPlayer.getCurrentPosition();

        name = getArguments().getString("Name", name);
        tvNamePlay.setText(name);
//        tvArtistRun.setText(artist);

        shuffe = (ImageView) view.findViewById(R.id.shuffe);
        // prev = (ImageView) view.findViewById(R.id.prev);
        play = (ImageView) view.findViewById(R.id.play1);
        //nextBoole = (ImageView) view.findViewById(R.id.nextBoole);
        repeat = (ImageView) view.findViewById(R.id.repeat);

//        shuffe.setOnClickListener(this);
//        prev.setOnClickListener(this);
        play.setOnClickListener(this);
//        nextBoole.setOnClickListener(this);
//        repeat.setOnClickListener(this);

        tvTotalTime.setText(String.format("%d min %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tvRemainTime.setText(String.format("%d min %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );


        seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
        seekBar.setClickable(false);
        seekBar.setMax(finalTime);
        seekBar.setProgress(mPlayer.getCurrentPosition());
        myHandler.postDelayed(UpdateSongTime, 100);
        seekBar.setOnSeekBarChangeListener(this);
        // seekBar.setOnSeekBarChangeListener(this);


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View v) {

        count++;

        switch (v.getId()) {
            case R.id.shuffe:

                break;
            case R.id.prev1:

                check();
                mAdapter.getSongItem(mList.get(position1 - 1).getDataCode());

                break;

            case R.id.play1:
                if ((count > 2 && count % 2 == 0 )|| count == 1 ) {
                    play.setImageResource(R.drawable.icnotifplay);
                    mPlayer.pause();
                } else {
                    play.setImageResource(R.drawable.icnotifpause);
                    mPlayer.start();
                }

                break;

            case R.id.next1:

                check();
                mAdapter.getSongItem(mList.get(position1 + 1).getDataCode());

                break;

            case R.id.repeat:


                break;


            default:
                break;

        }

    }

    private void check() {

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release ();
            mediaPlayer = null;

        }

        mPlayer = new MediaPlayer();
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mPlayer.getCurrentPosition();

            tvRemainTime.setText(String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setMax(mPlayer.getDuration());
            seekBar.setProgress(mPlayer.getCurrentPosition());
            Log.d("TAGGGGGGGGGGGGGGGGGGGG", mPlayer.getDuration()+"") ;
//            if (mPlayer.getCurrentPosition() >= mPlayer.getDuration()) {
//                mPlayer.stop();
//                mPlayer.release();
//                mPlayer = null;
//
//                pos1 = pos1 + 1;
//
//                Uri uri2 = Uri.parse(linkOnline);
//                mPlayer = mPlayer.create(getContext(), uri2);
//                tvNamePlay.setText(mList.get(pos1).getTitle());
//                mPlayer.start();
//                startTime = 0;
//                finalTime = mPlayer.getDuration();
//                tvTotalTime.setText(String.format("%d min %d sec",
//                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
//                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
//                                        finalTime)))
//                );
//
//                seekBar.setProgress(mPlayer.getCurrentPosition());
//
//            }
            myHandler.postDelayed(this, 100);

        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser == true) {
            int time =  progress;
            if (time > mPlayer.getDuration())
                time = mPlayer.getDuration();

            mPlayer.pause();
            mPlayer.seekTo(time);
            mPlayer.start();
            mPlayer.setOnBufferingUpdateListener(this);


        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);

    }
}
