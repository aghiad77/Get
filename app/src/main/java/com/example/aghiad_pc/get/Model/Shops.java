package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Shops {

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("address")
    private String address;

    @SerializedName("comment")
    private String comment;

    public Shops(String name, String logo, String address, String comment) {
        this.name = name;
        this.logo = logo;
        this.address = address;
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getAddress() {
        return address;
    }

    public String getComment() {
        return comment;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.name+";"+this.logo+";"+this.address+";" +this.comment+"@result@";
        else
            return this.name+";"+this.logo+";"+this.address+";" +this.comment;
    }

}
