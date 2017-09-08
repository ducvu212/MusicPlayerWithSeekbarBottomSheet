package com.example.admin.mp3player.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.mp3player.Adapter.AdapterArtist;
import com.example.admin.mp3player.Adapter.AdapterList;
import com.example.admin.mp3player.Common.Artist;
import com.example.admin.mp3player.Common.Item;
import com.example.admin.mp3player.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

import static com.example.admin.mp3player.Fragments.MusicFragment.mList;


public class SearchActivity extends AppCompatActivity implements AdapterList.Imusic, TextWatcher, AdapterArtist.Imusic {

    private ListView listSerch, listArtist, listAlbums;
    private AdapterList adapter ;
    private Disposable disposableOrigin;
    private final List<Item> filteredList = new ArrayList<>();
    private final List<Item> filteredListArtist = new ArrayList<>();
    private final List<Item> filteredListAlbum = new ArrayList<>();
    private AdapterArtist adapterArtist;
    private TextView tvSong, tvAlbum,tvArtist ;
    private AdapterList adapterAlbums;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left48));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        findViewByIds() ;


    }

    private void findViewByIds() {
        EditText editText = (EditText) findViewById(R.id.edt_searchFrag);
        editText.addTextChangedListener(this);
        listSerch = (ListView) findViewById(R.id.list_searchAct) ;
        listArtist = (ListView) findViewById(R.id.list_artistAct) ;
        listAlbums = (ListView) findViewById(R.id.list_albumAct) ;
        tvSong = (TextView) findViewById(R.id.tv_songArtist) ;
        tvArtist = (TextView) findViewById(R.id.tv_Artist) ;
        tvAlbum = (TextView) findViewById(R.id.tv_albumsAct) ;

        tvAlbum.setVisibility(View.INVISIBLE);
        tvArtist.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true ;
    }

    private List<Item> filter(List<Item> mList, String query) {
        query = query.toLowerCase();

        for(Item item : mList){
            if ((convert(item.getTitle().toLowerCase())).contains(query)) {
                Log.d("TAGGNAME", convert(item.getTitle().toLowerCase()));

                filteredList.add(item);
                adapter = new AdapterList(this, this, this);
                listSerch.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            for (int j = 0; j < filteredList.size(); j++) {
                filteredListArtist.add(item);
                Log.d("TAGGARTIST", convert(item.getArtist().toLowerCase()));

                adapterArtist = new AdapterArtist(this, this, this);
                listArtist.setAdapter(adapterArtist);
                adapterArtist.notifyDataSetChanged();
                if (filteredListArtist.size() != 0) {
                    tvArtist.setVisibility(View.VISIBLE);
                }

                filteredListAlbum.add(item);
                Log.d("TAGGALBUMS", convert(item.getAlbum().toLowerCase()));

                adapterAlbums = new AdapterList(this, this, this) ;
                listSerch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (filteredListAlbum.size() != 0) {
                    tvArtist.setVisibility(View.VISIBLE);
                }
            }

        }
        Log.d("TAGGNAME", "========================");

        return filteredList;
    }

    private String convert(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Item getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filteredList.clear();

        if (disposableOrigin != null && !disposableOrigin.isDisposed()) {
            disposableOrigin.dispose();
        }
        String content = s.toString().trim();
        filter(mList, content) ;
    }

    @Override
    public int getCountArtist() {
        return 0;
    }

    @Override
    public Artist getItemArtist(int position) {
        return null;
    }
}
