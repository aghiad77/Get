package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class productDetails {

    @SerializedName("product")
    private Products product;

    @SerializedName("images")
    private List<image> images;

    @SerializedName("warehouse")
    private List<Warehouse> warehouse;

    @SerializedName("offer")
    private OfferD offer;

    @SerializedName("sold")
    private Sold sold;

    public productDetails(Products product, List<image> images, List<Warehouse> warehouse,  OfferD offer, Sold sold) {
        this.product = product;
        this.warehouse = warehouse;
        this.images = images;
        this.offer = offer;
        this.sold = sold;
    }

    public productDetails(){}

    public void setProduct(Products product) {
        this.product = product;
    }

    public void setOffer(OfferD offer) {
        this.offer = offer;
    }

    public void setSold(Sold sold) {
        this.sold = sold;
    }

    public Products getProduct() {
        return product;
    }

    public List<Warehouse> getWarehouse() {
        return warehouse;
    }

    public OfferD getOffer() {
        return offer;
    }

    public Sold getSold() {
        return sold;
    }

    public void setWarehouse(List<Warehouse> warehouse) {
        this.warehouse = warehouse;
    }

    public void setImages(List<image> images) {
        this.images = images;
    }

    public List<image> getImages() {
        return images;
    }
}
