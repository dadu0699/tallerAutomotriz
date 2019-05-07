package org.didierdominguez.list.CircularSimpleList;

public class CircularSimpleNode {
    private Object object;
    private CircularSimpleNode nextNode;

    public CircularSimpleNode(Object object) {
        this.object = object;
        nextNode = this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public CircularSimpleNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CircularSimpleNode nextNode) {
        this.nextNode = nextNode;
    }
}
