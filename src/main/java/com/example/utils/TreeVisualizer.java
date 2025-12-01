package com.example.utils;

import com.example.TDAs.Entry;
import com.example.TDAs.Position;
import com.example.TDAs.Tree;
import com.example.TDAs.TreeMap;
import com.example.TDAs.AVLTreeMap;
import com.example.TDAs.SplayTreeMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * TreeVisualizer: dibuja árboles que exponen API positional (root, left, right, isInternal).
 * Soporta:
 *  - Tree<Entry<K,V>> (tu interfaz Tree)
 *  - TreeMap<K,V>, AVLTreeMap<K,V>, SplayTreeMap<K,V> (sobrecargas)
 */
public class TreeVisualizer<K, V> {

    private static final int NODE_RADIUS = 22;
    private static final int LEVEL_HEIGHT = 70;
    private static final int H_SPACING = 40;

    private final Color nodeColor;
    private final Color edgeColor;

    public TreeVisualizer(Color nodeColor, Color edgeColor) {
        this.nodeColor = nodeColor;
        this.edgeColor = edgeColor;
    }

    /* ------------------------------------------
       Método original: si tienes un Tree<Entry<K,V>>
       ------------------------------------------ */
    public void draw(Tree<Entry<K, V>> tree, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (tree == null || tree.isEmpty()) {
            gc.setFill(Color.GRAY);
            gc.setFont(new Font(20));
            gc.fillText("Árbol vacío", canvas.getWidth() / 2 - 40, canvas.getHeight() / 2);
            return;
        }

        int height = treeHeight(tree, tree.root());
        double widthNeeded = Math.pow(2, height) * (NODE_RADIUS + H_SPACING);

        canvas.setWidth(Math.max(canvas.getWidth(), widthNeeded));
        canvas.setHeight(Math.max(canvas.getHeight(), height * LEVEL_HEIGHT + 100));

        drawRecursive(gc, tree, tree.root(), canvas.getWidth() / 2, 60, canvas.getWidth() / 4);
    }

    /* ----------------------------------------------------------
       NUEVAS SOBRECARGAS: aceptan directamente tus implementaciones
       (asumen API pública: root(), left(p), right(p), isInternal(p))
       ---------------------------------------------------------- */

    public void draw(TreeMap<K, V> map, Canvas canvas) {
        drawFromPositionalProvider(map::root, map::left, map::right, map::isInternal, canvas);
    }

    public void draw(AVLTreeMap<K, V> map, Canvas canvas) {
        drawFromPositionalProvider(map::root, map::left, map::right, map::isInternal, canvas);
    }

    public void draw(SplayTreeMap<K, V> map, Canvas canvas) {
        drawFromPositionalProvider(map::root, map::left, map::right, map::isInternal, canvas);
    }

    /* ----------------------------------------------------------
       Helper genérico que dibuja utilizando funciones proveedoras
       rootFn, leftFn, rightFn, isInternalFn (basado en Position<Entry<K,V>>)
       ---------------------------------------------------------- */
    private void drawFromPositionalProvider(
            java.util.function.Supplier<Position<Entry<K, V>>> rootFn,
            java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
            java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn,
            java.util.function.Function<Position<Entry<K, V>>, Boolean> isInternalFn,
            Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Position<Entry<K, V>> root = rootFn.get();
        if (root == null || !isInternalFn.apply(root)) {
            gc.setFill(Color.GRAY);
            gc.setFont(new Font(20));
            gc.fillText("Árbol vacío", canvas.getWidth() / 2 - 40, canvas.getHeight() / 2);
            return;
        }

        // compute height using the provided functions
        int height = treeHeightFromFns(root, leftFn, rightFn, isInternalFn);
        double widthNeeded = Math.pow(2, height) * (NODE_RADIUS + H_SPACING);

        canvas.setWidth(Math.max(canvas.getWidth(), widthNeeded));
        canvas.setHeight(Math.max(canvas.getHeight(), height * LEVEL_HEIGHT + 100));

        drawRecursiveFromFns(gc, root, leftFn, rightFn, isInternalFn, canvas.getWidth() / 2, 60, canvas.getWidth() / 4);
    }

