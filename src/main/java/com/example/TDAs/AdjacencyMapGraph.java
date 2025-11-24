package com.example.TDAs;

public class AdjacencyMapGraph<V, E> implements Graph<V, E> {

    @SuppressWarnings("hiding")
    private class InnerVertex<V> implements Vertex<V> {

        private V element;
        private Position<Vertex<V>> position;
        private Map<Vertex<V>, Edge<E>> outgoing, incoming;

        public InnerVertex(V element, boolean graphIsDirected) {
            this.element = element;
            outgoing = new UnsortedTableMap<>();
            if (graphIsDirected) {
                incoming = new UnsortedTableMap<>();
            } else {
                incoming = outgoing;
            }
        }

        @Override
        public V getElement() {
            return this.element;
        }

        public void setPosition(Position<Vertex<V>> position) {
            this.position = position;
        }

        public Position<Vertex<V>> getPosition() {
            return this.position;
        }

        public Map<Vertex<V>, Edge<E>> getOutgoing() {
            return this.outgoing;
        }

        public Map<Vertex<V>, Edge<E>> getIncoming() {
            return this.incoming;
        }

        public boolean validate(Graph<V, E> graph) {
            return AdjacencyMapGraph.this == graph && position != null;
        }
    }

    @SuppressWarnings("hiding")
    private class InnerEdge<E> implements Edge<E> {

        private E element;
        private Position<Edge<E>> position;
        private Vertex<V>[] endpoints;

        @SuppressWarnings("unchecked")
        public InnerEdge(Vertex<V> u, Vertex<V> v, E element) {
            this.element = element;
            endpoints = (Vertex<V>[]) new Vertex[]{u, v};
        }

        @Override
        public E getElement() {
            return this.element;
        }

        public Vertex<V>[] getEndPoints() {
            return this.endpoints;
        }

        public void setPosition(Position<Edge<E>> position) {
            this.position = position;
        }

        public Position<Edge<E>> getPosition() {
            return this.position;
        }

        public boolean validate(Graph<V, E> graph) {
            return AdjacencyMapGraph.this == graph && position != null;
        }
    }

    private boolean isDirected;
    private PositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
    private PositionalList<Edge<E>> edges = new LinkedPositionalList<>();

    public AdjacencyMapGraph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        return vertices;
    }

    @Override
    public Iterable<Edge<E>> edges() {
        return edges;
    }

    @Override
    public int outDegree(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(vertex);
        return vert.getOutgoing().size();
    }

    @Override
    public int inDegree(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(vertex);
        return vert.getIncoming().size();
    }

    @Override
    public Iterable<Edge<E>> outgoingEdges(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(vertex);
        return vert.getOutgoing().values();
    }

    @Override
    public Iterable<Edge<E>> incomingEdges(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(vertex);
        return vert.getIncoming().values();
    }

    @Override
    public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> origin = validate(u);
        InnerVertex<V> dest = validate(v);
        return origin.getOutgoing().get(dest);
    }

    @Override
    public Vertex<V>[] endVertices(Edge<E> edge) throws IllegalArgumentException {
        InnerEdge<E> e = validate(edge);
        return e.getEndPoints();
    }

    @Override
    public Vertex<V> opposite(Vertex<V> vertex, Edge<E> edge) throws IllegalArgumentException {
        validate(vertex);
        InnerEdge<E> e = validate(edge);
        Vertex<V>[] endpoints = e.getEndPoints();
        if (endpoints[0] == vertex) {
            return endpoints[1];
        } else if (endpoints[1] == vertex) {
            return endpoints[0];
        } else {
            throw new IllegalArgumentException("v is not incident to this edge");
        }
    }

    @Override
    public Vertex<V> insertVertex(V element) {
        InnerVertex<V> v = new InnerVertex<>(element, isDirected);
        Position<Vertex<V>> position = vertices.addLast(v);
        v.setPosition(position);
        return v;
    }

    @Override
    public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
        if (getEdge(u, v) == null) {
            InnerEdge<E> e = new InnerEdge<>(u, v, element);
            Position<Edge<E>> position = edges.addLast(e);
            e.setPosition(position);
            InnerVertex<V> origin = validate(u);
            InnerVertex<V> dest = validate(v);
            origin.getOutgoing().put(v, e);
            dest.getIncoming().put(u, e);
            return e;
        } else {
            throw new IllegalArgumentException("Edge from u to v exists");
        }
    }

    @Override
    public void removeVertex(Vertex<V> vertex) throws IllegalArgumentException {
        InnerVertex<V> v = validate(vertex);
        for (Edge<E> e : v.getOutgoing().values()) {
            removeEdge(e);
        }
        for (Edge<E> e : v.getIncoming().values()) {
            removeEdge(e);
        }
        vertices.remove(v.getPosition());
    }

    @Override
    public void removeEdge(Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        Vertex<V>[] endpoints = edge.getEndPoints();
        InnerVertex<V> origin = validate(endpoints[0]);
        InnerVertex<V> dest = validate(endpoints[1]);
        origin.getOutgoing().remove(dest);
        dest.getIncoming().remove(origin);
        edges.remove(edge.getPosition());
    }

    //@SuppressWarnings({"unchecked"})
    private InnerVertex<V> validate(Vertex<V> v) {
        if (!(v instanceof InnerVertex)) {
            throw new IllegalArgumentException("Invalid vertex");
        }
        InnerVertex<V> vert = (InnerVertex<V>) v;
        if (!vert.validate(this)) {
            throw new IllegalArgumentException("Invalid vertex");
        }
        return vert;
    }

    //@SuppressWarnings({"unchecked"})
    private InnerEdge<E> validate(Edge<E> e) {
        if (!(e instanceof InnerEdge)) {
            throw new IllegalArgumentException("Invalid edge");
        }
        InnerEdge<E> edge = (InnerEdge<E>) e;
        if (!edge.validate(this)) {
            throw new IllegalArgumentException("Invalid edge");
        }
        return edge;
    }
}