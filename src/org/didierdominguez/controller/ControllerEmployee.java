package org.didierdominguez.controller;

import org.didierdominguez.bean.Employee;
import org.didierdominguez.bean.User;
import org.didierdominguez.list.DoubleList.DoubleList;
import org.didierdominguez.list.DoubleList.DoubleNode;

public class ControllerEmployee {
    private static ControllerEmployee instance;
    private DoubleList employeeList;
    private int id;
    private boolean update;

    private ControllerEmployee() {
        employeeList = new DoubleList();
        id = 0;
    }

    public static ControllerEmployee getInstance() {
        if (instance == null) {
            instance = new ControllerEmployee();
        }
        return instance;
    }

    public void createEmployee(String name, String role, String userName, String password, Boolean userRole) {
        if (searchEmployee(name) == null) {
            User user = ControllerUser.getInstance().createUser(userName.toUpperCase().trim(), password, userRole);
            if (user != null) {
                id++;
                Employee employee = new Employee(id, name.toUpperCase().trim(), user, role.toUpperCase().trim());
                employeeList.addLastNode(employee);
                System.out.println("Employee added successfully");
            }
        } else {
            System.out.println("The employee is already registered");
        }
    }

    public void updateEmployee(Integer id, String name, String role, String userName, String password,
            Boolean userRole) {
        Employee employee = searchEmployee(id);
        update = false;
        if (employee != null) {
            if (searchEmployee(name) == null || employee.getName().equalsIgnoreCase(name)) {
                User user = employee.getUser();
                ControllerUser.getInstance().updateUser(user.getId(), userName, password, userRole);
                if (ControllerUser.getInstance().updateUser()) {
                    employee.setName(name);
                    employee.setRole(role);
                    update = true;
                    System.out.println("Employee updated successfully");
                }
            } else {
                System.out.println("The employee is already registered");
            }
        } else {
            System.out.println("Employee was not found");
        }
    }

    public boolean updateEmployee() {
        return update;
    }

    public void deleteEmployee(Integer id) {
        Employee employee = searchEmployee(id);
        if (employee != null) {
            employeeList.deleteSpecificNode(employee);
            ControllerUser.getInstance().deleteUser(employee.getUser().getId());
            System.out.println("Employee deleted successfully");
        } else {
            System.out.println("Employee was not found");
        }
    }

    public DoubleList getEmployeeList() {
        return employeeList;
    }

    private Employee searchEmployee(Integer id) {
        DoubleNode auxiliaryNode = getEmployeeList().getFirstNode();
        while (auxiliaryNode != null && !((Employee) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Employee) auxiliaryNode.getObject();
        }
        return null;
    }

    public Employee searchEmployee(String name) {
        DoubleNode auxiliaryNode = getEmployeeList().getFirstNode();
        while (auxiliaryNode != null
                && !((Employee) auxiliaryNode.getObject()).getName().equalsIgnoreCase(name)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Employee) auxiliaryNode.getObject();
        }
        return null;
    }
}
