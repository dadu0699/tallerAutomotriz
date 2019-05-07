package org.didierdominguez.list.DoubleList;

public class DoubleNode {
    private Object object;
    private DoubleNode nextNode;
    private DoubleNode previousNode;

    public DoubleNode(Object object) {
        this.object = object;
        nextNode = null;
        previousNode = null;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public DoubleNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(DoubleNode nextNode) {
        this.nextNode = nextNode;
    }

    public DoubleNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(DoubleNode previousNode) {
        this.previousNode = previousNode;
    }
}
