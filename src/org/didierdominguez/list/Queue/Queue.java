package org.didierdominguez.list.Queue;

public class Queue {
    private QueueNode firstNode;
    private int length;

    public Queue() {
        firstNode = null;
        length = 0;
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void push(Object object) {
        QueueNode node = new QueueNode(object);
        if (isEmpty()) {
            firstNode = node;
        } else {
            getLastNode().setNextNode(node);
        }
        length++;
    }

    public void readNodes() {
        QueueNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getObject() + " <-- ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public void pop() {
        if (!isEmpty()) {
            firstNode = firstNode.getNextNode();
            length--;
        }
    }

    public Object getfirstNode() {
        if (!isEmpty()) {
            return firstNode.getObject();
        }
        return null;
    }

    public int getLength() {
        return length;
    }

    private QueueNode getLastNode() {
        QueueNode lastNode = firstNode;
        while (lastNode.getNextNode() != null) {
            lastNode = lastNode.getNextNode();
        }
        return lastNode;
    }

    private QueueNode searchNode(Object object) {
        QueueNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null && auxiliaryNode.getObject() != object) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }
}
