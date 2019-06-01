package org.didierdominguez.bean;

public class SparePart {
    private Integer id;
    private String name;
    private String brand;
    private String model;
    private Integer stock;
    private Double price;
    private Integer count;

    public SparePart(Integer id, String name, String brand, String model, Integer stock, Double price, Integer count) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.stock = stock;
        this.price = price;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
