package org.didierdominguez.bean;

public class Customer extends Person {
    private Boolean type;

    public Customer(Integer id, String name, User user, Boolean type) {
        super(id, name, user);
        this.type = type;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }
}
