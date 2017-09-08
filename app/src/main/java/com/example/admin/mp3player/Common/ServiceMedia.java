package com.example.admin.mp3player.Common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SeekBar;

import com.example.admin.mp3player.Activity.PlayActivity;
import com.example.admin.mp3player.Fragments.MusicFragment;
import com.example.admin.mp3player.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.example.admin.mp3player.Activity.PlayActivity.blurBuilder;
import static com.example.admin.mp3player.Activity.PlayActivity.seekBar;
import static com.example.admin.mp3player.Activity.PlayActivity.tvArtistRun;
import static com.example.admin.mp3player.Activity.PlayActivity.tvNamePlay;
import static com.example.admin.mp3player.Activity.PlayActivity.tvRemainTime;
import static com.example.admin.mp3player.Fragments.MusicFragment.adapterList;
import static com.example.admin.mp3player.Fragments.MusicFragment.bitmap;
import static com.example.admin.mp3player.Fragments.MusicFragment.mList;
import static com.example.admin.mp3player.Fragments.MusicFragment.mService;
import static com.example.admin.mp3player.Fragments.MusicFragment.posMusic;


public class ServiceMedia extends Service implements SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "ServiceMedia";
    public ManagerAudioOffline mManagerPlayerOffline;
    private MyBoradCast myBoradCast;
    private int currentPosition = -1;
    private NotificationCompat.Builder builder;
    public static RemoteViews remoteViews;
    private Handler myHandler = new Handler();
    private int Pos;
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        initMyBroadcast();
        mManagerPlayerOffline = new ManagerAudioOffline();
    }

    private void initMyBroadcast() {
        myBoradCast = new MyBoradCast();
        IntentFilter filter = new IntentFilter();
        IntentFilter filterNext = new IntentFilter();
        IntentFilter filterPrev = new IntentFilter();
        IntentFilter filterForward = new IntentFilter();
        IntentFilter filterRewind = new IntentFilter();

        filter.addAction("PLAY");
        filterNext.addAction("NEXT");
        filterPrev.addAction("PREVIOUS");
        filterForward.addAction("FORWARD");
        filterRewind.addAction("REWIND");

        registerReceiver(myBoradCast, filter);
        registerReceiver(myBoradCast, filterNext);
        registerReceiver(myBoradCast, filterPrev);
        registerReceiver(myBoradCast, filterForward);
        registerReceiver(myBoradCast, filterRewind);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderService(this);
    }

    public void play(int posotion) throws IOException {
        mManagerPlayerOffline.release();
        mManagerPlayerOffline.inits(mList.get(posotion).getPath());
        mManagerPlayerOffline.play();
        currentPosition = posotion;
        createNotification(posotion, true);
    }

    public void pause() {
        mManagerPlayerOffline.pause();
    }

    public void resume() {
        mManagerPlayerOffline.play();
    }

    public int getDuration() {
        return mManagerPlayerOffline.getDuration();
    }

    public void seek(int posMusic) {
        mManagerPlayerOffline.mPlay.seekTo(posMusic);
    }

    public void setOnBufferingUpdateListener() {
        try {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            });
            mManagerPlayerOffline.mPlay.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    seekBar.setSecondaryProgress(percent);
                }
            });
        } catch (NullPointerException e) {
        }

    }

    public int getCurrentPosition() {
        return mManagerPlayerOffline.getCurrentPosition();
    }

    public void release() {
        mManagerPlayerOffline.release();
    }

    public void stop() {
        mManagerPlayerOffline.stop();
        mManagerPlayerOffline.release();
        mManagerPlayerOffline = null;
    }

    public void start() {
        mManagerPlayerOffline.start();
    }

    public boolean isPlaying() {
        return mManagerPlayerOffline.mPlay.isPlaying();
    }

    private void createNotification(int posMusic, boolean isPlay) {
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.musicblue512);
//        builder.setContentText(mSongs.get(posMusic).getName());
//        builder.setContentTitle("Media Pro");
//        builder.setLargeIcon(BitmapFactory.decodeResource(
//                getResources(), R.mipmap.ic_launcher));


        Pos = posMusic;
        Log.d("PosMusic", posMusic + " duc " + Pos);

        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_nofifiction);
        remoteViews.setTextViewText(R.id.tv_name, mList.get(posMusic).getTitle());

        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.notiImage, bitmap);
        } else remoteViews.setImageViewResource(R.id.notiImage, R.drawable.ivmusic);

        if (isPlay) {
            //play
            remoteViews.setImageViewResource(R.id.btn_play, android.R.drawable.ic_media_pause);
        } else {
            //set lai icon pause
            remoteViews.setImageViewResource(R.id.btn_play, android.R.drawable.ic_media_play);
        }

        final Intent intentPlayFrag = new Intent(this, PlayActivity.class);
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intentPlayFrag, 0);

        remoteViews.setOnClickPendingIntent(R.id.layoutNoti, contentIntent);
        Intent intent = new Intent();
        Intent intentNext = new Intent();
        Intent intentPrev = new Intent();
        Intent intentForward = new Intent();
        Intent intentRewind = new Intent();

        intent.setAction("PLAY");
        intentNext.setAction("NEXT");
        intentPrev.setAction("PREVIOUS");
        intentForward.setAction("FORWARD");
        intentRewind.setAction("REWIND");

        intentNext.putExtra("nextBoole", posMusic + 1);
        intentNext.putExtra("check", true);

        intentPrev.putExtra("previous", posMusic - 1);
        intentPrev.putExtra("checkPrev", true);


        Bitmap bitMap = adapterList.getAlbumart(Uri.parse(mList.get(Pos).getPath()));
        if (bitMap != null) {
            remoteViews.setImageViewBitmap(R.id.notiImage, bitMap);
        } else remoteViews.setImageViewResource(R.id.notiImage, R.drawable.ivmusic);

        PendingIntent pendingPlay = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_play, pendingPlay);

        PendingIntent pendingNext = PendingIntent.getBroadcast(this, 1, intentNext, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_next, pendingNext);

        PendingIntent pendingPrev = PendingIntent.getBroadcast(this, 2, intentPrev, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_previous, pendingPrev);

        PendingIntent pendingForward = PendingIntent.getBroadcast(this, 2, intentForward, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_forward, pendingForward);

        PendingIntent pendingPrevious = PendingIntent.getBroadcast(this, 2, intentRewind, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_rewind, pendingPrevious);

        PendingIntent pendingClose = PendingIntent.getBroadcast(this, 2, intentRewind, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_rewind, pendingClose);

        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);

        if (isPlay) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            builder.setAutoCancel(false);
            manager.notify(111, builder.build());

        } else {
            manager.notify(111, builder.build());
        }

    }

    private class MyBoradCast extends BroadcastReceiver implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "MyBoradCast onReceive action: " + intent.getAction());
            if (intent.getAction().equals("PLAY")) {
                if (mManagerPlayerOffline.isPlaying()) {
                    mManagerPlayerOffline.pause();
                    createNotification(currentPosition, false);

                } else {
                    mManagerPlayerOffline.play();
                    createNotification(currentPosition, true);
                }
            }

            if (intent.getAction().equals("FORWARD")) {
                if (mManagerPlayerOffline.isPlaying()) {
                    mManagerPlayerOffline.seekTo(1000);
                } else {
                    mManagerPlayerOffline.play();
                    mManagerPlayerOffline.seekTo(1000);
                }
            }

            if (intent.getAction().equals("REWIND")) {
                if (mManagerPlayerOffline.isPlaying()) {
                    mManagerPlayerOffline.seekTo(-1000);
                } else {
                    mManagerPlayerOffline.play();
                    mManagerPlayerOffline.seekTo(-1000);
                }
            }

            if (intent.getAction().equals("NEXT")) {
                if (Pos < mList.size()) {
                    Pos = Pos + 1;
                } else Pos = 0;

                try {
                    play(Pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (intent.getAction().equals("PREVIOUS")) {
                if (Pos > 0) {
                    Pos = Pos - 1;
                }
                if (Pos == 0) {
                    Pos = 0;
                }

                try {
                    play(Pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    public static class BinderService extends Binder {
        private ServiceMedia mService;

        public BinderService(ServiceMedia service) {
            mService = service;
        }

        public ServiceMedia getServiceMedia() {
            return mService;
        }

    }

    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public Item getItemSong(int posMusic) {
        return mList.get(posMusic);
    }

    private int startTime;
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

                    if (posMusic >= mList.size()) {
                        posMusic = 0;
                    } else posMusic += 1;

                    try {
                        Uri uri1 = Uri.parse(mList.get(posMusic).getPath());
                        mService.play(posMusic);

                        tvNamePlay.setText(mList.get(posMusic).getTitle());
                        tvArtistRun.setText(mList.get(posMusic).getArtist());

                        mService.start();

                        startTime = 0;

                        setImagePlay(posMusic);
                        seekBar.setMax(mService.getDuration());
                        seekBar.setProgress(mService.getCurrentPosition());
                    } catch (Exception e) {
                    }


                }
                myHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    private void setImagePlay(int posMusic) {
        if (posMusic < mList.size()) {

            Bitmap mBit = adapterList.getAlbumart(Uri.parse(mList.get(posMusic).getPath()));

            if (mBit != null) {
                PlayActivity.playImg.setImageBitmap(mBit);
                MusicFragment.musicPlay.setImageBitmap(mBit);
                com.example.admin.mp3player.Activity.PlayActivity.relayLayout.setBackgroundDrawable(
                        new BitmapDrawable(blurBuilder.blur(getBaseContext(), mBit)));

            } else {
                PlayActivity.playImg.setImageResource(R.drawable.ivmusic);
                com.example.admin.mp3player.Activity.PlayActivity.relayLayout.setBackgroundResource(R.drawable.background);

            }

        }
    }

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


}
