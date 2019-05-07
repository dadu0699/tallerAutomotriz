package org.didierdominguez.bean;

import org.didierdominguez.list.CircularSimpleList.CircularSimpleList;

public class Customer extends Person {
    private Boolean type;
    private CircularSimpleList cars;

    public Customer(Integer id, String name, User user, Boolean type, CircularSimpleList cars) {
        super(id, name, user);
        this.type = type;
        this.cars = cars;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public CircularSimpleList getCars() {
        return cars;
    }

    public void setCars(CircularSimpleList cars) {
        this.cars = cars;
    }
}
