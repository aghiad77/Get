package com.example.aghiad_pc.get.Model;

public class Product {

    private String id,title, name, salary,count,size,color;
    private String  image;
    public Product() {
    }
    public Product(String id,String title, String name, String salary, String image,String count,String size,String color) {
        this.id=id;
        this.title = title;
        this.name = name;
        this.salary = salary;
        this.image = image;
        this.count=count;
        this.size=size;
        this.color=color;
    }

    public String getId() {
        return id;
    }
    public String getCount() {
        return count;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSalary() {
        return salary;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }
}
