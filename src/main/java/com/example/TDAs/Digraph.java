package com.example.TDAs;

public class Digraph<V,E> extends AdjacencyMapGraph<V,E> {
    public Digraph() {
        super(true);
    }

    public void DFS(Vertex<V> u, Map<Vertex<V>,Boolean> known, Map<Vertex<V>,Edge<E>> forest) {
        known.put(u, true);
        for (Edge<E> e : outgoingEdges(u)) {
            Vertex<V> v = opposite(u, e);
            if (!known.get(v)) {
                forest.put(v, e);
                DFS(v, known, forest);
            }
        }
    }

    public Map<Vertex<V>,Edge<E>> DFSComplete() {
        Map<Vertex<V>,Boolean> known = new ProbeHashMap<>();
        Map<Vertex<V>,Edge<E>> forest = new ProbeHashMap<>();
        for (Vertex<V> u : vertices())
            known.put(u, false);
        for (Vertex<V> u : vertices())
            if (!known.get(u))
                DFS(u, known, forest);
        return forest;
    }

    public void BFS(Vertex<V> s, Map<Vertex<V>,Boolean> known, Map<Vertex<V>,Edge<E>> forest) {
        Queue<Vertex<V>> queue = new LinkedQueue<>();
        known.put(s, true);
        queue.enqueue(s);
        while (!queue.isEmpty()) {
            Vertex<V> u = queue.dequeue();
            for (Edge<E> e : outgoingEdges(u)) {
                Vertex<V> v = opposite(u, e);
                if (!known.get(v)) {
                    known.put(v, true);
                    forest.put(v, e);
                    queue.enqueue(v);
                }
            }
        }
    }

    public Map<Vertex<V>,Edge<E>> BFSComplete() {
        Map<Vertex<V>,Boolean> known = new ProbeHashMap<>();
        Map<Vertex<V>,Edge<E>> forest = new ProbeHashMap<>();
        for (Vertex<V> u : vertices())
            known.put(u, false);
        for (Vertex<V> u : vertices())
            if (!known.get(u))
                BFS(u, known, forest);
        return forest;
    }

    public static <V,E> PositionalList<Vertex<V>> topologicalSort(Graph<V,E> g) {
        PositionalList<Vertex<V>> topo = new LinkedPositionalList<>();
        Stack<Vertex<V>> ready = new LinkedStack<>();
        Map<Vertex<V>, Integer> inCount = new ProbeHashMap<>();
        for (Vertex<V> u : g.vertices()) {
            inCount.put(u, g.inDegree(u));
            if (inCount.get(u) == 0)
                ready.push(u);
        }
        while (!ready.isEmpty()) {
            Vertex<V> u = ready.pop();
            topo.addLast(u);
            for (Edge<E> e : g.outgoingEdges(u)) {
                Vertex<V> v = g.opposite(u, e);
                inCount.put(v, inCount.get(v) - 1);
                if (inCount.get(v) == 0)
                    ready.push(v);
            }
        }
        return topo;
    }

    public static <V,E> void transitiveClosure(Graph<V,E> g) {
        for (Vertex<V> k : g.vertices())
            for (Vertex<V> i : g.vertices())
                if (i != k && g.getEdge(i,k) != null)
                    for (Vertex<V> j : g.vertices())
                        if (i != j && j != k && g.getEdge(k,j) != null)
                            if (g.getEdge(i,j) == null)
                                g.insertEdge(i, j, null);
    }
}