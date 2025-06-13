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

    public Map<String, BusRide> calcShortestPath(String start, String stop, int currentTime) {
        String currentStation = start;
        BusRide currentRide = null;
        Map<String, BusRide> path = new HashMap<>();

        while (!Objects.equals(currentStation, stop)) {
            for (BusRide busRide : rides) {
                if ((currentRide == null && busRide.containsStation(currentStation) && busRide.containsStation(stop)) || (busRide.containsStation(currentStation) && busRide.containsStation(stop) && currentRide != null) && (busRide.stationPathLength.get(stop) < currentRide.stationPathLength.get(stop) && currentTime <= busRide.stationTimings.getOrDefault(currentStation, -1))) {
                    currentRide = busRide;
                }
            }
            if (currentRide == null || currentRide.stationPathLength.getOrDefault(currentStation, Integer.MAX_VALUE).equals(Integer.MAX_VALUE))
                return null;
            path.put(currentStation, currentRide);
            currentStation = currentRide.getNextStation(currentStation);
            currentTime = currentRide.stationTimings.get(currentStation);
        }

        path.put(currentStation, currentRide);

        return path;
    }
}
