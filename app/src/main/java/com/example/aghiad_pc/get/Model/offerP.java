package com.example.aghiad_pc.get.Model;

public class offerP {

    String name;
    String oldPrice,newPrice;
    int image;

    public offerP(String name, int image,String oldPrice, String newPrice) {
        this.name = name;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public int getImage() {
        return image;
    }
}
