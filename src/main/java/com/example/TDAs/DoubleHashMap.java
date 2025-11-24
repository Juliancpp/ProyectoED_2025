package com.example.TDAs;

import java.util.ArrayList;

public class DoubleHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);
    private int q;

    public DoubleHashMap() {
        super();
        this.q = findPrime(capacity / 2);
    }

    public DoubleHashMap(int cap) {
        super(cap);
        this.q = findPrime(cap / 2);
    }

    public DoubleHashMap(int cap, int p) {
        super(cap, p);
        this.q = findPrime(cap / 2);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createTable() {
        table = (MapEntry<K, V>[]) new MapEntry[capacity];
    }

    private int secondaryHash(K key) {
        return q - (Math.abs(key.hashCode()) % q);
    }

    private int findSlot(int h, K k) {
        int d = secondaryHash(k);
        int j = 0;
        int slot;
        do {
            slot = (h + j * d) % capacity;
            if (table[slot] == null || table[slot] == DEFUNCT) {
                return -(slot + 1);
            }
            if (table[slot].getKey().equals(k)) {
                return slot;
            }
            j++;
        } while (j < capacity);
        return -1;
    }

    @Override
    protected V bucketGet(int h, K k) {
        int slot = findSlot(h, k);
        if (slot < 0) return null;
        return table[slot].getValue();
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        int slot = findSlot(h, k);
        if (slot >= 0) {
            return table[slot].setValue(v);
        }
        table[-(slot + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        int slot = findSlot(h, k);
        if (slot < 0) return null;
        V answer = table[slot].getValue();
        table[slot] = DEFUNCT;
        n--;
        return answer;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i] != DEFUNCT) {
                buffer.add(table[i]);
            }
        }
        return buffer;
    }

    private int findPrime(int limit) {
        for (int i = limit; i >= 2; i--) {
            if (isPrime(i)) return i;
        }
        return 2;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    public int GetHashValue(K key) {
        return findSlot(GetHashValueKey(key), key);
    }
}
