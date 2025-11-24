package com.example.utils;

import com.example.TDAs.Entry;
import com.example.TDAs.Position;
import com.example.TDAs.Tree;

//Uso de ChatGPT
//Usé la IA para que me diera directamente las herramientas de dibujo.
//Prompt: Oye, estoy haciendo una app en JavaFX y necesito un área para dibujar necesito poder pintar formas, cambiar colores y escribir texto Cuáles son los imports y las clases esenciales para hacerlo
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
}
