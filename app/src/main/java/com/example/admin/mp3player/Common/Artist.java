package com.example.admin.mp3player.Common;

/**
 * Created by minhd on 17/07/27.
 */

public class Artist {
    private String numAl ;
    private String numTrack ;
    private String artist ;

    public Artist(String numAl, String numTrack, String artist) {
        this.numAl = numAl;
        this.numTrack = numTrack;
        this.artist = artist ;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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
}
