package com.nonobank.test.utils;

import com.nonobank.test.commons.MockException;

/**
 * Created by H.W. on 2018/4/10.
 */
public class RuleNode {
    private Node top;
    private int size;

    public RuleNode() {
        top = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return top == null ? true : false;
    }

    public void push(Object obj) {
        Node v = new Node(obj, top);
        top = v;
        size++;
    }

    public Object pop() throws MockException {
        if (isEmpty()) {
            throw new MockException("RuleNode为空");
        }
        Object temp = top.getElement();
        top = top.getNext();
        size--;
        return temp;
    }

    public Object top() throws MockException {
        if (isEmpty()) {
            throw new MockException("RuleNode为空");
        }
        return top.getElement();
    }

}


class Node {
    private Object element;
    private Node next;

    public Node() {
        this(null, null);
    }

    public Node(Object element, Node node) {
        this.element = element;
        this.next = node;
    }

    public Object getElement() {
        return this.element;
    }

    public Object setElement(Object e) {
        Object old = this.element;
        this.element = e;
        return old;
    }


    public Node getNext() {
        return this.next;
    }

    public void setNext(Node newNext) {
        this.next = newNext;
    }
}
