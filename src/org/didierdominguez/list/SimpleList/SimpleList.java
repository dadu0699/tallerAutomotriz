package org.didierdominguez.list.SimpleList;

public class SimpleList {
    private SimpleNode firstNode;

    public SimpleList() {
        firstNode = null;
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void addFirstNode(Object object) {
        SimpleNode node = new SimpleNode(object);
        if (isEmpty()) {
            firstNode = node;
        } else {
            node.setNextNode(firstNode);
            firstNode = node;
        }
    }

    public void addLastNode(Object object) {
        SimpleNode node = new SimpleNode(object);
        if (isEmpty()) {
            firstNode = node;
        } else {
            getLastNode().setNextNode(node);
        }
    }

    public void readNodes() {
        SimpleNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getObject() + " --> ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public void updateNode(Object oldObject, Object newObject) {
        SimpleNode node = searchNode(oldObject);
        if (node != null) {
            node.setObject(newObject);
        }
    }

    public void deleteFirstNode() {
        if (!isEmpty()) {
            firstNode = firstNode.getNextNode();
        }
    }

    public void deleteLastNode() {
        if (!isEmpty()) {
            SimpleNode lastNode = getLastNode();
            SimpleNode auxiliaryNode = firstNode;
            while (auxiliaryNode.getNextNode() != lastNode) {
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
            lastNode = auxiliaryNode;
            lastNode.setNextNode(null);
        }
    }

    public void deleteSpecificNode(Object object) {
        if (!isEmpty()) {
            if (firstNode == getLastNode() && object == firstNode.getObject()) {
                firstNode = null;
            } else if (object == firstNode.getObject()) {
                firstNode = firstNode.getNextNode();
            } else {
                SimpleNode previousNode = firstNode;
                SimpleNode auxiliaryNode = firstNode.getNextNode();
                while (auxiliaryNode != null && auxiliaryNode.getObject() != object) {
                    previousNode = previousNode.getNextNode();
                    auxiliaryNode = auxiliaryNode.getNextNode();
                }
                if (auxiliaryNode != null) {
                    previousNode.setNextNode(auxiliaryNode.getNextNode());
                }
            }
        }
    }

    private SimpleNode getLastNode() {
        SimpleNode lastNode = firstNode;
        while (lastNode.getNextNode() != null) {
            lastNode = lastNode.getNextNode();
        }
        return lastNode;
    }

    private SimpleNode searchNode(Object object) {
        SimpleNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null && auxiliaryNode.getObject() != object) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }

    public SimpleNode getFirstNode() {
        return firstNode;
    }
}
