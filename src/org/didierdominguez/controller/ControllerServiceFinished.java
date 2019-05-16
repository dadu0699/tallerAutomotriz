package org.didierdominguez.controller;

import org.didierdominguez.bean.Order;
import org.didierdominguez.list.Queue.Queue;
import org.didierdominguez.list.Queue.QueueNode;

public class ControllerServiceFinished {
    private static ControllerServiceFinished instance;
    private Queue serviceFinishedQueue;

    private ControllerServiceFinished() {
        serviceFinishedQueue = new Queue();
    }

    public static ControllerServiceFinished getInstance() {
        if (instance == null) {
            instance = new ControllerServiceFinished();
        }
        return instance;
    }

    public void addQueue(Order order) {
        ControllerOrder.getInstance().updateOrder(order.getId(), "LISTO");
        updateCustomer(order);
        serviceFinishedQueue.push(order);
    }

    public Queue getServiceFinishedQueue() {
        return serviceFinishedQueue;
    }

    public void updateCustomer(Order order) {
        int count = 1;
        QueueNode auxiliaryNode = serviceFinishedQueue.getfirstNode();
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
