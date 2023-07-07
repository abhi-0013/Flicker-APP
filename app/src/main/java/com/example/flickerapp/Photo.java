package com.example.flickerapp;


import java.io.Serializable;

public class Photo implements Serializable {
    private String mtitle;
    private String mauthor;
    private String mauthor_id;
    private String mlink;
    private String mImage;
    private String mtag;

    public Photo(String mtitle, String mauthor, String mauthor_id, String mlink, String mImage, String mtag) {
        this.mtitle = mtitle;
        this.mauthor = mauthor;
        this.mauthor_id = mauthor_id;
        this.mlink = mlink;
        this.mImage = mImage;
        this.mtag = mtag;
    }

    public String getTitle() {
        return mtitle;
    }

    public String getAuthor() {
        return mauthor;
    }

    public String getAuthor_id() {
        return mauthor_id;
    }

    public String getLink() {
        return mlink;
    }

    public String getImage() {
        return mImage;
    }

    public String getTag() {
        return mtag;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mtitle='" + mtitle + '\'' +
                ", mauthor='" + mauthor + '\'' +
                ", mauthor_id='" + mauthor_id + '\'' +
                ", mlink='" + mlink + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mtag='" + mtag + '\'' +
                '}';
    }
}
