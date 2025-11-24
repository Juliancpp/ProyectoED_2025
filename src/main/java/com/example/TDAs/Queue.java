package com.example.TDAs;

public interface Queue<E> {
    int size();
    boolean isEmpty();
    E first();//leer el primer elemento, get head
    void enqueue(E e); //last 
    E dequeue();
    
}
