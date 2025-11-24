package com.example.TDAs;

import java.util.Iterator;

public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    protected TreeNode<E> createNode(E e, TreeNode<E> parent, TreeNode<E> left, TreeNode<E> right){
        return new TreeNode<>(e, parent, left, right);
    }
    protected TreeNode<E> root = null;
    private int size = 0;
    public LinkedBinaryTree(){}
    protected TreeNode<E> validate(Position<E> p) throws IllegalArgumentException{
        if(!(p instanceof TreeNode))
            throw new IllegalArgumentException("Not a valid Position type");
        TreeNode<E> node = (TreeNode<E>) p;
        if(node.getParent() == node)
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
    public Position<E> parent(Position<E> p) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        return node.getParent();
    }
    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        return node.getLeft();
    }
    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        return node.getRight();
    }
    public Position<E> addRoot(E e) throws IllegalArgumentException{
        if(!isEmpty()) throw new IllegalArgumentException("Tree is not empty");
        size = 1;
        root = createNode(e, null, null, null);
        return root;
    }
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException{
        TreeNode<E> parent = validate(p);
        if(parent.getLeft() != null) throw new IllegalArgumentException("Left child already exists");
        TreeNode<E> leftChild = createNode(e, parent, null, null);
        size++;
        parent.setLeft(leftChild);
        return leftChild;
    }
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException{
        TreeNode<E> parent = validate(p);
        if(parent.getRight() != null) throw new IllegalArgumentException("Right child already exists");
        TreeNode<E> rightChild = createNode(e, parent, null, null);
        size++;
        parent.setRight(rightChild);
        return rightChild;
    }
    public E set(Position<E> p, E e) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }
    public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        if(isInternal(p)) throw new IllegalArgumentException("p must be leaf");
        size += t1.size() + t2.size();
        if(!t1.isEmpty()){
            t1.root.setParent(node);
            node.setLeft(t1.root);
            t1.root = null;
            t1.size = 0;
        }
        if(!t2.isEmpty()){
            t2.root.setParent(node);
            node.setRight(t2.root);
            t2.root = null;
            t2.size = 0;
        }
    }
    public E remove(Position<E> p) throws IllegalArgumentException{
        TreeNode<E> node = validate(p);
        if(numChildren(p) == 2) throw new IllegalArgumentException("p has two children");
        TreeNode<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
        if(child != null) 
            child.setParent(node.getParent());
        if(node == root)
            root = child;
        else{
            TreeNode<E> parent = node.getParent();
            if(node == parent.getLeft())
                parent.setLeft(child);
            else
                parent.setRight(child);
        }
        size--;
        E temp = node.getElement();
        node.setElement(null);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(node);
        return temp;
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
    public boolean isExternal(Position<E> p){
        return super.isExternal(p);
    }
    public boolean isInternal(Position<E> p){
        return super.isInternal(p);
    }

    @Override
    public Iterator<E> iterator(){
        return new ElementIterator();
    }
    @Override
    public Iterable<Position<E>> positions(){
        return preOrder();
    }
    public Iterable<Position<E>> inOrder(){
        return super.inOrder();
    }
    public Iterable<Position<E>> postOrder(){
        return super.postOrder();
    }
    public Iterable<Position<E>> preOrder(){
        return super.preOrder();
    }

}
