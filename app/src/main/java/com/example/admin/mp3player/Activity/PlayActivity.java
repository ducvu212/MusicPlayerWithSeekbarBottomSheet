package com.example.admin.mp3player.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.mp3player.Common.BlurBuilder;
import com.example.admin.mp3player.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.example.admin.mp3player.Activity.MainActivity.check;
import static com.example.admin.mp3player.Activity.MainActivity.checkPrev;
import static com.example.admin.mp3player.Activity.MainActivity.nextBoole;
import static com.example.admin.mp3player.Activity.MainActivity.number;
import static com.example.admin.mp3player.Activity.MainActivity.numberPrev;
import static com.example.admin.mp3player.Activity.MainActivity.prevBoole;
import static com.example.admin.mp3player.Activity.MainActivity.relayControl;
import static com.example.admin.mp3player.Common.ServiceMedia.remoteViews;
import static com.example.admin.mp3player.Fragments.MusicFragment.adapterList;
import static com.example.admin.mp3player.Fragments.MusicFragment.artist;
import static com.example.admin.mp3player.Fragments.MusicFragment.bitmap;
import static com.example.admin.mp3player.Fragments.MusicFragment.mList;
import static com.example.admin.mp3player.Fragments.MusicFragment.mService;
import static com.example.admin.mp3player.Fragments.MusicFragment.mediaPlayer;
import static com.example.admin.mp3player.Fragments.MusicFragment.musicPlay;
import static com.example.admin.mp3player.Fragments.MusicFragment.name;
import static com.example.admin.mp3player.Fragments.MusicFragment.num;
import static com.example.admin.mp3player.Fragments.MusicFragment.pos;
import static com.example.admin.mp3player.Fragments.MusicFragment.pos1;
import static com.example.admin.mp3player.Fragments.MusicFragment.posMusic;


public class PlayActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener {

    public static TextView tvNamePlay, tvArtistRun, tvRemainTime;
    public static ImageView prev, play, next, playImg, ivBackground;
    public static SeekBar seekBar;
    private int startTime;
    public static int finalTime;
    private Handler myHandler = new Handler();
    private int count = 0;
    public static RelativeLayout relayLayout;
    public static BlurBuilder blurBuilder = new BlurBuilder();
    private Bitmap mBit;
    private Bitmap bMap;
    private int position1, position;


