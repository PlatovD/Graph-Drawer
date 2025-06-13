package ru.vsu.cs.dplatov.vvp.task8.logic;


import java.util.*;

public class LogicModel {
    private static LogicModel instance;
    private final Map<Integer, Route> routes = new HashMap<>();
    private final List<BusRide> rides = new ArrayList<>();

    public static LogicModel getInstance() {
        if (instance == null) {
            instance = new LogicModel();
        }
        return instance;
    }

    public void addRoute(int num, Route route) {
        routes.put(num, route);
    }

    public Route getRoute(int num) {
        return routes.getOrDefault(num, null);
    }

    public void addRide(BusRide ride) {
        rides.add(ride);
    }

    public void clear() {
        routes.clear();
        rides.clear();
    }

    public String calcShortestPath() {
        return "";
    }
}
