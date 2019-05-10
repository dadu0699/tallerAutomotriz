package org.didierdominguez.controller;

import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.User;
import org.didierdominguez.list.CircularDoubleList.CircularDoubleList;
import org.didierdominguez.list.CircularDoubleList.CircularDoubleNode;

public class ControllerCustomer {
    private static ControllerCustomer instance;
    private CircularDoubleList customerList;
    private boolean update;

    private ControllerCustomer() {
        customerList = new CircularDoubleList();
    }

    public static ControllerCustomer getInstance() {
        if (instance == null) {
            instance = new ControllerCustomer();
        }
        return instance;
    }

    public void createCustomer(Integer id, String name, Boolean role, String userName, String password) {
        if (searchCustomer(id) == null && searchCustomer(name) == null) {
            User user = ControllerUser.getInstance().createUser(userName.toUpperCase().trim(), password, false);
            if (user != null) {
                Customer customer = new Customer(id, name.toUpperCase().trim(), user, role);
                customerList.addNode(customer);
                System.out.println("Customer added successfully");
            }
        } else {
            System.out.println("The customer is already registered");
        }
    }

    public void updateCustomer(Integer id, String name, String userName, String password) {
        Customer customer = searchCustomer(id);
        update = false;
        if (customer != null) {
            if ((searchCustomer(id) == null && searchCustomer(name) == null)
                    || (customer.getName().equalsIgnoreCase(name) && customer.getId().equals(id))) {
                User user = customer.getUser();
                ControllerUser.getInstance().updateUser(user.getId(), userName, password, false);
                if (ControllerUser.getInstance().updateUser()) {
                    customer.setName(name);
                    update = true;
                    System.out.println("Customer updated successfully");
                }
            } else {
                System.out.println("The customer is already registered");
            }
        } else {
            System.out.println("Customer was not found");
        }
    }

    public boolean updateCustomer() {
        return update;
    }

    public void deleteCustomer(Integer id) {
        Customer customer = searchCustomer(id);
        if (customer != null) {
            customerList.deleteSpecificNode(customer);
            ControllerUser.getInstance().deleteUser(customer.getUser().getId());
            System.out.println("Customer deleted successfully");
        } else {
            System.out.println("Customer was not found");
        }
    }

    public CircularDoubleList getCustomerList() {
        return customerList;
    }

    public Customer searchCustomer(Integer id) {
        CircularDoubleNode auxiliaryNode = getCustomerList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Customer) auxiliaryNode.getObject()).getId().equals(id)) {
                    return (Customer) auxiliaryNode.getObject();
                } else {
                    auxiliaryNode = auxiliaryNode.getNextNode();
                }
            } while (auxiliaryNode != getCustomerList().getNode());
        }
        return null;
    }

    public Customer searchCustomer(String name) {
        CircularDoubleNode auxiliaryNode = getCustomerList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Customer) auxiliaryNode.getObject()).getName().equalsIgnoreCase(name)) {
                    return (Customer) auxiliaryNode.getObject();
                } else {
                    auxiliaryNode = auxiliaryNode.getNextNode();
                }
            } while (auxiliaryNode != getCustomerList().getNode());
        }
        return null;
    }
}
