package com.example.admin.mp3player.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.mp3player.Common.BlurBuilder;
import com.example.admin.mp3player.Fragments.AblumFragment;
import com.example.admin.mp3player.Fragments.MusicFragment;
import com.example.admin.mp3player.Fragments.NgheSiFragment;
import com.example.admin.mp3player.Fragments.ThuMucFragment;
import com.example.admin.mp3player.R;

import java.io.IOException;

import static com.example.admin.mp3player.Activity.PlayActivity.ivBackground;
import static com.example.admin.mp3player.Activity.PlayActivity.playImg;
import static com.example.admin.mp3player.Activity.PlayActivity.tvArtistRun;
import static com.example.admin.mp3player.Activity.PlayActivity.tvNamePlay;
import static com.example.admin.mp3player.Fragments.MusicFragment.adapterList;
import static com.example.admin.mp3player.Fragments.MusicFragment.bitmap;
import static com.example.admin.mp3player.Fragments.MusicFragment.mList;
import static com.example.admin.mp3player.Fragments.MusicFragment.mService;
import static com.example.admin.mp3player.Fragments.MusicFragment.musicPlay;
import static com.example.admin.mp3player.Fragments.MusicFragment.num;
import static com.example.admin.mp3player.Fragments.MusicFragment.pos;
import static com.example.admin.mp3player.Fragments.MusicFragment.pos1;
import static com.example.admin.mp3player.Fragments.MusicFragment.posMusic;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener {

    public ViewPager vpMusic;
    public static TabLayout tabLayout;
    private MainActivity.FragmentAdapter mAdapter;
    public static RelativeLayout relativeLayout;
    public static ImageView imBackground;
    private NavigationView navigationView;
    public static TextView tvNameArtist, tvNameMusic, tvShowingName, tvShowingArtist;
    private ImageView ivSearch;
    private LinearLayout linearLayout;
    public static RelativeLayout relayControl, relatInfo, relay;
    public static SeekBar seekBar;
    public static ImageView play1, showingBackground;
    public static TextView tvNameOn, tvArtistOn;
    public static int count1 = 0;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView showingPrev;
    private ImageView showingNext;
    private ImageView showingPlay;
    private BlurBuilder blurBuilder = new BlurBuilder();
    private Bitmap bMap;
    private Handler myHandler = new Handler();
    private BroadcastReceiver receiver;
    public static int number;
    public static boolean check, checkPrev;
    public static int numberPrev;
    public static boolean nextBoole, prevBoole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fullAct();
        setContentView(R.layout.activity_main);
        initsView();
        setEvents();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Universal Music Player");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        inflateHeader();

        IntentFilter filter = new IntentFilter("NEXT");
        IntentFilter filterPrev = new IntentFilter("PREVIOUS");
        //tạo bộ lắng nghe
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processReceive(context, intent);
            }
        };

        //đăng ký bộ lắng nghe vào hệ thống
        registerReceiver(receiver, filter);
        registerReceiver(receiver, filterPrev);

    }


    protected void onDestroy() {
        super.onDestroy();
        //hủy bỏ đăng ký khi tắt ứng dụng
        unregisterReceiver(receiver);
    }

    public void processReceive(Context context, Intent intent) {
        if (intent.getAction() == "NEXT") {
            Bundle bundle = intent.getExtras();
            number = bundle.getInt("nextBoole");
            check = bundle.getBoolean("check");
            inits(number);
        }
        if (intent.getAction() == "PREVIOUS") {
            Bundle bundle1 = intent.getExtras();
            numberPrev = bundle1.getInt("previous");
            checkPrev = bundle1.getBoolean("checkPrev");
            inits(numberPrev);

        }

    }

    private void inits(int number) {
        tvNameOn.setText(mList.get(number).getTitle());
        tvArtistOn.setText(mList.get(number).getArtist());
        tvNameMusic.setText(mList.get(number).getTitle());
        tvNameArtist.setText(mList.get(number).getArtist());
        tvShowingName.setText(mList.get(number).getTitle());
        tvShowingArtist.setText(mList.get(number).getArtist());
        try {
            tvNamePlay.setText(mList.get(number).getTitle());
            tvArtistRun.setText(mList.get(number).getArtist());
        } catch (NullPointerException e) {}

        Bitmap bit = adapterList.getAlbumart(Uri.parse(mList.get(number).getPath()));
        if (bit != null) {
            musicPlay.setImageBitmap(bit);
            showingBackground.setImageBitmap(blurBuilder.blur(getBaseContext(), bit));
            imBackground.setImageBitmap(bit);
            try {
            ivBackground.setImageBitmap(blurBuilder.blur(this, bit));
            playImg.setImageBitmap(bit);
            } catch (NullPointerException e) {}

        } else {
            Bitmap Bit = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);
            musicPlay.setImageResource(R.drawable.ivmusic);
            showingBackground.setImageBitmap(blurBuilder.blur(getBaseContext(), Bit));
            imBackground.setImageResource(R.drawable.ivmusic);
            try {
            ivBackground.setImageBitmap(blurBuilder.blur(this, Bit));
            playImg.setImageResource(R.drawable.ivmusic);
            } catch (NullPointerException e) {}

        }
    }

    private void inflateHeader() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvNameArtist = (TextView) headerView.findViewById(R.id.tv_nameArtist);
        tvNameMusic = (TextView) headerView.findViewById(R.id.tv_nameMusic);
        imBackground = (ImageView) headerView.findViewById(R.id.iv_background);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nowPlaying) {
            // Handle the camera action
        } else if (id == R.id.nav_library) {

        } else if (id == R.id.nav_playlist) {
            Intent intentPlay = new Intent(MainActivity.this, PlaylistActivity.class);
            startActivity(intentPlay);
        } else if (id == R.id.nav_playingQueue) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initsView() {
        vpMusic = (ViewPager) findViewById(R.id.vp_music);
        tabLayout = (TabLayout) findViewById(R.id.tabLay);
        relativeLayout = (RelativeLayout) findViewById(R.id.main);
        play1 = (ImageView) findViewById(R.id.play1);
        musicPlay = (ImageView) findViewById(R.id.musicPlay);
        relatInfo = (RelativeLayout) findViewById(R.id.relayInfo);
        relay = (RelativeLayout) findViewById(R.id.control);
        showingPrev = (ImageView) findViewById(R.id.showing_prev);
        showingPlay = (ImageView) findViewById(R.id.showing_play);
        showingNext = (ImageView) findViewById(R.id.showing_next);
        showingBackground = (ImageView) findViewById(R.id.showing_background);
        tvShowingName = (TextView) findViewById(R.id.showing_name);
        tvShowingArtist = (TextView) findViewById(R.id.showing_artist);
        seekBar = (SeekBar) findViewById(R.id.showing_seekbar);
        showingPrev.setOnClickListener(this);
        showingPlay.setOnClickListener(this);
        showingNext.setOnClickListener(this);


        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        relay.setVisibility(View.VISIBLE);

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        relay.setVisibility(View.INVISIBLE);

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        if (num % 2 == 0) {
            showingPlay.setImageResource(R.drawable.icnotifpause);
        }
        Log.d("AAAAAAAAAAAAAAAAAAAA", num + "");

        if (bitmap != null) {
            showingBackground.setImageBitmap(blurBuilder.blur(getBaseContext(), bitmap));

        } else {
            bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ivmusic);
            showingBackground.setImageBitmap(blurBuilder.blur(getBaseContext(), bMap));
        }

