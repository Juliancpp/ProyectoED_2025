package com.example.TDAs;

import java.util.ArrayList;
import java.util.Comparator;

public class TreeMap<K, V> extends AbstractSortedMap<K, V> {
    protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();
    public TreeMap() {
        super();
        tree.addRoot(null);
    }
    public TreeMap(Comparator<K> comp) {
        super(comp);
        tree.addRoot(null);
    }

    @Override
    public int size() {
        return (tree.size() - 1) / 2;
    }

    private void expandExternal(Position<Entry<K, V>> position, Entry<K, V> entry) {
        tree.set(position, entry);
        tree.addLeft(position, null);
        tree.addRight(position, null);
    }

    public Position<Entry<K, V>> root() {
        return tree.root();
    }

    protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) {
        return tree.parent(p);
    }

    public Position<Entry<K, V>> left(Position<Entry<K, V>> p) {
        return tree.left(p);
    }

    public Position<Entry<K, V>> right(Position<Entry<K, V>> p) {
        return tree.right(p);
    }

    protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) {
        return tree.sibling(p);
    }

    protected boolean isRoot(Position<Entry<K, V>> p) {
        return tree.isRoot(p);
    }

    protected boolean isExternal(Position<Entry<K, V>> p) {
        return tree.isExternal(p);
    }

    protected boolean isInternal(Position<Entry<K, V>> p) {
        return tree.isInternal(p);
    }

    protected void set(Position<Entry<K, V>> p, Entry<K, V> e) {
        tree.set(p, e);
    }

    protected Entry<K, V> remove(Position<Entry<K, V>> p) {
        return tree.remove(p);
    }

    protected void rotate(Position<Entry<K, V>> p) {
        tree.rotate(p);
    }

    protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
        return tree.restructure(x);
    }

    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> position, K key) {
        if (isExternal(position)) {
            return position;
        }

        int comp = compare(key, position.getElement());
        if (comp == 0) {
            return position;
        } else if (comp < 0) {
            return treeSearch(left(position), key);
        } else {
            return treeSearch(right(position), key);
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> position = treeSearch(root(), key);
        rebalanceAccess(position);

        if (isExternal(position)) {
            return null;
        }

        return position.getElement().getValue();
    }

    @Override
    public V put(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K, V> newEntry = new MapEntry<>(key, value);
        Position<Entry<K, V>> position = treeSearch(root(), key);

        if (isExternal(position)) {
            expandExternal(position, newEntry);
            rebalanceInsert(position);
            return null;
        } else {
            V old = position.getElement().getValue();
            set(position, newEntry);
            rebalanceAccess(position);
            return old;
        }
    }

    @Override
    public V remove(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> position = treeSearch(root(), key);
        if (isExternal(position)) {
            rebalanceAccess(position);
            return null;
        } else {
            V old = position.getElement().getValue();
            if (isInternal(left(position)) && isExternal(right(position))) {
                Position<Entry<K, V>> replacement = treeMax(left(position));
                set(position, replacement.getElement());
                position = replacement;
            }
            Position<Entry<K, V>> leaf = isExternal(left(position)) ? left(position) : right(position);
            Position<Entry<K, V>> sibling = sibling(leaf);
            remove(leaf);
            remove(position);
            rebalanceDelete(sibling);
            return old;
        }
    }

    protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> position) {
        Position<Entry<K, V>> walk = position;
        while (isInternal(walk)) {
            walk = right(walk);
        }
        return parent(walk);
    }

    @Override
    public Entry<K, V> lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return treeMax(root()).getElement();
    }

    @Override
    public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> position = treeSearch(root(), key);
        if (isInternal(position)) {
            return position.getElement();
        }
        while (!isRoot(position)) {
            if (position == right(parent(position))) {
                return parent(position).getElement();
            } else {
                position = parent(position);
            }
        }
        return null;
    }

    @Override
    public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> position = treeSearch(root(), key);
        if (isInternal(position) && isInternal(left(position))) {
            return treeMax(left(position)).getElement();
        }
        while (!isRoot(position)) {
            if (position == right(parent(position))) {
                return parent(position).getElement();
            } else {
                position = parent(position);
            }
        }
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
        for (Position<Entry<K, V>> position : tree.inOrder()) {
            if (isInternal(position)) {
                buffer.add(position.getElement());
            }
        }
        return buffer;
    }

    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
        if (compare(fromKey, toKey) < 0) {
            subMapRecurse(fromKey, toKey, root(), buffer);
        }
        return buffer;
    }

    private void subMapRecurse(K fromKey, K toKey, Position<Entry<K, V>> position, ArrayList<Entry<K, V>> buffer) {
        if (isInternal(position)) {
            if (compare(position.getElement(), fromKey) < 0) {
                subMapRecurse(fromKey, toKey, right(position), buffer);
            } else {
                subMapRecurse(fromKey, toKey, left(position), buffer);
                if (compare(position.getElement(), toKey) < 0) {
                    buffer.add(position.getElement());
                    subMapRecurse(fromKey, toKey, right(position), buffer);
                }
            }
        }
    }

    protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
        Position<Entry<K, V>> walk = p;
        while (isInternal(walk)) {
            walk = left(walk);
        }
        return parent(walk);
    }

    @Override
    public Entry<K, V> firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return treeMin(root()).getElement();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isInternal(p)) {
            return p.getElement();
        }
        while (!isRoot(p)) {
            if (p == left(parent(p))) {
                return parent(p).getElement();
            } else {
                p = parent(p);
            }
        }
        return null;
    }

    @Override
    public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isInternal(p) && isInternal(right(p))) {
            return treeMin(right(p)).getElement();
        }
        while (!isRoot(p)) {
            if (p == left(parent(p))) {
                return parent(p).getElement();
            } else {
                p = parent(p);
            }
        }
        return null;
    }

    /*protected void rebalanceInsert(Position<Entry<K, V>> p) {
    }

    protected void rebalanceDelete(Position<Entry<K, V>> p) {
    }

    protected void rebalanceAccess(Position<Entry<K, V>> p) {
    }*/

    //metodos auxiliares
   protected int height(Position<Entry<K,V>> p) {
        return tree.getAux(p);
    }


    protected void recomputeHeight(Position<Entry<K, V>> p) {
        tree.setAux(p, 1 + Math.max(height(left(p)), height(right(p))));
    }

    protected boolean isBalanced(Position<Entry<K, V>> p) {
        return Math.abs(height(left(p)) - height(right(p))) <= 1;
    }

    protected Position<Entry<K, V>> tallerChild(Position<Entry<K, V>> p) {
        if (height(left(p)) > height(right(p))) return left(p);
        if (height(left(p)) < height(right(p))) return right(p);
        if (isRoot(p)) return left(p);
        if (p == left(parent(p))) return left(p);
        else return right(p);
    }

    protected void rebalance(Position<Entry<K, V>> p) {
        int oldHeight, newHeight;
        do {
            oldHeight = height(p);
            if (!isBalanced(p)) {
                p = restructure(tallerChild(tallerChild(p)));
                recomputeHeight(left(p));
                recomputeHeight(right(p));
            }
            recomputeHeight(p);
            newHeight = height(p);
            p = parent(p);
        } while (oldHeight != newHeight && p != null);
    }

   
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        rebalance(p);
    }

   
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        if (!isRoot(p))
            rebalance(parent(p));
    }


    protected void rebalanceAccess(Position<Entry<K, V>> p) {
    }

}