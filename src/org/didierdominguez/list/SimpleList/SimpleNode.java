package org.didierdominguez.list.SimpleList;

public class SimpleNode {
    private Object object;
    private SimpleNode nextNode;

    public SimpleNode(Object object) {
        this.object = object;
        nextNode = null;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public SimpleNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(SimpleNode nextNode) {
        this.nextNode = nextNode;
    }
}