//        play1.setOnClickListener(this);

        relayControl = (RelativeLayout) findViewById(R.id.bottom_sheet);
        relayControl.setVisibility(View.GONE);
        linearLayout = (LinearLayout) findViewById(R.id.mainLayout);
        relayControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity playActivity1 = new PlayActivity();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                MusicFragment musicFragment = new MusicFragment();

                if (musicFragment.isVisible()) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(musicFragment);
                    fragmentTransaction.commit();
                }

                Intent intent = new Intent(MainActivity.this, com.example.admin.mp3player.Activity.PlayActivity.class);
                startActivity(intent);
            }
        });


        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fullAct() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void setEvents() {
        mAdapter = new MainActivity.FragmentAdapter(getSupportFragmentManager());
        vpMusic.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(vpMusic);
        tabLayout.setSelectedTabIndicatorColor(Color.CYAN);
        tabLayout.getTabAt(0).setText("BÀI HÁT");
        tabLayout.getTabAt(1).setText("ALBUM");
        tabLayout.getTabAt(2).setText("NGHỆ SĨ");
        tabLayout.getTabAt(3).setText("THƯ MỤC");
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#4DD0E1"));
        vpMusic.addOnPageChangeListener(this);


        tvNameOn = (TextView) findViewById(R.id.name1);
        tvArtistOn = (TextView) findViewById(R.id.artist1);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.tranlate);
        tvNameOn.startAnimation(animation);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

                return new MusicFragment();
            }
            if (position == 1) {
                return new AblumFragment();
            }
            if (position == 2) {
                return new NgheSiFragment();
            }
            if (position == 3) {
                return new ThuMucFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onClick(View v) {

        count1++;

        switch (v.getId()) {

            case R.id.showing_prev:

                mService.onDestroy();

                prevBoole = true ;

                if (pos > 0) {
                    pos = pos - 1;
                } else
                    pos = 0;

                setImagePlay(pos);
                try {
                    mService.play(pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myHandler.postDelayed(UpdateSongTime, 100);
                Bitmap map = adapterList.getAlbumart(Uri.parse(mList.get(pos).getPath()));
                tvNameOn.setText(mList.get(pos).getTitle());
                tvArtistOn.setText(mList.get(pos).getArtist());
                if (mList.get(pos).getPath() != null) {
                    if (map != null) {
                        showingBackground.setImageBitmap(blurBuilder.blur(this, map));
                        imBackground.setImageBitmap(map);
                    } else {
                        showingBackground.setImageResource(R.drawable.ivmusicbackground);
                        imBackground.setImageResource(R.drawable.ivmusic);
                    }
                }
                tvShowingName.setText(mList.get(pos).getTitle());
                tvShowingArtist.setText(mList.get(pos).getArtist());
                tvNameMusic.setText(mList.get(pos).getTitle());
                tvNameArtist.setText(mList.get(pos).getArtist());

                mService.start();
                break;

            case R.id.showing_play:

                num++;

                if ((count1 > 2 && count1 % 2 == 0) || count1 == 1) {
                    showingPlay.setImageResource(R.drawable.icnotifplay);
                    mService.pause();
                } else {
                    showingPlay.setImageResource(R.drawable.icnotifpause);
                    mService.start();
                }

                break;

            case R.id.showing_next:

               mService.onDestroy();
                nextBoole = true ;

                pos1 = pos1 + 1;

                if (pos1 == mList.size()) {
                    pos1 = 0;
                }

                setImagePlay(pos1);

                //observable

                try {
                    mService.play(pos1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myHandler.postDelayed(UpdateSongTime, 100);
                tvNameOn.setText(mList.get(pos1).getTitle());
                tvArtistOn.setText(mList.get(pos1).getArtist());
                if (mList.get(pos1).getPath() != null) {
                    Bitmap map1 = adapterList.getAlbumart(Uri.parse(mList.get(pos1).getPath()));
                    if (map1 != null) {
                        showingBackground.setImageBitmap(blurBuilder.blur(this, map1));
                        imBackground.setImageBitmap(map1);
                    } else {
                        showingBackground.setImageResource(R.drawable.ivmusicbackground);
                        imBackground.setImageResource(R.drawable.ivmusic);
                    }
                }
                tvShowingName.setText(mList.get(pos1).getTitle());
                tvShowingArtist.setText(mList.get(pos1).getArtist());
                tvNameMusic.setText(mList.get(pos1).getTitle());
                tvNameArtist.setText(mList.get(pos1).getArtist());

                Log.d("NAMEEEEEEEEEEEEEEEEE", mList.get(pos1).getTitle() + "\t" + pos1);
                mService.start();

                break;

            default:
                break;

        }
    }

    private void setImagePlay(int position) {
        bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

        if (bitmap != null) {
            musicPlay.setImageBitmap(bitmap);
        } else musicPlay.setImageResource(R.drawable.ivmusic);
    }

    private int startTime;
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mService != null && mService.getCurrentPosition() <= mService.getDuration() - 1000) {
                startTime = mService.getCurrentPosition();

                seekBar.setProgress(mService.getCurrentPosition());
                Log.d("POSSITION", mService.getCurrentPosition() + "\t" + mService.getDuration());
                if (mService.getCurrentPosition() > mService.getDuration() - 1000) {
                    mService.onDestroy();

                    if (posMusic >= mList.size()) {
                        posMusic = 0;
                    } else posMusic += 1;

                    try {
                        mService.play(posMusic);

                        tvArtistOn.setText(mList.get(posMusic).getTitle());
                        tvShowingArtist.setText(mList.get(posMusic).getArtist());

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

}
