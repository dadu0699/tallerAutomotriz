package org.didierdominguez.list.Stack;

public class Stack {
    private StackNode top;
    private int length;

    public Stack() {
        top = null;
        length = 0;
    }

    private boolean isEmpty() {
        return top == null;
    }

    public void push(Object object) {
        StackNode node = new StackNode(object);
        node.setNextNode(top);
        top = node;
        length++;
    }

    public void readNodes() {
        StackNode auxiliaryNode = top;
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getObject() + " <-- ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public Object pop() {
        if (!isEmpty()) {
            Object auxiliaryNode = top.getObject();
            top = top.getNextNode();
            length--;
            return auxiliaryNode;
        }
        return null;
    }

    public Object getTop() {
        if (!isEmpty()) {
            return top.getObject();
        }
        return null;
    }

    public int getLength() {
        return length;
    }

    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    public StackNode searchNode(Object object) {
        StackNode auxiliaryNode = top;
        while (auxiliaryNode != null && auxiliaryNode.getObject() != object) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }
}