    /* recursive drawer for Tree interface (existing) */
    private void drawRecursive(GraphicsContext gc, Tree<Entry<K, V>> tree, Position<Entry<K, V>> p,
                               double x, double y, double offsetX) {

        if (p == null) return;

        drawNode(gc, p.getElement().getKey(), x, y);

        Iterable<Position<Entry<K, V>>> children = tree.children(p);
        Position<Entry<K, V>> left = null;
        Position<Entry<K, V>> right = null;

        int i = 0;
        for (Position<Entry<K, V>> c : children) {
            if (i == 0) left = c;
            else right = c;
            i++;
        }

        if (left != null) {
            double newX = x - offsetX;
            double newY = y + LEVEL_HEIGHT;
            drawEdge(gc, x, y, newX, newY);
            drawRecursive(gc, tree, left, newX, newY, offsetX / 2);
        }

        if (right != null) {
            double newX = x + offsetX;
            double newY = y + LEVEL_HEIGHT;
            drawEdge(gc, x, y, newX, newY);
            drawRecursive(gc, tree, right, newX, newY, offsetX / 2);
        }
    }

    /* recursive drawer for positional-provider functions */
    private void drawRecursiveFromFns(GraphicsContext gc, Position<Entry<K, V>> p,
                                      java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                                      java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn,
                                      java.util.function.Function<Position<Entry<K, V>>, Boolean> isInternalFn,
                                      double x, double y, double offsetX) {

        if (p == null) return;

        drawNode(gc, p.getElement().getKey(), x, y);

        Position<Entry<K, V>> left = leftFn.apply(p);
        Position<Entry<K, V>> right = rightFn.apply(p);

        if (isInternalFn.apply(left)) {
            double newX = x - offsetX;
            double newY = y + LEVEL_HEIGHT;
            drawEdge(gc, x, y, newX, newY);
            drawRecursiveFromFns(gc, left, leftFn, rightFn, isInternalFn, newX, newY, offsetX / 2);
        }

        if (isInternalFn.apply(right)) {
            double newX = x + offsetX;
            double newY = y + LEVEL_HEIGHT;
            drawEdge(gc, x, y, newX, newY);
            drawRecursiveFromFns(gc, right, leftFn, rightFn, isInternalFn, newX, newY, offsetX / 2);
        }
    }

    private void drawNode(GraphicsContext gc, Object value, double x, double y) {
        gc.setFill(nodeColor);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(14));
        gc.fillText(String.valueOf(value), x - 6, y + 4);
    }

    private void drawEdge(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.setStroke(edgeColor);
        gc.setLineWidth(2);
        gc.strokeLine(x1, y1, x2, y2);
    }

    private int treeHeight(Tree<Entry<K, V>> tree, Position<Entry<K, V>> p) {
        if (p == null) return 0;

        int max = 0;
        for (Position<Entry<K, V>> child : tree.children(p)) {
            int h = treeHeight(tree, child);
            if (h > max) max = h;
        }
        return max + 1;
    }

    private int treeHeightFromFns(Position<Entry<K, V>> p,
                                  java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                                  java.util.function.Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn,
                                  java.util.function.Function<Position<Entry<K, V>>, Boolean> isInternalFn) {
        if (p == null) return 0;
        int leftH = isInternalFn.apply(leftFn.apply(p)) ?  treeHeightFromFns(leftFn.apply(p), leftFn, rightFn, isInternalFn) : 0;
        int rightH = isInternalFn.apply(rightFn.apply(p)) ? treeHeightFromFns(rightFn.apply(p), leftFn, rightFn, isInternalFn) : 0;
        return 1 + Math.max(leftH, rightH);
    }
}
