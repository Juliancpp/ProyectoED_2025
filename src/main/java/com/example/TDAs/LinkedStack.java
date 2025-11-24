package com.example.TDAs;

public class LinkedStack<E> extends SinglyLinkedList<E> implements Stack <E> {
    //L

    public LinkedStack() {
        super();
    }

    @Override
    public int size(){
        return super.size();
    }

    @Override
    public boolean isEmpty(){
        return super.isEmpty();
    }

    @Override
    public void push(E element){
        super.addFirst(element);
    }

    @Override
    public E pop(){
        return super.removeFirst();
    }

    @Override
    public E top(){
        return super.first();
    }
}
