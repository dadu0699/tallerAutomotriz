package org.didierdominguez.list.Queue;

public class QueueNode {
    private Object object;
    private QueueNode nextNode;

    public QueueNode(Object object) {
        this.object = object;
        nextNode = null;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public QueueNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(QueueNode nextNode) {
        this.nextNode = nextNode;
    }
}
