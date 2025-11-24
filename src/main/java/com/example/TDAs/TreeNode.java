package com.example.TDAs;

public class TreeNode<E> implements Position<E> {
    private E element;
    private TreeNode<E> parent;
    private TreeNode<E> left;
    private TreeNode<E> right;
    public TreeNode(E e, TreeNode<E> above, TreeNode<E> leftChild, TreeNode<E> rightChild){
        element = e;
        parent = above;
        left = leftChild;
        right = rightChild;
    }
    public E getElement(){
        return element;
    }
    public TreeNode<E> getParent(){
        return parent;
    }
    public TreeNode<E> getLeft(){
        return left;
    }
    public TreeNode<E> getRight(){
        return right;
    }
    public void setElement(E e){
        element = e;
    }
    public void setParent(TreeNode<E> p){
        parent = p;
    }
    public void setLeft(TreeNode<E> l){
        left = l;
    }
    public void setRight(TreeNode<E> r){
        right = r;
    }
}