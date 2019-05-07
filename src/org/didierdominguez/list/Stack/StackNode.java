package org.didierdominguez.list.Stack;

public class StackNode {
    private Object object;
    private StackNode nextNode;

    public StackNode(Object object) {
        this.object = object;
        nextNode = null;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public StackNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(StackNode nextNode) {
        this.nextNode = nextNode;
    }
}
