package org.didierdominguez.controller;

import org.didierdominguez.bean.Order;
import org.didierdominguez.bean.SparePart;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.list.Stack.StackNode;

public class ControllerServiceFinished {
    private static ControllerServiceFinished instance;
    private SimpleList serviceFinishedList;

    private ControllerServiceFinished() {
        serviceFinishedList = new SimpleList();
    }

    public static ControllerServiceFinished getInstance() {
        if (instance == null) {
            instance = new ControllerServiceFinished();
        }
        return instance;
    }

    public void addList(Order order) {
        ControllerOrder.getInstance().updateOrder(order.getId(), "LISTO");
        updateCustomer(order);
        order.getService().setCount(order.getService().getCount() + 1);
        StackNode auxiliaryNode = (StackNode) order.getService().getSpares().getTop();
        while (auxiliaryNode != null) {
            ((SparePart) auxiliaryNode.getObject()).setCount(((SparePart) auxiliaryNode.getObject()).getCount() + 1);
            ((SparePart) auxiliaryNode.getObject()).setStock(((SparePart) auxiliaryNode.getObject()).getStock() - 1);
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        serviceFinishedList.addLastNode(order);
    }

    public SimpleList getServiceFinishedList() {
        return serviceFinishedList;
    }

    public void deleteFinishedList(Order order) {
        serviceFinishedList.deleteSpecificNode(order);
    }

    public void updateCustomer(Order order) {
        int count = 1;
        SimpleNode auxiliaryNode = serviceFinishedList.getFirstNode();
        while (auxiliaryNode != null) {
            if (((Order) auxiliaryNode.getObject()).getCustomer() == order.getCustomer()) {
                count++;
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (count >= 3) {
            order.getCustomer().setType(true);
        }
    }
}
