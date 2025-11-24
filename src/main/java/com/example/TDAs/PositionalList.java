package com.example.TDAs;

import java.util.Iterator;

public interface PositionalList<E> extends Iterable<E> {

    Position<E> first();
    Position<E> last();
    Position<E> before(Position<E> p) throws IllegalStateException;
    Position<E> after(Position<E> p) throws IllegalStateException;
    Position<E> addFirst(E element);
    Position<E> addLast(E element);
    Position<E> addBefore(Position<E> p, E element) throws IllegalStateException;
    Position<E> addAfter(Position<E> p, E element) throws IllegalStateException;
    E set(Position<E> p, E element) throws IllegalStateException;
    E remove(Position<E> p) throws IllegalArgumentException;
    int size();
    boolean isEmpty();
    Iterator<E> iterator();
    Iterable<Position<E>> positions();
}