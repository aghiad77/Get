package com.example.aghiad_pc.get.Model;

public class Order {

    private String title, description, cost, status, duration;

    public Order(String title, String description, String cost, String status, String duration) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.status = status;
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public String getDuration() {
        return duration;
    }
}
