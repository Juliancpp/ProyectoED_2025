package com.example.TDAs;

public class LinkedQueue<E> extends SinglyLinkedList<E> implements Queue<E> {
    //F

    public LinkedQueue(){
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
    public E first(){
        return super.first();
    }

    @Override
    public void enqueue(E e){
        super.addLast(e);
    } 

    @Override
    public E dequeue(){
        return super.removeFirst();
    }
    
}
