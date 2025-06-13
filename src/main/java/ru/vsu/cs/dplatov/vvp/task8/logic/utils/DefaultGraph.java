package ru.vsu.cs.dplatov.vvp.task8.logic.utils;

import java.util.*;

public class DefaultGraph<T, N extends Number> implements WGraph<T, N> {
    class DefaultEdge implements Edge<T, N> {
        private final T from;
        private final T to;
        private final N weight;

        DefaultEdge(T from, T to, N weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public T from() {
            return from;
        }

        @Override
        public T to() {
            return to;
        }

        @Override
        public N weight() {
            return weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge<?, ?> other)) {
                return false;
            }
            return from.equals(other.from()) && to.equals(other.to()) ||
                    from.equals(other.to()) && to.equals(other.from());
        }

        @Override
        public int hashCode() {
            return to.hashCode() + from.hashCode();
        }

        @Override
        public String toString() {
            return from + " ->[" + weight + "] " + to;
        }
    }

    private int edgesCnt = 0;
    private final Map<T, List<Edge<T, N>>> chainedLists = new HashMap<>();

    @Override
    public int nodesCnt() {
        return chainedLists.size();
    }

    @Override
    public int edgesCnt() {
        return edgesCnt;
    }

    @Override
    public void addEdge(T v1, T v2, N weight) {
        if (!containsNode(v1) || !containsNode(v2)) {
            return;
        }
        Edge<T, N> fromTo = getEdge(v1, v2);
        if (fromTo != null) {
            removeEdge(v1, v2);
            addEdge(v1, v2, weight);
        } else {
            chainedLists.get(v1).add(new DefaultEdge(v1, v2, weight));
            chainedLists.get(v2).add(new DefaultEdge(v2, v1, weight));
            edgesCnt++;
        }
    }

    private Edge<T, N> getEdge(T v1, T v2) {
        if (!chainedLists.containsKey(v1) || !chainedLists.containsKey(v2)) {
            return null;
        }
        for (Edge<T, N> edge : adjacentEdges(v1)) {
            if (edge.to().equals(v2)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public void addNode(T value) {
        if (chainedLists.containsKey(value)) {
            return;
        }
        chainedLists.put(value, new ArrayList<>());
    }

    @Override
    public void clear() {
        chainedLists.clear();
    }

    @Override
    public boolean containsNode(T value) {
        return chainedLists.containsKey(value);
    }

    @Override
    public boolean containsEdge(T from, T to) {
        return getEdge(from, to) != null;
    }

    @Override
    public boolean removeEdge(T v1, T v2) {
        Edge<T, N> edge = getEdge(v1, v2);
        if (edge == null) {
            return false;
        }
        chainedLists.get(v1).remove(edge);
        chainedLists.get(v2).remove(edge);
        edgesCnt--;
        return true;
    }

    public boolean removeNode(T node) {
        if (!containsNode(node)) {
            return false;
        }
        for (T other : adjacentNodes(node)) {
            removeEdge(node, other);
        }
        chainedLists.remove(node);
        return true;
    }

    @Override
    public Iterable<T> adjacentNodes(T v) {
        if (!chainedLists.containsKey(v) || chainedLists.get(v).isEmpty()) {
            return new ArrayList<>();
        }
        return new Iterable<>() {
            int index = 0;

            @Override
            public Iterator<T> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return index < chainedLists.get(v).size();
                    }

                    @Override
                    public T next() {
                        return chainedLists.get(v).get(index++).to();
                    }
                };
            }
        };
    }

    @Override
    public Iterable<Edge<T, N>> adjacentEdges(T v) {
        if (!chainedLists.containsKey(v)) {
            return new ArrayList<>();
        }
        return () -> chainedLists.get(v).iterator();
    }

    @Override
    public Iterable<T> allNodes() {
        return chainedLists.keySet();
    }
}
