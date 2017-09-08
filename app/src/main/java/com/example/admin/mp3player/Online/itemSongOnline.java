package com.example.admin.mp3player.Online;

import java.util.List;

/**
 * Created by VuMinhDuc on 17/05/04.
 */

public class itemSongOnline {

    private String msg;
    private List<Data> data;

    public itemSongOnline() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    //gson la doi tuong dung de chuyen doi dang doi tuong tu json sang object hoawc nguoc lai

    public static final class Data {

        private String id;

        private String name;

        private String artist;

        private String cover;

        private List<String> source_list;

        private String source_base;

        private String lyric;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<String> getSource_list() {
            return source_list;
        }

        public void setSource_list(List<String> source_list) {
            this.source_list = source_list;
        }

        public String getSource_base() {
            return source_base;
        }

        public void setSource_base(String source_base) {
            this.source_base = source_base;
        }

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
