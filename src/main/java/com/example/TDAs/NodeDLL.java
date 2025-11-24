package com.example.TDAs;

public class NodeDLL<E> {
        private E element;
        private NodeDLL<E> prev;
        private NodeDLL<E> next;

        public NodeDLL(E e, NodeDLL<E> p, NodeDLL<E> n) {
            element = e;
            prev = p;
            next = n;
        }

        public E getElement() { return element; }
        public NodeDLL<E> getPrev() { return prev; }
        public NodeDLL<E> getNext() { return next; }
        public void setPrev(NodeDLL<E> p) { prev = p; }
        public void setNext(NodeDLL<E> n) { next = n; }
    }