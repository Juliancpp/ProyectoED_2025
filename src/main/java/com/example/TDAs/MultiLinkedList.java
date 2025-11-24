package com.example.TDAs;

public class MultiLinkedList<E> {
    public static class MultiNode<E> {
        private E element;
        private MultiNode<E>[] links;

        @SuppressWarnings("unchecked")
        public MultiNode(E e, int linkCount) {
            element = e;
            links = (MultiNode<E>[]) new MultiNode[linkCount];
        }

        public E getElement() { return element; }
        public void setElement(E e) { element = e; }

        public MultiNode<E> getLink(int i) {
            if (i < 0 || i >= links.length) throw new IndexOutOfBoundsException();
            return links[i];
        }
        public void setLink(int i, MultiNode<E> node) {
            if (i < 0 || i >= links.length) throw new IndexOutOfBoundsException();
            links[i] = node;
        }
        public int getLinkCount() { return links.length; }
    }

    private MultiNode<E> head;
    private int size;
    private final int linkCount;

    public MultiLinkedList(int linkCount) {
        this.linkCount = linkCount;
        this.head = null;
        this.size = 0;
    }

    public void addFirst(E e) {
        MultiNode<E> newNode = new MultiNode<>(e, linkCount);
        if (head != null) newNode.setLink(0, head);
        head = newNode;
        size++;
    }

    public E removeFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        E elem = head.getElement();
        head = head.getLink(0);
        size--;
        return elem;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public MultiNode<E> getHead() {
        return head;
    }

    public java.util.List<E> getAllByLink(int linkIndex) {
    java.util.List<E> result = new java.util.ArrayList<>();
    MultiNode<E> current = head;
    while (current != null) {
        result.add(current.getElement());
        current = current.getLink(linkIndex);
    }
    return result;
}

}
