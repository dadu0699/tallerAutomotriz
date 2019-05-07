package org.didierdominguez.list.CircularDoubleList;

public class CircularDoubleNode {
    private Object object;
    private CircularDoubleNode nextNode;
    private CircularDoubleNode previousNode;

    public CircularDoubleNode(Object object) {
        this.object = object;
        nextNode = this;
        previousNode = this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public CircularDoubleNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CircularDoubleNode nextNode) {
        this.nextNode = nextNode;
    }

    public CircularDoubleNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(CircularDoubleNode previousNode) {
        this.previousNode = previousNode;
    }
}
