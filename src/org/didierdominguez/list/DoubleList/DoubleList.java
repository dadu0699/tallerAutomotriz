package org.didierdominguez.list.DoubleList;

public class DoubleList {
    private DoubleNode firstNode;

    public DoubleList() {
        firstNode = null;
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void addFirstNode(Object object) {
        DoubleNode node = new DoubleNode(object);
        if (isEmpty()) {
            firstNode = node;
        } else {
            node.setNextNode(firstNode);
            firstNode.setPreviousNode(node);
            firstNode = node;
        }
    }

    public void addLastNode(Object object) {
        DoubleNode node = new DoubleNode(object);
        if (isEmpty()) {
            firstNode = node;
        } else {
            DoubleNode lastNode = getLastNode();
            node.setPreviousNode(lastNode);
            lastNode.setNextNode(node);
        }
    }

    public void readStartNodes() {
        DoubleNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getObject() + " <--> ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public void readEndNodes() {
        DoubleNode auxiliaryNode = getLastNode();
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getObject() + " <--> ");
            auxiliaryNode = auxiliaryNode.getPreviousNode();
        }
        System.out.println();
    }

    public void updateNode(Object oldObject, Object newObject) {
        DoubleNode node = searchNode(oldObject);
        if (node != null) {
            node.setObject(newObject);
        }
    }

    public void deleteFirstNode() {
        if (!isEmpty()) {
            firstNode = firstNode.getNextNode();
            firstNode.setPreviousNode(null);
        }
    }

    public void deleteLastNode() {
        if (!isEmpty()) {
            DoubleNode lastNode = getLastNode();
            DoubleNode auxiliaryNode = firstNode;
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
                firstNode.setPreviousNode(null);
            } else {
                DoubleNode auxiliaryNode = searchNode(object);
                if (auxiliaryNode != null) {
                    DoubleNode previousNode = auxiliaryNode.getPreviousNode();
                    DoubleNode nextNode = auxiliaryNode.getNextNode();
                    previousNode.setNextNode(auxiliaryNode.getNextNode());
                    if (nextNode != null) {
                        nextNode.setPreviousNode(auxiliaryNode.getPreviousNode());
                    }
                }
            }
        }
    }

    private DoubleNode getLastNode() {
        DoubleNode lastNode = firstNode;
        if (firstNode != null) {
            while (lastNode.getNextNode() != null) {
                lastNode = lastNode.getNextNode();
            }
            return lastNode;
        }
        return null;
    }

    private DoubleNode searchNode(Object object) {
        DoubleNode auxiliaryNode = firstNode;
        while (auxiliaryNode != null && auxiliaryNode.getObject() != object) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }

    public DoubleNode getFirstNode() {
        return firstNode;
    }
}
