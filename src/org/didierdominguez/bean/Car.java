package org.didierdominguez.bean;

public class Car {
    private String id;
    private String brand;
    private String model;
    private String picture;

    public Car(String id, String brand, String model, String picture) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
