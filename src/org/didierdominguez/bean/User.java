package org.didierdominguez.bean;

public class User {
    private Integer id;
    private String userName;
    private String password;
    private Boolean role;

    public User(Integer id, String userName, String password, Boolean role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return this.userName;
    }
}
