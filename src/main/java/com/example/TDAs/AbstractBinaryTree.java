package com.example.TDAs;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBinaryTree<E> extends AbstractTree<E> implements BinaryTree<E> {
    @Override
    public Position<E> sibling(Position<E> p){
        Position<E> parent = parent(p);
        if(parent == null)
            return null;
        if(p == left(parent))
            return right(parent);
        else
            return left(parent);
    }
    @Override
    public int numChildren(Position<E> p){
        int count = 0;
        if(left(p) != null)
            count++;
        if(right(p) != null)
            count++;
        return count;
    }
    @Override
    public Iterable<Position<E>> children(Position<E> p){
        List<Position<E>> snapshot = new ArrayList<>(2);
        if(left(p) != null)
            snapshot.add(left(p));
        if (right(p) != null)
            snapshot.add(right(p));
        return snapshot;
    }
    private void inOrderSubtree(Position<E> position, List<Position<E>> snapshot) {
        if (left(position) != null)
            inOrderSubtree(left(position), snapshot);
        snapshot.add(position);
        if (right(position) != null)
            inOrderSubtree(right(position), snapshot);
    }
    public Iterable<Position<E>> inOrder(){
        List<Position<E>> snapshot = new ArrayList<>();
        if(!isEmpty())
            inOrderSubtree(root(), snapshot);
        return snapshot;
    }
    public Iterable<Position<E>> position(){
        return inOrder();
    }

    //preorder y post order se implementan en AbstractTree
    public Iterable<Position<E>> preOrder(){
        return super.preOrder();
    }
    public Iterable<Position<E>> postOrder(){
        return super.postOrder();
    }

}