package com.example.TDAs;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class LinkedTree<E> extends AbstractTree<E> {
    protected GenericTreeNode<E> root = null;
    private int size = 0;
    public LinkedTree() {}
    protected GenericTreeNode<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof GenericTreeNode))
            throw new IllegalArgumentException("Not a valid Position type");
        GenericTreeNode<E> node = (GenericTreeNode<E>) p;
        if (node.getParent() == node)
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }
    
    @Override
    public int size() {
        return size;
    }
    @Override
    public Position<E> root() {
        return root;
    }
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(p);
        return node.getParent();
    }
    @Override
    public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(p);
        List<Position<E>> childPositions = new ArrayList<>();
        for (GenericTreeNode<E> child : node.getChildren()) {
            childPositions.add(child);
        }
        return childPositions;
    }
    public Position<E> addRoot(E e) throws IllegalArgumentException {
        if (root != null) throw new IllegalArgumentException("Tree is not empty");
        root = new GenericTreeNode<>(e, null);
        size = 1;
        return root;
    }
    public Position<E> addChild(Position<E> p, E e) throws IllegalArgumentException {
        GenericTreeNode<E> parent = validate(p);
        GenericTreeNode<E> child = new GenericTreeNode<>(e, parent);
        parent.getChildren().add(child);
        size++;
        return child;
    }
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }
    public E remove(Position<E> p) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(p);
        if (node == root) {
            root = null;
        } else {
            GenericTreeNode<E> parent = node.getParent();
            parent.getChildren().remove(node);
        }
        size -= 1 + descendantCount(node);
        E temp = node.getElement();
        node.setElement(null);
        node.setParent(null);
        node.getChildren().clear();
        return temp;
    }
    private int descendantCount(GenericTreeNode<E> node) {
        int count = 0;
        for (GenericTreeNode<E> child : node.getChildren()) {
            count += 1 + descendantCount(child);
        }
        return count;
    }
    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> positionIterator = positions().iterator();
        @Override
        public boolean hasNext() {
            return positionIterator.hasNext();
        }
        @Override
        public E next() {
            return positionIterator.next().getElement();
        }
        @Override
        public void remove() {
            positionIterator.remove();
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new ElementIterator();
    }
    @Override
    public Iterable<Position<E>> positions() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            preorderSubtree(root, snapshot);
        }
        return snapshot;
    }
    private void preorderSubtree(GenericTreeNode<E> node, List<Position<E>> snapshot) {
        snapshot.add(node);
        for (GenericTreeNode<E> child : node.getChildren()) {
            preorderSubtree(child, snapshot);
        }
    }
    @Override
    public boolean isInternal(Position<E> position) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(position);
        return !node.getChildren().isEmpty();
    }
    public boolean isLeaf(Position<E> position) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(position);
        return node.getChildren().isEmpty();
    }
    @Override
    public boolean isRoot(Position<E> position) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(position);
        return node.getParent() == null;
    }

    @Override
    public int numChildren(Position<E> position) throws IllegalArgumentException {
        GenericTreeNode<E> node = validate(position);
        return node.getChildren().size();
    }

    public Iterable<Position<E>> postOrder(){
        return super.postOrder();
    }
    public Iterable<Position<E>> preOrder(){
        return super.preOrder();
    }

}