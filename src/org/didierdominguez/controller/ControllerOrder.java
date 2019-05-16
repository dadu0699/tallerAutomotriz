package org.didierdominguez.controller;

import org.didierdominguez.bean.*;
import org.didierdominguez.list.DoubleList.DoubleNode;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.util.Verifications;

public class ControllerOrder {
    private static ControllerOrder instance;
    private SimpleList orderList;
    private int id;
    private boolean update;

    private ControllerOrder() {
        orderList = new SimpleList();
        id = 0;
    }

    public static ControllerOrder getInstance() {
        if (instance == null) {
            instance = new ControllerOrder();
        }
        return instance;
    }

    public void createOrder(Car car, Service service) {
        DoubleNode auxiliaryNode = ControllerEmployee.getInstance().getEmployeeList().getFirstNode();
        Employee employee = null;
        String state = "COLA DE ESPERA PARA SER ATENDIDO";
        while (auxiliaryNode != null) {
            if (((Employee) auxiliaryNode.getObject()).getRole().equalsIgnoreCase("MECÁNICO")
                    && !searchOrder((Employee) auxiliaryNode.getObject())) {
                employee = (Employee) auxiliaryNode.getObject();
                state = "EN SERVICIO";
                break;
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }

        id++;
        Order order = new Order(id, car, car.getCustomer(), service, Verifications.getInstance().getDate(), employee, state);
        orderList.addLastNode(order);
        if (state.equalsIgnoreCase("COLA DE ESPERA PARA SER ATENDIDO")) {
            ControllerPriorityQueue.getInstance().addQueue(order);
        }
        if (state.equalsIgnoreCase("EN SERVICIO")) {
            ControllerAttentionQueue.getInstance().addQueue(order);
        }
        System.out.println("Order added successfully");
        //System.out.println(order.getId()+"-"+order.getCar().getBrand()+"-"+order.getCustomer().getName()+"-"+order.getDate()+"-"+order.getEmployee()+"-"+order.getState());
    }

    public void updateOrder(Integer id, String state) {
        Order order = searchOrder(id);
        update = false;
        if (order != null) {
            order.setState(state);
            System.out.println("Order updated successfully");
        } else {
            System.out.println("Order was not found");
        }
    }

    public boolean updateOrder() {
        return update;
    }

    public void deleteOrder(Integer id) {
        Order order = searchOrder(id);
        if (order != null) {
            orderList.deleteSpecificNode(order);
            System.out.println("Order deleted successfully");
        } else {
            System.out.println("Order was not found");
        }
    }

    public SimpleList getOrderList() {
        return orderList;
    }

    private Order searchOrder(Integer id) {
        SimpleNode auxiliaryNode = getOrderList().getFirstNode();
        while (auxiliaryNode != null && !((Order) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Order) auxiliaryNode.getObject();
        }
        return null;
    }

    public Order searchOrder(Car car) {
        SimpleNode auxiliaryNode = getOrderList().getFirstNode();
        while (auxiliaryNode != null && !((Order) auxiliaryNode.getObject()).getCar().equals(car)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Order) auxiliaryNode.getObject();
        }
        return null;
    }

    public Boolean searchOrder(Employee employee) {
        SimpleNode auxiliaryNode = getOrderList().getFirstNode();

        while (auxiliaryNode != null) {
            if (((Order) auxiliaryNode.getObject()).getEmployee() != null) {
                if (((Order) auxiliaryNode.getObject()).getEmployee().getId().equals(employee.getId())
                        && !((Order) auxiliaryNode.getObject()).getState().equalsIgnoreCase("LISTO")) {
                    return true;
                }
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return false;
    }
}
