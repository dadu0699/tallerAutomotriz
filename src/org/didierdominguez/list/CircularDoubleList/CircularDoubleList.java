package org.didierdominguez.list.CircularDoubleList;

public class CircularDoubleList {
    private CircularDoubleNode node;

    public CircularDoubleList() {
        node = null;
    }

    private boolean isEmpty() {
        return node == null;
    }

    public void addNode(Object object) {
        CircularDoubleNode newNode = new CircularDoubleNode(object);
        CircularDoubleNode lastNode;
        if (node != null) {
            newNode.setNextNode(node.getNextNode());
            newNode.setPreviousNode(node);
            node.setNextNode(newNode);
        }
        node = newNode;

        lastNode = getLastNode();
        if (lastNode != null) {
            lastNode.getNextNode().setPreviousNode(newNode);
        }
    }

    public void readStartNodes() {
        if (!isEmpty()) {
            CircularDoubleNode auxiliaryNode = node.getNextNode();
            do {
                // System.out.print(auxiliaryNode.getPreviousNode().getObject() + " <-- ");
                System.out.print(auxiliaryNode.getObject() + " <--> ");
                // System.out.println(auxiliaryNode.getNextNode().getObject());
                auxiliaryNode = auxiliaryNode.getNextNode();
            } while (auxiliaryNode != node.getNextNode());
            System.out.println();
        }
    }

    public void readEndNodes() {
        CircularDoubleNode auxiliaryNode = getLastNode();
        if (auxiliaryNode != null) {
            do {
                // System.out.print(auxiliaryNode.getPreviousNode().getObject() + " <-- ");
                System.out.print(auxiliaryNode.getObject() + " <--> ");
                // System.out.println(auxiliaryNode.getNextNode().getObject());
                auxiliaryNode = auxiliaryNode.getPreviousNode();
            } while (auxiliaryNode != node);
            System.out.println();
        }
    }

    public void updateNode(Object oldObject, Object newObject) {
        CircularDoubleNode node = searchNode(oldObject);
        if (node != null) {
            node.setObject(newObject);
        }
    }

    public void deleteSpecificNode(Object object) {
        if (!isEmpty()) {
            CircularDoubleNode auxiliaryNode = searchNode(object);
            if (auxiliaryNode != null) {
                CircularDoubleNode previousNode = auxiliaryNode.getPreviousNode();
                CircularDoubleNode nextNode = auxiliaryNode.getNextNode();

                previousNode.setNextNode(auxiliaryNode.getNextNode());
                nextNode.setPreviousNode(auxiliaryNode.getPreviousNode());

                if (node == auxiliaryNode) {
                    if (node == previousNode && node == nextNode) {
                        node = null;
                    } else {
                        node = node.getPreviousNode();
                        node.setNextNode(nextNode);
                    }
                }
            }
        }
    }

    private CircularDoubleNode getLastNode() {
        CircularDoubleNode lastNode = node;
        if (node != null) {
            do {
                lastNode = lastNode.getNextNode();
            } while (lastNode != node);
        }
        return lastNode;
    }

    private CircularDoubleNode searchNode(Object object) {
        CircularDoubleNode auxiliaryNode = node;
        do {
            if (auxiliaryNode.getObject() == object) {
                return auxiliaryNode;
            } else {
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
        } while (auxiliaryNode != node);
        return null;
    }

    public CircularDoubleNode getNode() {
        return node;
    }
}
