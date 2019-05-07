package org.didierdominguez.bean;

import java.util.Date;

public class Order {
    private Integer id;
    private Car car;
    private Customer customer;
    private Service service;
    private Date date;
    private Employee employee;
    private String state;

    public Order(Integer id, Car car, Customer customer, Service service, Date date, Employee employee, String state) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.service = service;
        this.date = date;
        this.employee = employee;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
