package com.example.admin.mp3player.Online;

/**
 * Created by VuMinhDuc on 17/05/06.
 */

public class MusicItem {
    private String dataCode ;
    private String link;
    private String title ;

    public MusicItem(String dataCode, String link, String title) {
        this.dataCode = dataCode;
        this.link = link;
        this.title = title;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
