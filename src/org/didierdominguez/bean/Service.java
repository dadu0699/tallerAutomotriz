package org.didierdominguez.bean;

import org.didierdominguez.list.Stack.Stack;

public class Service {
    private Integer id;
    private String name;
    private String brand;
    private String model;
    private Stack spares;
    private Double laborPrice;
    private Double total;

    public Service(Integer id, String name, String brand, String model, Stack spares, Double laborPrice, Double total) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.spares = spares;
        this.laborPrice = laborPrice;
        this.total = total;
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

    public Stack getSpares() {
        return spares;
    }

    public void setSpares(Stack spares) {
        this.spares = spares;
    }

    public Double getLaborPrice() {
        return laborPrice;
    }

    public void setLaborPrice(Double laborPrice) {
        this.laborPrice = laborPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
