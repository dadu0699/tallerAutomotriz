package org.didierdominguez.bean;

public class Invoice {
    private Integer id;
    private Order order;
    private Boolean state;

    public Invoice(Integer id, Order order, Boolean state) {
        this.id = id;
        this.order = order;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
