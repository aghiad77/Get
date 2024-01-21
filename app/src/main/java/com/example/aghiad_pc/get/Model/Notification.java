package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("text")
    String text;

    @SerializedName("created_at")
    String created_at;

    public Notification(int id, String title, String text, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.created_at = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.created_at = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return created_at;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.title+";"+this.text+";"+this.created_at+"@result@";
        else
            return this.title+";"+this.text+";"+this.created_at;
    }
}
