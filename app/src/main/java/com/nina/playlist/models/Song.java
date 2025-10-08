package com.nina.playlist.models;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String thumbnailUrl;

    public Song(String id, String title, String artist, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}