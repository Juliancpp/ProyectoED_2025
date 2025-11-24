package com.example.TDAs;

import java.util.Comparator;

public class Lexicographic implements Comparator<Object> {
    private int xa, ya, xb, yb;

    public int compare(Object a, Object b) throws ClassCastException {
        xa = ((Point2D) a).getX();
        ya = ((Point2D) a).getY();
        xb = ((Point2D) b).getX();
        yb = ((Point2D) b).getY();

        if (xa != xb)
            return (xb - xa);
        else
            return (yb - ya);
    }
}
