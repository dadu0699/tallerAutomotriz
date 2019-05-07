package org.didierdominguez.bean;

public class Employee extends Person {
    private String role;

    public Employee(Integer id, String name, User user, String role) {
        super(id, name, user);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
