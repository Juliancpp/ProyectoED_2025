package com.example.TDAs;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTree<E> implements Tree<E> {
    @Override
    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }
    @Override
    public boolean isExternal(Position<E> p){
        return numChildren(p) == 0;
    }
    @Override
    public boolean isRoot(Position<E> p) {
    return p == root();
    }
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    public int depth(Position<E> p) {
        if (isRoot(p))
            return 0;
        else
            return 1 + depth(parent(p));
    }
    @SuppressWarnings("unused")
    private int heightBad(){
        int h = 0;
        for (Position<E> p : positions())
            if(isExternal(p))
                h = Math.max(h, depth(p));
        return h;
    }
    public int height(Position<E> p) {
        int h = 0;
        for (Position<E> child : children(p))
            h = Math.max(h, 1 + height(child));
        return h;
    }
    private void preOrderSubtree(Position<E> position, List<Position<E>> snapshot) {
        snapshot.add(position);
        for (Position<E> child : children(position)) {
            preOrderSubtree(child, snapshot);
        }
    }
    public Iterable<Position<E>> preOrder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            preOrderSubtree(root(), snapshot);
        }
        return snapshot;
    }
    private void postOrderSubtree(Position<E> position, List<Position<E>> snapshot) {
        for (Position<E> child : children(position)) {
            postOrderSubtree(child, snapshot);
        }
        snapshot.add(position);
    }
    public Iterable<Position<E>> postOrder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            postOrderSubtree(root(), snapshot);
        }
        return snapshot;
    }
    public Iterable<Position<E>> breathFirst() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            LinkedQueue<Position<E>> fringe = new LinkedQueue<>();
            fringe.enqueue(root());
            
            while (!fringe.isEmpty()) {
                Position<E> position = fringe.dequeue();
                snapshot.add(position);
                for (Position<E> child : children(position)) {
                    fringe.enqueue(child);
                }
            }
        }
        return snapshot;
    }
}