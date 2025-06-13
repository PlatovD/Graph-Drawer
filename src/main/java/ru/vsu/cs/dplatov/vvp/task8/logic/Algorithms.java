package ru.vsu.cs.dplatov.vvp.task8.logic;

import ru.vsu.cs.dplatov.vvp.task8.logic.utils.WGraph;

import java.util.*;

public class Algorithms {
    public static <T> List<T> findTheShortestPath(WGraph<T, Integer> graph, T from, T to) {
        Map<T, Integer> shortestPaths = new HashMap<>();
        shortestPaths.put(from, 0);

        Map<T, T> path = new HashMap<>();

        Map<T, Boolean> visited = new HashMap<>();
        PriorityQueue<T> priorityQueue = new PriorityQueue<>(Comparator.comparing(shortestPaths::get));

        priorityQueue.add(from);

        while (!priorityQueue.isEmpty()) {
            T node = priorityQueue.poll();
            if (node.equals(to)) return describePath(path, to);
            if (visited.getOrDefault(node, false)) continue;
            visited.put(node, true);

            for (WGraph.Edge<T, Integer> edge : graph.adjacentEdges(node)) {
                if (!visited.containsKey(edge.to())) {
                    if (shortestPaths.containsKey(edge.to()) && edge.weight() + shortestPaths.get(node) < shortestPaths.get(edge.to())) {
                        shortestPaths.put(edge.to(), edge.weight() + shortestPaths.get(node));
                        priorityQueue.add(edge.to());
                        path.put(edge.to(), node);
                    } else {
                        shortestPaths.put(edge.to(), edge.weight());
                        priorityQueue.add(edge.to());
                        path.put(edge.to(), node);
                    }
                }
            }
        }
        return null;
    }

    private static <T> List<T> describePath(Map<T, T> toFrom, T to) {
        if (!toFrom.containsKey(to)) {
            return null;
        }
        List<T> path = new LinkedList<>();
        T current = to;
        while (current != null) {
            path.add(0, current);
            current = toFrom.getOrDefault(current, null);
        }
        return path;
    }

//    public static void main(String[] args) {
//        WGraph<String, Integer> graph = new DefaultGraph<>();
//        graph.addNode("Dima");
//        graph.addNode("Vika");
//        graph.addNode("Alina");
//        graph.addNode("Matvey");
//
//        graph.addEdge("Dima", "Vika", 5);
//        graph.addEdge("Dima", "Matvey", 1);
//        graph.addEdge("Vika", "Matvey", 2);
//        graph.addEdge("Matvey", "Alina", 1);
//        graph.addEdge("Vika", "Alina", 10);
//
//        System.out.println(findTheShortestPath(graph, "Dima", "Alina"));
//    }
}
