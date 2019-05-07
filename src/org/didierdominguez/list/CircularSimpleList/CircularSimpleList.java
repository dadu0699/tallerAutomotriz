package org.didierdominguez.list.CircularSimpleList;

public class CircularSimpleList {
    private CircularSimpleNode node;

    public CircularSimpleList() {
        node = null;
    }

    private boolean isEmpty() {
        return node == null;
    }

    public void addNode(Object object) {
        CircularSimpleNode newNode = new CircularSimpleNode(object);
        if (node != null) {
            newNode.setNextNode(node.getNextNode());
            node.setNextNode(newNode);
        }
        node = newNode;
    }

    public void readNodes() {
        if (!isEmpty()) {
            CircularSimpleNode auxiliaryNode = node.getNextNode();
            do {
                System.out.print(auxiliaryNode.getObject() + " --> ");
                // System.out.println(auxiliaryNode.getNextNode().getObject());
                auxiliaryNode = auxiliaryNode.getNextNode();
            } while (auxiliaryNode != node.getNextNode());
            System.out.println();
        }
    }

    public void updateNode(Object oldObject, Object newObject) {
        CircularSimpleNode node = searchNode(oldObject);
        if (node != null) {
            node.setObject(newObject);
        }
    }

    public void deleteSpecificNode(Object object) {
        CircularSimpleNode actualNode = node;
        boolean found = false;
        while (actualNode.getNextNode() != node && !found) {
            found = (actualNode.getNextNode().getObject() == object);
            if (!found) {
                actualNode = actualNode.getNextNode();
            }
        }
        found = (actualNode.getNextNode().getObject() == object);
        if (found) {
            CircularSimpleNode auxiliaryNode = actualNode.getNextNode();
            if (node == node.getNextNode()) {
                node = null;
            } else {
                if (auxiliaryNode == node) {
                    node = actualNode;
                }
                actualNode.setNextNode(auxiliaryNode.getNextNode());
            }
        }
    }

    public CircularSimpleNode searchNode(Object object) {
        CircularSimpleNode auxiliaryNode = node;
        do {
            if (auxiliaryNode.getObject() == object) {
                return auxiliaryNode;
            } else {
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
        } while (auxiliaryNode != node);
        return null;
    }

    public CircularSimpleNode getNode() {
        return node;
    }
}