    public PlayActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play);
        relayControl.setVisibility(View.INVISIBLE);

        tvNamePlay = (TextView) findViewById(R.id.nameRun);
        tvArtistRun = (TextView) findViewById(R.id.artistRun);
        tvRemainTime = (TextView) findViewById(R.id.timeRemain);
        ivBackground = (ImageView) findViewById(R.id.iv_music_background);
        relayLayout = (RelativeLayout) findViewById(R.id.layoutBlur);
        prev = (ImageView) findViewById(R.id.prev);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        playImg = (ImageView) findViewById(R.id.imgPlay);


        position1 = posMusic;
        position = posMusic;

        finalTime = mService.getDuration();
        startTime = mService.getCurrentPosition();

        tvNamePlay.setText(name);
        tvArtistRun.setText(artist);
        if (bitmap != null) {
            playImg.setImageBitmap(bitmap);
            ivBackground.setImageBitmap(blurBuilder.blur(this, bitmap));

        } else {
            bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);
            ivBackground.setImageBitmap(blurBuilder.blur(this, bMap));
        }

        if (nextBoole) {
            tvNamePlay.setText(mList.get(pos1).getTitle());
            tvArtistRun.setText(mList.get(pos1).getArtist());
            Bitmap map1 = adapterList.getAlbumart(Uri.parse(mList.get(pos1).getPath()));
            if (map1 != null) {
                ivBackground.setImageBitmap(map1);
            } else {
                ivBackground.setImageResource(R.drawable.ivmusicbackground);
            }
        }

        if (prevBoole) {
            tvNamePlay.setText(mList.get(pos).getTitle());
            tvArtistRun.setText(mList.get(pos).getArtist());
            Bitmap map1 = adapterList.getAlbumart(Uri.parse(mList.get(pos).getPath()));
            if (map1 != null) {
                ivBackground.setImageBitmap(map1);
            } else {
                ivBackground.setImageResource(R.drawable.ivmusicbackground);
            }
        }

        if (check) {
            inits(number);
        }
        if (checkPrev) {
            inits(numberPrev);
        }

        prev.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setClickable(false);
        seekBar.setMax(finalTime);
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        myHandler.postDelayed(UpdateSongTime, 100);
        seekBar.setOnSeekBarChangeListener(this);
        if (seekBar.isShown() | seekBar.isEnabled()) {
            Log.d("SHOW", "true");
        }

        tvRemainTime.setText(String.format("%d min %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );


        // seekBar.setOnSeekBarChangeListener(this);


        if (num % 2 == 0) {
            play.setImageResource(R.drawable.icnotifpause);
        }
        Log.d("AAAAAAAAAAAAAAAAAAAA", num + "");

    }

    private void inits(int number) {
        tvNamePlay.setText(mList.get(number).getTitle());
        tvArtistRun.setText(mList.get(number).getArtist());
        Bitmap bit = adapterList.getAlbumart(Uri.parse(mList.get(number).getPath()));

        if (bit != null) {
            ivBackground.setImageBitmap(blurBuilder.blur(this, bit));
            playImg.setImageBitmap(bit);
        } else {
            Bitmap Bit = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);

            ivBackground.setImageBitmap(blurBuilder.blur(this, Bit));
            playImg.setImageResource(R.drawable.ivmusic);

        }
    }

    @Override
    public void onClick(View v) {

        count++;

        switch (v.getId()) {

            case R.id.prev:
                if (mService != null) {

                    if (position > 0) {
                        position = position - 1;
                    } else
                        position = 0;

                    setImagePlay(position);

                    try {
                        mService.play(position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mService.start();
                    tvNamePlay.setText(mList.get(position).getTitle());
                    tvArtistRun.setText(mList.get(position).getArtist());
                    myHandler.postDelayed(UpdateSongTime, 100);
                    seekBar.setOnSeekBarChangeListener(this);
                    Bitmap bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));
                    if (bitmap != null) {
                        playImg.setImageBitmap(bitmap);
                        ivBackground.setImageBitmap(blurBuilder.blur(this, bitmap));
                        remoteViews.setImageViewBitmap(R.id.notiImage, bitmap);

                    } else {
                        bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);
                        ivBackground.setImageBitmap(blurBuilder.blur(this, bMap));
                        remoteViews.setImageViewBitmap(R.id.notiImage, bMap);

                    }
                }
                break;

            case R.id.play:
                if (mService != null) {
                    if ((count > 2 && count % 2 == 0) || count == 1) {
                        play.setImageResource(R.drawable.icnotifplay);
                        mService.pause();
                    } else {
                        play.setImageResource(R.drawable.icnotifpause);
                        mService.start();
                    }
                }
                if (!mService.isPlaying()) {
                    play.setImageResource(R.drawable.icnotifplay);
                }
                break;

            case R.id.next:

                if (mService != null) {

                    if (position1 < mList.size()) {
                        position1 = position1 + 1;
                    } else position1 = 0;

                    setImagePlay(position1);

                    try {
                        mService.play(position1);
                        mService.setOnBufferingUpdateListener();
                        tvNamePlay.setText(mList.get(position1).getTitle());
                        tvArtistRun.setText(mList.get(position1).getArtist());
                        myHandler.postDelayed(UpdateSongTime, 100);
                        seekBar.setOnSeekBarChangeListener(this);
                        Bitmap bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position1).getPath()));
                        if (bitmap != null) {
                            playImg.setImageBitmap(bitmap);
                            ivBackground.setImageBitmap(blurBuilder.blur(this, bitmap));

                        } else {
                            bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);
                            ivBackground.setImageBitmap(blurBuilder.blur(this, bMap));
                        }

                    } catch (IndexOutOfBoundsException e) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

            default:
                break;

        }

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mService != null && mService.getCurrentPosition() <= mService.getDuration() - 1000) {
                startTime = mService.getCurrentPosition();

                tvRemainTime.setText(String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress(mService.getCurrentPosition());
                Log.d("POSSITION", mService.getCurrentPosition() + "\t" + mService.getDuration());
                if (mService.getCurrentPosition() > mService.getDuration() - 1000) {
                    mService.onDestroy();

                    if (position1 >= mList.size()) {
                        position1 = 0;
                    } else position1 += 1;

                    try {
                        Uri uri1 = Uri.parse(mList.get(position1).getPath());
                        mService.play(position1);

                        tvNamePlay.setText(mList.get(position1).getTitle());
                        tvArtistRun.setText(mList.get(position1).getArtist());

                        mService.start();

                        startTime = 0;
                        finalTime = mService.getDuration();

                        setImagePlay(position1);
                        seekBar.setMax(finalTime);
                        seekBar.setProgress(mService.getCurrentPosition());
                    } catch (Exception e) {
                    }


                }
                myHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress;
            if (time > mService.getDuration())
                time = mService.getDuration();

            mService.pause();
            mService.seek(time);
            mService.start();
            mService.setOnBufferingUpdateListener();


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

    private void setImagePlay(int position) {
        if (position < mList.size()) {

            mBit = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

            if (mBit != null) {
                playImg.setImageBitmap(mBit);
                musicPlay.setImageBitmap(mBit);
                relayLayout.setBackgroundDrawable(
                        new BitmapDrawable(blurBuilder.blur(this, mBit)));

            } else {
                playImg.setImageResource(R.drawable.ivmusic);
                relayLayout.setBackgroundResource(R.drawable.background);

            }

        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener( new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event )
//            {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    getActivity().onBackPressed();
//                    relayControl.setVisibility(View.VISIBLE);
//                    return true;
//                }
//                return false;
//            }
//        } );
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        relayControl.setVisibility(View.VISIBLE);
        finish();
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
