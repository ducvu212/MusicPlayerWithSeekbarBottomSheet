package com.example.admin.mp3player.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.mp3player.R;

import static com.example.admin.mp3player.Fragments.MusicFragment.mList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThuMucFragment extends Fragment {


    private String content;
    private String foder;

    public ThuMucFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thu_muc, container, false);

        TextView tvFoder = (TextView) view.findViewById(R.id.tv_foder);
        TextView tvPath = (TextView) view.findViewById(R.id.tv_path);
        getPath() ;
        tvFoder.setText(foder);
        tvPath.setText(getPath());
        Log.d("PATHHHHHHHHHHH ", getPath()) ;

        return view;
    }

    private String getPath() {
        for (int i = 0; i < mList.size(); i++) {
            content = mList.get(i).getPath();
            foder = "Download" ;
            if (content.endsWith("MP3") | content.endsWith("mp3")) {
                for (int j = content.length() - 1; j >= 0; j--) {
                    if (content.charAt(j) == '/') {
                        content = content.substring(0, j - 1).concat("d/");
                        foder = content.substring( content.lastIndexOf("/", j - 1) + 1) ;
                        return content ;
                    }

                }
            }
        }
        return content ;

    }
}