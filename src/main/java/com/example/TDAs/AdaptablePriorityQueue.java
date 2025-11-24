package com.example.TDAs;

interface AdaptablePriorityQueue<K, V> {
    void remove(Entry<K, V> entry);
    void replaceKey(Entry<K, V> entry, K key);
    void replaceValue(Entry<K, V> entry, V value);
    Entry<K, V> insert(K key, V value);
    boolean isEmpty();
    Entry<K, V> removeMin();

}