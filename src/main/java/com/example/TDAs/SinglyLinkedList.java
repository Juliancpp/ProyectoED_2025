package com.example.TDAs;
import java.util.ArrayList; ///ver
import java.util.List; ///ver

public class SinglyLinkedList<E> {
    public static class Node<E> {
        protected E element;
        protected Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public E getElement() { return element; }
        public Node<E> getNext() { return next; }
        public void setNext(Node<E> n) { next = n; }
    }

    public Node<E> head = null;
    protected Node<E> tail = null;
    protected int size = 0;

    public SinglyLinkedList() { }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public E first() {
        if (isEmpty()) return null;
        return head.getElement();
    }
    public E last() {
        if (isEmpty()) return null;
        return tail.getElement();
    }

    public void addFirst(E e) {
        head = new Node<>(e, head);
        if (size == 0)
            tail = head;
        size++;
    }

    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty())
            head = newest;
        else
            tail.setNext(newest);
        tail = newest;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        E answer = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0)
            tail = null;
        return answer;
    }

    public E find(E toFind) {
        Node<E> current = head;
        while (current != null) {
            if (toFind.equals(current.getElement())) {
                return current.getElement();
            }
            current = current.getNext();
        }
        return null;
    }

    public List<E> traverse() {///iterator
    List<E> elements = new ArrayList<>();
    Node<E> current = head;
    while (current != null) {
        elements.add(current.getElement());
        current = current.getNext();
    }
    return elements;
    }
}
