package com.example.TDAs;

import java.util.ArrayList;

public class ProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V> [] table;
    private MapEntry<K,V> DEFUNCT = new MapEntry<>(null, null);
    public ProbeHashMap(){
        super();
    }
    public ProbeHashMap(int cap){
        super(cap);
    }
    public ProbeHashMap(int cap, int p){
        super(cap,p);
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void createTable() {
        table = (MapEntry<K, V>[]) new MapEntry[capacity];
    }
    private boolean isAvailable(int h){
        return table[h] == null || table[h] == DEFUNCT;
    }

    //linear probing methods
    private int findSlot(int h, K k){
        int avail = -1;
        int j = h;
        do{
            if(isAvailable(j)){
                if(avail == -1) avail = j;
                if (table[j] == null) break;
            } else if (table[j].getKey().equals(k)) {
                return j;
            }
            j = (j + 1) % capacity;
        } while (j != h);
        return -(avail + 1);
    }

    
    @Override
    protected V bucketGet(int h, K k){
        int slot = findSlot(h, k);
        if(slot < 0) return null;
        return table[slot].getValue();
    }
    @Override
    protected V bucketPut(int h, K k, V v){
        int slot = findSlot(h, k);
        if(slot >= 0){
            return table[slot].setValue(v);
        }
        table[-(slot + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }
    @Override
    protected V bucketRemove(int h, K k){
        int slot = findSlot(h, k);
        if(slot < 0) return null;
        V answer = table[slot].getValue();
        table[slot] = DEFUNCT;
        n--;
        return answer;
    }
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if(!isAvailable(i)){
                buffer.add(table[i]);
            }
        }
        return buffer;
    }
}