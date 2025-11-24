package com.example.TDAs;

import java.util.Comparator;

public class HeapAdaptablePriorityQueue<K, V> extends HeapPriorityQueue<K, V> implements AdaptablePriorityQueue<K, V> {

    //---------------- nested AdaptablePQEntry class ----------------
    protected static class AdaptablePQEntry<K, V> extends PQEntry<K, V> {

        private int index;

        public AdaptablePQEntry(K key, V value, int index) {
            super(key, value);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
    //----------- end of nested AdaptablePQEntry class -----------

    public HeapAdaptablePriorityQueue() {
        super();
    }

    public HeapAdaptablePriorityQueue(Comparator<K> comparator) {
        super(comparator);
    }

    protected AdaptablePQEntry<K, V> validate(Entry<K, V> entry) throws IllegalArgumentException {
        if (!(entry instanceof AdaptablePQEntry)) {
            throw new IllegalArgumentException("Invalid entry");
        }
        AdaptablePQEntry<K, V> locator = (AdaptablePQEntry<K, V>) entry;
        int index = locator.getIndex();
        if (index >= heap.size() || heap.get(index) != locator) {
            throw new IllegalArgumentException("Invalid entry");
        }
        return locator;
    }

    @Override
    protected void swap(int i, int j) {
        super.swap(i, j);
        AdaptablePQEntry<K, V> iEntry = (AdaptablePQEntry<K, V>) heap.get(i);
        AdaptablePQEntry<K, V> jEntry = (AdaptablePQEntry<K, V>) heap.get(j);
        iEntry.setIndex(j);
        jEntry.setIndex(i);
    }

    protected void bubble(int index) {
        if (index > 0 && compare(heap.get(index), heap.get(parent(index))) < 0) {
            upHeap(index);
        } else {
            downHeap(index);
        }
    }

    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K, V> newest = new AdaptablePQEntry<>(key, value, heap.size());
        heap.add(newest);
        upHeap(heap.size() - 1);
        return newest;
    }

    @Override
    public void remove(Entry<K, V> entry) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        int index = locator.getIndex();
        if (index == heap.size() - 1) {
            heap.remove(heap.size() - 1);
        } else {
            swap(index, heap.size() - 1);
            heap.remove(heap.size() - 1);
            bubble(index);
        }
    }

    @Override
    public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        checkKey(key);
        locator.setKey(key);
        bubble(locator.getIndex());
    }

    @Override
    public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        locator.setValue(value);
    }
}