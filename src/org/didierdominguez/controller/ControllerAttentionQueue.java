package org.didierdominguez.controller;

import org.didierdominguez.bean.Order;
import org.didierdominguez.list.Queue.Queue;

public class ControllerAttentionQueue {
    private static ControllerAttentionQueue instance;
    private Queue attentionQueue;

    private ControllerAttentionQueue() {
        attentionQueue = new Queue();
    }

    public static ControllerAttentionQueue getInstance() {
        if (instance == null) {
            instance = new ControllerAttentionQueue();
        }
        return instance;
    }

    public void addQueue(Order order) {
        ControllerOrder.getInstance().updateOrder(order.getId(), "EN SERVICIO");
        attentionQueue.push(order);
    }

    public void removeQueue() {
        if (attentionQueue.getfirstNode() != null) {
            ControllerServiceFinished.getInstance().addList((Order) attentionQueue.getfirstNode().getObject());
            ControllerInvoice.getInstance().createInvoice((Order) attentionQueue.getfirstNode().getObject());
            attentionQueue.pop();
            ControllerPriorityQueue.getInstance().removeQueue();
        }
    }

    public Queue getAttentionQueue() {
        return attentionQueue;
    }
}
