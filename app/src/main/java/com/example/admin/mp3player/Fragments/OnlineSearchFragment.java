package com.example.admin.mp3player.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.admin.mp3player.Activity.MainActivity;
import com.example.admin.mp3player.Online.MusicItem;
import com.example.admin.mp3player.Online.adapterMusic;
import com.example.admin.mp3player.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineSearchFragment extends Fragment implements View.OnClickListener, adapterMusic.Iinter {

    private static final String TAG = OnlineSearchFragment.class.getSimpleName();
    private String link = "http://mp3.zing.vn/tim-kiem/bai-hat.html?q=";
    private EditText edtFind;
    private String music;
    private StringBuilder stringBuilder, linkBuilder, linkBuilder2;
    private String downloadLink = "http://mp3.zing.vn";
    public static List<MusicItem> mList;
    public static FrameLayout Online;
    public static adapterMusic mAdapter;
    private RecyclerView rcl_music;
    private String[] words;
    private int count;


    public OnlineSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_search, container, false);
        mList = new ArrayList<>();

        edtFind = (EditText) view.findViewById(R.id.edt_find);
        Online = (FrameLayout) view.findViewById(R.id.OnlineXML);

        stringBuilder = new StringBuilder(link);
        linkBuilder = new StringBuilder(downloadLink);
        linkBuilder2 = new StringBuilder(downloadLink);

        view.findViewById(R.id.btn_find).setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcl_music = (RecyclerView) view.findViewById(R.id.rcl_listMusic);

        rcl_music.setLayoutManager(manager);

        rcl_music.invalidate();

        PlayOnlineFragment playOnlineFragment = new PlayOnlineFragment();

        if (playOnlineFragment.isVisible()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(playOnlineFragment);
            fragmentTransaction.commit();
        }


        return view;
    }


    public void parse() {
        if (!mList.isEmpty()) {
            mList.clear();
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ///
                try {

                    Document document = Jsoup.connect(stringBuilder.toString()).get();
                    Log.d("TAGGGGGGGGGGGGG", music + "");
                    Element element = document.select("div.wrap-content").first();
                    Elements elements = element.select("div.item-song");

                    for (Element elSong : elements) {
                        String dataCode = elSong.attr("data-code").toString();
                        String link1 = elSong.select("h3").select("a").first().attr("href").toString();
                        String title = elSong.select("h3").select("a").first().attr("title").toString();
                        String artist = elSong.select("h3").select("a").next().attr("title").toString();

                        Log.d(TAG, "dataCode: " + dataCode);
                        Log.d(TAG, "link: " + link1);
                        Log.d(TAG, "title: " + title);
                        Log.d(TAG, "title: " + artist);

                        mList.add(new MusicItem(dataCode, link1, title));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setRclView();
            }
        }.execute();
    }


    @Override
    public void onClick(View v) {

        count++;
        music = edtFind.getText().toString();

        Log.d("TAGGGGGGGGG", mList.size() + "");


        if (stringBuilder.equals(link)) {
            words = music.split("\\s+");
            append();
        } else {
            stringBuilder = new StringBuilder(link);
            words = music.split("\\s+");
            append();
        }
        if(mList.size()==0){
            mList.clear();
        }
        parse();


    }

    private void append() {
        for (int i = 0; i < words.length; i++) {
            if (i != words.length - 1) {
                stringBuilder.append(String.format("%s+", words[i]));
            } else
                stringBuilder.append(String.format("%s", words[i]));
        }
        rcl_music.setAdapter(mAdapter);
    }
    private void setRclView(){
        mAdapter = new adapterMusic(this, (MainActivity) getActivity());
        rcl_music.setAdapter(mAdapter);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MusicItem getItem(int position) {
        return mList.get(position);
    }


}
