package com.example.admin.mp3player.Common;

public class Item {

    private int imgId ;
    private String path;
    private String title;
    private String artist;
    private String album;
    private long id;
    private String numAl;
    private String numTrack;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item(String path, String title, String artist, String album, String composer, long id) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.composer = composer;
        this.id = id;
    }

    public String getPath() {

        return path;
    }

    public String getNumAl() {
        return numAl;
    }

    public void setNumAl(String numAl) {
        this.numAl = numAl;
    }

    public String getNumTrack() {
        return numTrack;
    }

    public void setNumTrack(String numTrack) {
        this.numTrack = numTrack;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    private String composer;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
