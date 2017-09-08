package com.example.admin.mp3player.Fragments;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.admin.mp3player.Activity.MainActivity;
import com.example.admin.mp3player.Adapter.AdapterList;
import com.example.admin.mp3player.Common.Artist;
import com.example.admin.mp3player.Common.BlurBuilder;
import com.example.admin.mp3player.Common.Item;
import com.example.admin.mp3player.Common.Playlist;
import com.example.admin.mp3player.Common.ServiceMedia;
import com.example.admin.mp3player.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.admin.mp3player.Activity.MainActivity.imBackground;
import static com.example.admin.mp3player.Activity.MainActivity.relayControl;
import static com.example.admin.mp3player.Activity.MainActivity.seekBar;
import static com.example.admin.mp3player.Activity.MainActivity.showingBackground;
import static com.example.admin.mp3player.Activity.MainActivity.tvArtistOn;
import static com.example.admin.mp3player.Activity.MainActivity.tvNameArtist;
import static com.example.admin.mp3player.Activity.MainActivity.tvNameMusic;
import static com.example.admin.mp3player.Activity.MainActivity.tvNameOn;
import static com.example.admin.mp3player.Activity.MainActivity.tvShowingArtist;
import static com.example.admin.mp3player.Activity.MainActivity.tvShowingName;
import static com.example.admin.mp3player.Online.adapterMusic.mPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements AdapterList.Imusic
        , AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener{

    private AdapterList mAdapter;
    public static List<Item> mList;
    public static List<Artist> listArtist;
    public static List<Playlist> listPlaylist;
    private ListView lv;
    protected static String link;
    public static ImageView musicPlay;
    private static final String TAG = MusicFragment.class.getSimpleName();
    public static long id;
    public static int pos;
    public static int pos1;
    public static String name, artist;
    public static MediaPlayer mediaPlayer;
    public static int num = 0;
    public static Bitmap bitmap;
    public static AdapterList adapterList;
    public static Bundle bundle;
    private int count = 0;
    private BlurBuilder blurBuilder = new BlurBuilder();
    private ServiceConnection mConn;
    public static ServiceMedia mService;
    public static int totalTime;
    private Handler myHandler = new Handler();
    public static int posMusic;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_music, container, false);
        mList = new ArrayList<>();
        listArtist = new ArrayList<>();
        listPlaylist = new ArrayList<>();
        ListMusic();
        lv = (ListView) view.findViewById(R.id.lvList);
        // Inflate the layout for this fragment

        mediaPlayer = new MediaPlayer();
        mAdapter = new AdapterList(this, (MainActivity) getActivity(), getContext());
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);
        if (mediaPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        lv.smoothScrollToPosition(mList.size());
        startService();
        requestService();
        return view;
    }

    private void startService() {
        Intent intent = new Intent();
        intent.setClass(getContext(), ServiceMedia.class);
        getContext().startService(intent);
    }

    private void requestService() {
        //tao ra cau connection den service truoc
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceMedia.BinderService binderService =
                        (ServiceMedia.BinderService) service;
                mService = binderService.getServiceMedia();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        //gui yeu cau
        Intent intent = new Intent();
        intent.setClass(getContext(), ServiceMedia.class);

        getContext().bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        getContext().unbindService(mConn);
        super.onDestroy();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Item getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        posMusic = position ;
        Log.d("PosMusic", posMusic + "");

        adapterList = new AdapterList(this, (MainActivity) getActivity(),
                getContext());

        bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

        if (bitmap != null) {
            imBackground.setImageBitmap(bitmap);
            try {
                showingBackground.setImageBitmap(blurBuilder.blur(getContext(), bitmap));
            } catch (NullPointerException e) {
            }
        } else {
            imBackground.setImageResource(R.drawable.ivmusicbackground);
            showingBackground.setImageResource(R.drawable.ivmusicbackground);
        }
        try {
            mService.play(position);
            totalTime = mService.getDuration();
            seekBar.setClickable(false);
            seekBar.setMax(totalTime);
            seekBar.setProgress(mService.getCurrentPosition());
            myHandler.postDelayed(UpdateSongTime, 100);
            seekBar.setOnSeekBarChangeListener(this);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Khong the mo bai hat nay", Toast.LENGTH_LONG).show();
        }
        relayControl.setVisibility(View.VISIBLE);
        setImagePlay(position);
        mPlayer = new MediaPlayer();

        count++;


        bundle = new Bundle();
        bundle.putString("key", mList.get(position).getTitle());
        bundle.putString("key1", mList.get(position).getArtist());
        bundle.putInt("key2", position);
        bundle.putInt("key3", mList.size());

        name = mList.get(position).getTitle();
        artist = mList.get(position).getArtist();
        pos = position;
        pos1 = position;

        tvNameOn.setText(name);
        tvArtistOn.setText(artist);

        tvNameMusic.setText(name);
        tvNameArtist.setText(artist);

        tvShowingName.setText(name);
        tvShowingArtist.setText(artist);

        if (mService.isPlaying()) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            musicPlay.startAnimation(animation);
        } else {
        }


