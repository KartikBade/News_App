package com.example.newsappv01;

public class Story {

    private String title;
    private String section;
    private String writer;
    private String date;
    private String link;

    Story(String title, String section, String writer, String date, String link) {
        this.title = title;
        this.section = section;
        this.writer = writer;
        this.date = date;
        this.link = link;
    }

    String getTitle() {
        return title;
    }

    String getSection() {
        return section;
    }

    String getWriter() {
        return writer;
    }

    String getDate() {
        return date;
    }

    String getLink() {
        return link;
    }

}
