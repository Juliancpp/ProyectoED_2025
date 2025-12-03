package com.example.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.example.TDAs.AVLTreeMap;
import com.example.TDAs.Entry;
import com.example.TDAs.Position;
import com.example.TDAs.SplayTreeMap;
import com.example.TDAs.TreeMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TreeVisualizer<K, V> {

    private static final int NODE_RADIUS = 20;
    private static final int Y_SPACING = 60;
    private static final int X_SPACING = 45;

    private final Color nodeColor;
    private final Color edgeColor;
    
    private int xCounter = 0;
    private Map<Object, Point> positions;

    public TreeVisualizer(Color nodeColor, Color edgeColor) {
        this.nodeColor = nodeColor;
        this.edgeColor = edgeColor;
    }

    public void draw(TreeMap<K, V> map, Canvas canvas) {
        drawUnified(map::root, map::left, map::right, canvas);
    }

    public void draw(AVLTreeMap<K, V> map, Canvas canvas) {
        drawUnified(map::root, map::left, map::right, canvas);
    }

    public void draw(SplayTreeMap<K, V> map, Canvas canvas) {
        drawUnified(map::root, map::left, map::right, canvas);
    }

    private void calculatePositions(Position<Entry<K, V>> p, int depth,
                                    Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                                    Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn) {
        if (p == null || p.getElement() == null) return;

        calculatePositions(leftFn.apply(p), depth + 1, leftFn, rightFn);

        xCounter++;
        double x = xCounter * X_SPACING;
        double y = depth * Y_SPACING;
        positions.put(p, new Point(x, y));

        calculatePositions(rightFn.apply(p), depth + 1, leftFn, rightFn);
    }

    private void drawEdges(GraphicsContext gc, Position<Entry<K, V>> p,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn) {
        if (p == null || p.getElement() == null) return;

        Point myPos = positions.get(p);
        
        Position<Entry<K, V>> left = leftFn.apply(p);
        if (left != null && left.getElement() != null) {
            Point childPos = positions.get(left);
            if (childPos != null) { // Safety check
                drawEdge(gc, myPos.x, myPos.y, childPos.x, childPos.y);
                drawEdges(gc, left, leftFn, rightFn);
            }
        }

        Position<Entry<K, V>> right = rightFn.apply(p);
        if (right != null && right.getElement() != null) {
            Point childPos = positions.get(right);
            if (childPos != null) {
                drawEdge(gc, myPos.x, myPos.y, childPos.x, childPos.y);
                drawEdges(gc, right, leftFn, rightFn);
            }
        }
    }

    private void drawNodes(GraphicsContext gc, Position<Entry<K, V>> p,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn) {
        if (p == null || p.getElement() == null) return;

        Point myPos = positions.get(p);
        drawNodeCircle(gc, p.getElement().getKey(), myPos.x, myPos.y);

        drawNodes(gc, leftFn.apply(p), leftFn, rightFn);
        drawNodes(gc, rightFn.apply(p), leftFn, rightFn);
    }

    private int getMaxDepth(Position<Entry<K, V>> p,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
                           Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn) {
        if (p == null || p.getElement() == null) return 0;
        int l = getMaxDepth(leftFn.apply(p), leftFn, rightFn);
        int r = getMaxDepth(rightFn.apply(p), leftFn, rightFn);
        return 1 + Math.max(l, r);
    }

    private void drawEmptyMessage(GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.GRAY);
        gc.setFont(new Font(20));
        gc.fillText("Árbol vacío", 50, 50);
    }

    private void drawNodeCircle(GraphicsContext gc, Object value, double x, double y) {
        gc.setFill(nodeColor);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 12));
        
        String text = String.valueOf(value);
        double textOffset = text.length() * 3.5;
        gc.fillText(text, x - textOffset, y + 5);
    }

    private void drawEdge(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.setStroke(edgeColor);
        gc.setLineWidth(2);
        gc.strokeLine(x1, y1, x2, y2);
    }

    private static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    private void drawUnified(
            Supplier<Position<Entry<K, V>>> rootFn,
            Function<Position<Entry<K, V>>, Position<Entry<K, V>>> leftFn,
            Function<Position<Entry<K, V>>, Position<Entry<K, V>>> rightFn,
            Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        gc.save();
        gc.setTransform(1, 0, 0, 1, 0, 0); 
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.restore();

        Position<Entry<K, V>> root = rootFn.get();
        
        if (root == null || root.getElement() == null) {
            drawEmptyMessage(gc, canvas);
            return;
        }

        positions = new HashMap<>();
        xCounter = 0;
        calculatePositions(root, 1, leftFn, rightFn);

        double treeWidth = (xCounter + 1) * X_SPACING + 100;
        double treeHeight = getMaxDepth(root, leftFn, rightFn) * Y_SPACING + 100;

        canvas.setWidth(treeWidth);
        canvas.setHeight(treeHeight);
 
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawEdges(gc, root, leftFn, rightFn);
        drawNodes(gc, root, leftFn, rightFn);
    }

}