//        play1.setImageResource(R.drawable.icnotifpause);

    }

    private void ListMusic() {
        String list[] = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.ALBUM_ID,
        };

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                list, null, null, null);

        Cursor cursor1 = getActivity().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                        MediaStore.Audio.Artists.ARTIST}, null, null, null);
        Cursor cursor2 = getActivity().getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Playlists.DATA,
                        MediaStore.Audio.Playlists.NAME},
                null, null, null);
        if (cursor == null && cursor1 == null && cursor2 == null) {
            return;
        }
        int indexTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int indexData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int indexArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int indexAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int indexComposer = cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER);
        int indexAlbumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

        int indexNumAl = cursor1.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
        int indexNumTrack = cursor1.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
        int indexArtistAlbum = cursor1.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

        int indexPlayListNum = cursor2.getColumnIndex(MediaStore.Audio.Playlists._COUNT);
        int indexPlayListName = cursor2.getColumnIndex(MediaStore.Audio.Playlists.NAME);
        int indexPlayListData = cursor2.getColumnIndex(MediaStore.Audio.Playlists.DATA);

        cursor.moveToFirst();
        cursor1.moveToFirst();
        cursor2.moveToFirst();

        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexData);
            String title = cursor.getString(indexTitle);
            String artist = cursor.getString(indexArtist);
            String album = cursor.getString(indexAlbum);
            String composer = cursor.getString(indexComposer);
//            String numAlb = cursor.getString(indexNumAl) ;
//            String numTrak = cursor.getString(indexNumTrack) ;
            id = cursor.getLong(indexAlbumId);

            Log.d(TAG, "path" + path);
            Log.d(TAG, "title" + title);
            Log.d(TAG, "artist" + artist);
            Log.d(TAG, "album" + album);
            Log.d(TAG, "composer" + composer);
            Log.d(TAG, "idddddddd" + id);
//            Log.d(TAG, "Albummmmmmmmmm" + numAlb);
//            Log.d(TAG, "Tracklkkkkkk" + numTrak);

            Log.d(TAG, "TAG" + "=========================================");

            mList.add(new Item(path, title, artist, album, composer, id));

            ContentResolver musicResolve = getActivity().getContentResolver();
            Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            Cursor music = musicResolve.query(smusicUri, null         //should use where clause(_ID==albumid)
                    , null, null, null);

            cursor.moveToNext();

        }

        while (!cursor1.isAfterLast()) {

            String numAl = cursor1.getString(indexNumAl);
            String numTrack = cursor1.getString(indexNumTrack);
            String artist = cursor1.getString(indexArtistAlbum);
            listArtist.add(new Artist(numAl, numTrack, artist));
            cursor1.moveToNext();
            Log.d("Num", numAl + "\t" + numTrack);

        }

        while (!cursor2.isAfterLast()) {

            String name = cursor2.getString(indexPlayListName);
//            int count = cursor2.getInt(indexPlayListNum);
            String data = cursor2.getString(indexPlayListData);

            listPlaylist.add(new Playlist(name, data));

            cursor2.moveToNext();

            Log.d("Playlist", name + "\t" + data);

        }


        cursor.close();
        cursor1.close();
        cursor2.close();
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mService != null) {
                if (mService.getCurrentPosition() < mService.getDuration())
                seekBar.setProgress(mService.getCurrentPosition());
                if (mService.getCurrentPosition() >= mService.getDuration() - 1000) {
                    mService.onDestroy();
                    mService = null;

                    mService = new ServiceMedia();

                    if (posMusic >= mList.size()) {
                        posMusic = 0;
                    } else posMusic += 1;

                    try {
                        Uri uri1 = Uri.parse(mList.get(posMusic).getPath());
                        mService.play(posMusic);

                        tvShowingName.setText(mList.get(posMusic).getTitle());
                        tvShowingArtist.setText(mList.get(posMusic).getArtist());

                        mService.start();

                        setImagePlay(posMusic);
                        seekBar.setMax(totalTime);
                        seekBar.setProgress(mService.getCurrentPosition());
                    } catch (Exception e) {
                    }


                }
                myHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress;
            if (time > mService.getDuration())
                time = mService.getDuration();

            mService.pause();
            mService.seek(time);
            mService.start();
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mService != null) {
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
            mService.mManagerPlayerOffline.mPlay.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    seekBar.setSecondaryProgress(percent);
                }
            });
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setImagePlay(int position) {
        bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

        if (bitmap != null) {
            musicPlay.setImageBitmap(bitmap);
        } else musicPlay.setImageResource(R.drawable.ivmusic);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }
}
