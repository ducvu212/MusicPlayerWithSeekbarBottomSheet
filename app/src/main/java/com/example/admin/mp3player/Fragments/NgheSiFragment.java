package com.example.admin.mp3player.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.admin.mp3player.Adapter.AdapterArtist;
import com.example.admin.mp3player.Common.Artist;
import com.example.admin.mp3player.R;

import static com.example.admin.mp3player.Fragments.MusicFragment.listArtist;

/**
 * A simple {@link Fragment} subclass.
 */
public class NgheSiFragment extends Fragment implements AdapterArtist.Imusic {

    private ListView listView ;
    private AdapterArtist adapterArtist ;

    public NgheSiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nghe_si, container, false);
        listView = (ListView) view.findViewById(R.id.list_artistActFrag);
        adapterArtist = new AdapterArtist(this,getActivity(),getContext()) ;
        listView.setAdapter(adapterArtist);
        return  view ;
    }

    @Override
    public int getCountArtist() {
        return listArtist.size();
    }

    @Override
    public Artist getItemArtist(int position) {
        return listArtist.get(position);
    }
}
