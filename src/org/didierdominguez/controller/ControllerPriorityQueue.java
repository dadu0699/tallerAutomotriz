package org.didierdominguez.controller;

import org.didierdominguez.bean.Employee;
import org.didierdominguez.bean.Order;
import org.didierdominguez.list.DoubleList.DoubleNode;
import org.didierdominguez.list.Queue.Queue;
import org.didierdominguez.list.Queue.QueueNode;

public class ControllerPriorityQueue {
    private static ControllerPriorityQueue instance;
    private Queue priorityQueue;

    private ControllerPriorityQueue() {
        priorityQueue = new Queue();
    }

    public static ControllerPriorityQueue getInstance() {
        if (instance == null) {
            instance = new ControllerPriorityQueue();
        }
        return instance;
    }

    public void addQueue(Order order) {
        priorityQueue.push(order);
        sortQueue();
    }

    public void removeQueue() {
        if (priorityQueue.getfirstNode() != null) {
            DoubleNode auxiliaryNode = ControllerEmployee.getInstance().getEmployeeList().getFirstNode();
            Employee employee = null;
            while (auxiliaryNode != null) {
                if (((Employee) auxiliaryNode.getObject()).getRole().equalsIgnoreCase("MECÁNICO")
                        && !ControllerOrder.getInstance().searchOrder((Employee) auxiliaryNode.getObject())) {
                    employee = (Employee) auxiliaryNode.getObject();
                    ((Order) priorityQueue.getfirstNode().getObject()).setEmployee(employee);
                    ControllerAttentionQueue.getInstance().addQueue((Order) priorityQueue.getfirstNode().getObject());
                    priorityQueue.pop();
                    break;
                }
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
        }
    }

    private void sortQueue() {
        QueueNode current = priorityQueue.getfirstNode();
        QueueNode index;
        Object temp;

        if(priorityQueue.getfirstNode() != null) {
            while(current != null) {
                index = current.getNextNode();
                while(index != null) {
                    if(!((Order) current.getObject()).getCustomer().getType()
                            && ((Order) index.getObject()).getCustomer().getType()) {
                        temp = current.getObject();
                        current.setObject(index.getObject());
                        index.setObject(temp);
                    }
                    index = index.getNextNode();
                }
                current = current.getNextNode();
            }
        }
        sortQueueID();
    }

    private void sortQueueID() {
        QueueNode current = priorityQueue.getfirstNode();
        QueueNode index;
        Object temp;

        if(priorityQueue.getfirstNode() != null) {
            while(current != null) {
                index = current.getNextNode();
                while(index != null) {
                    if(!((Order) current.getObject()).getCustomer().getType()
                            && !((Order) index.getObject()).getCustomer().getType()
                            && (((Order) current.getObject()).getId() > ((Order) index.getObject()).getId())) {
                        temp = current.getObject();
                        current.setObject(index.getObject());
                        index.setObject(temp);
                    }
                    index = index.getNextNode();
                }
                current = current.getNextNode();
            }
        }
    }

    public void readNodes() {
        QueueNode auxiliaryNode = priorityQueue.getfirstNode();
        while (auxiliaryNode != null) {
            System.out.println(((Order) auxiliaryNode.getObject()).getId() + "-"
                    + ((Order) auxiliaryNode.getObject()).getCustomer().getName() + "-"
                    + ((Order) auxiliaryNode.getObject()).getCustomer().getType());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public Queue getPriorityQueue() {
        return priorityQueue;
    }
}
