package com.example.aghiad_pc.get.Model;

public class Shop {


    int Image;
    String shopText;

    public Shop(int image,String shopText) {

        this.Image = image;
        this.shopText = shopText;
    }

    public int getImage() {
        return Image;
    }
    public void setImage(int image) {
        this.Image = image;
    }

    public String getShopText() {
        return shopText;
    }
    public void setShopText(String shopText) {
        this.shopText = shopText;
    }

}
