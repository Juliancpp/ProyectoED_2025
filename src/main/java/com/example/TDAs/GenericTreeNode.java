package com.example.TDAs;

import java.util.ArrayList;
import java.util.List;

public class GenericTreeNode<E> implements Position<E> {
    private E element;
    private GenericTreeNode<E> parent;
    private List<GenericTreeNode<E>> children;
    public GenericTreeNode(E element, GenericTreeNode<E> parent) {
        this.element = element;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
    public E getElement() { return element; }
    public void setElement(E element) { this.element = element; }
    public GenericTreeNode<E> getParent() { return parent; }
    public void setParent(GenericTreeNode<E> parent) { this.parent = parent; }
    public List<GenericTreeNode<E>> getChildren() { return children; }
}