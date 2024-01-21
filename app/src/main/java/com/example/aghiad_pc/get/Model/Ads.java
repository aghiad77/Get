package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Ads {

    @SerializedName("link")
    String link;

    @SerializedName("image_url")
    String image_url;

    public Ads(String link, String image_url) {
        this.link = link;
        this.image_url = image_url;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLink() {
        return link;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.link+";"+this.image_url+"@result@";
        else
            return this.link+";"+this.image_url;
    }
}
