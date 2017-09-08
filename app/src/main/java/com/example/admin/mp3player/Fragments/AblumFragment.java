package com.example.admin.mp3player.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.admin.mp3player.Adapter.AdapterAlbums;
import com.example.admin.mp3player.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AblumFragment extends Fragment {


    private GridView gridView;

    public AblumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ablum, container, false);
        gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new AdapterAlbums(getContext()));
        gridView.smoothScrollToPosition(0);

        return view ;
    }

}
