package com.example.admin.mp3player.Common;

/**
 * Created by minhd on 17/07/28.
 */

public class Playlist {
    private String name ;
    private int count ;
    private String data ;

    public Playlist(String name
            , String data) {
        this.name = name;
        this.count = count;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
