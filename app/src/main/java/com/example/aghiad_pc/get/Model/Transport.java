package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Transport {

    @SerializedName("transport_cost")
    private String transport_cost;

    @SerializedName("transport_message")
    private String transport_message;

    public Transport(String transport_cost, String transport_message) {
        this.transport_cost = transport_cost;
        this.transport_message = transport_message;
    }

    public void setTransport_cost(String transport_cost) {
        this.transport_cost = transport_cost;
    }

    public void setTransport_message(String transport_message) {
        this.transport_message = transport_message;
    }

    public String getTransport_cost() {
        return transport_cost;
    }

    public String getTransport_message() {
        return transport_message;
    }
}
