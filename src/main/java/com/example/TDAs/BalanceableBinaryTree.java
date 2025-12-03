package com.example.TDAs;

public class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {

    protected static class BSTNode<E> extends TreeNode<E> {

        int aux = 0;

        BSTNode(E element, TreeNode<E> parent, TreeNode<E> leftChild, TreeNode<E> rightChild) {
            super(element, parent, leftChild, rightChild);
        }

        public int getAux() {
            return aux;
        }

        public void setAux(int aux) {
            this.aux = aux;
        }
    }
    public int getAux(Position<Entry<K, V>> position) {
        return ((BSTNode<Entry<K, V>>) position).getAux();
    }

    public void setAux(Position<Entry<K, V>> position, int value) {
        ((BSTNode<Entry<K, V>>) position).setAux(value);
    }


    protected TreeNode<Entry<K, V>> createNode(Entry<K, V> element,TreeNode<Entry<K, V>> parent, TreeNode<Entry<K, V>> left, TreeNode<Entry<K, V>> right) {
        return new BSTNode<>(element, parent, left, right);
    }

    private void relink(TreeNode<Entry<K, V>> parent, TreeNode<Entry<K, V>> child, boolean makeLeftChild) {
        child.setParent(parent);
        if (makeLeftChild) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }
    }

    public void rotate(Position<Entry<K, V>> position) {
        TreeNode<Entry<K, V>> x = validate(position);
        TreeNode<Entry<K, V>> y = x.getParent();
        TreeNode<Entry<K, V>> z = y.getParent();
        if (z == null) {
            root = x;
            x.setParent(null);
        } else {
            boolean makeLeftChild = y == z.getLeft();
            relink(z, x, makeLeftChild);
        }

        if (x == y.getLeft()) {
            relink(y, x.getRight(), true);
            relink(x, y, false);
        } else {
            relink(y, x.getLeft(), false);
            relink(x, y, true);
        }
    }

    public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
        Position<Entry<K, V>> y = parent(x);
        Position<Entry<K, V>> z = parent(y);
        if ((x == right(y)) == (y == right(z))) {
            rotate(y);
            return y;
        } else {
            rotate(x);
            rotate(x);
            return x;
        }
    }
}