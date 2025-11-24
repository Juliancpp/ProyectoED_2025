package com.example.TDAs;

public class NodeSLL<E> {
        private E element;
        private NodeSLL<E> next;

        public NodeSLL(E e, NodeSLL<E> n) {
            element = e;
            next = n;
        }

        public E getElement() { return element; }
        public NodeSLL<E> getNext() { return next; }
        public void setNext(NodeSLL<E> n) { next = n; }
}
