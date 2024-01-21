package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Orders {

    @SerializedName("id")
    private  String id;

    @SerializedName("ordered_at")
    private  String ordered_at;

    @SerializedName("shipped_at")
    private  String shipped_at;

    @SerializedName("transport_cost")
    private  String transport_cost;

    @SerializedName("status")
    private  String status;

    @SerializedName("shipmethod")
    private  String shipmethod;

    @SerializedName("sum_total_price")
    private  String sum_total_price;

    @SerializedName("item_count")
    private  String item_count;

    @SerializedName("transport_message")
    private  String transport_message;


    public String getId() {
        return id;
    }

    public String getOrdered_at() {
        return ordered_at;
    }

    public String getShipped_at() {
        return shipped_at;
    }

    public String getTransport_cost() {
        return transport_cost;
    }

    public String getStatus() {
        return status;
    }

    public String getSum_total_price() {
        return sum_total_price;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrdered_at(String ordered_at) {
        this.ordered_at = ordered_at;
    }

    public void setShipped_at(String shipped_at) {
        this.shipped_at = shipped_at;
    }

    public void setTransport_cost(String transport_cost) {
        this.transport_cost = transport_cost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSum_total_price(String sum_total_price) {
        this.sum_total_price = sum_total_price;
    }

    public void setShipmethod(String shipmethod) {
        this.shipmethod = shipmethod;
    }

    public String getShipmethod() {
        return shipmethod;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public void setTransport_message(String transport_message) {
        this.transport_message = transport_message;
    }

    public String getTransport_message() {
        return transport_message;
    }

    public Orders(String id, String ordered_at, String shipped_at, String transport_cost, String transport_message, String status, String shipmethod, String sum_total_price, String item_count) {
        this.id = id;
        this.ordered_at = ordered_at;
        this.shipped_at = shipped_at;
        this.transport_cost = transport_cost;
        this.status = status;
        this.sum_total_price = sum_total_price;
        this.item_count = item_count;
        this.shipmethod = shipmethod;
        this.transport_message=transport_message;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.id+";"+this.ordered_at+";"+this.shipped_at+";"+this.transport_cost+";"+this.transport_message+";"+this.status+";"+this.shipmethod+";"+this.sum_total_price+";"+this.item_count+"@result@";
        else
            return this.id+";"+this.ordered_at+";"+this.shipped_at+";"+this.transport_cost+";"+this.transport_message+";"+this.status+";"+this.shipmethod+";"+this.sum_total_price+";"+this.item_count;
    }

}
