package org.didierdominguez.controller;

import org.didierdominguez.bean.User;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;

public class ControllerUser {
    private static ControllerUser instance;
    private SimpleList userList;
    private int id;
    private boolean update;

    private ControllerUser() {
        userList = new SimpleList();
        id = 0;
    }

    public static ControllerUser getInstance() {
        if (instance == null) {
            instance = new ControllerUser();
        }
        return instance;
    }

    public User createUser(String userName, String password, Boolean role) {
        if (searchUser(userName) == null) {
            id++;
            User user = new User(id, userName, password, role);
            userList.addLastNode(user);
            System.out.println("User added successfully");
            return searchUser(userName);
        } else {
            System.out.println("The username is already used");
        }
        return null;
    }

    public SimpleList getUserList() {
        return userList;
    }

    public void updateUser(Integer id, String userName, String password, Boolean role) {
        User user = searchUser(id);
        update = false;
        if (user != null) {
            if (searchUser(userName) == null || user.getUserName().equalsIgnoreCase(userName)) {
                user.setUserName(userName);
                user.setPassword(password);
                user.setRole(role);
                update = true;
                System.out.println("User updated successfully");
            } else {
                System.out.println("The username is already used");
            }
        } else {
            System.out.println("User was not found");
        }
    }

    public boolean updateUser() {
        return update;
    }

    public void deleteUser(Integer id) {
        User user = searchUser(id);
        if (user != null) {
            userList.deleteSpecificNode(user);
            System.out.println("User deleted successfully");
        } else {
            System.out.println("User was not found");
        }
    }

    private User searchUser(Integer id) {
        SimpleNode auxiliaryNode = getUserList().getFirstNode();
        while (auxiliaryNode != null && !((User) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (User) auxiliaryNode.getObject();
        }
        return null;
    }

    public User searchUser(String userName) {
        SimpleNode auxiliaryNode = getUserList().getFirstNode();
        while (auxiliaryNode != null && !((User) auxiliaryNode.getObject()).getUserName().equalsIgnoreCase(userName)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (User) auxiliaryNode.getObject();
        }
        return null;
    }

    public User searchUser(String userName, String password) {
        SimpleNode auxiliaryNode = getUserList().getFirstNode();
        while (auxiliaryNode != null
                && (!((User) auxiliaryNode.getObject()).getUserName().equalsIgnoreCase(userName)
                || !((User) auxiliaryNode.getObject()).getPassword().equals(password))) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (User) auxiliaryNode.getObject();
        }
        return null;
    }
}
