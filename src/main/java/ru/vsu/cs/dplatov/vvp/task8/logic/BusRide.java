package ru.vsu.cs.dplatov.vvp.task8.logic;

import ru.vsu.cs.dplatov.vvp.task8.logic.utils.WGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BusRide {
    private WGraph<String, Integer> graph;
    private Route route;
    private String vehicleNum;
    private int timeStart;
    private int speed;
    private int price;

    Map<String, Integer> stationTimings;
    Map<String, Integer> stationPrices;
    Map<String, Integer> stationPathLength;


    public BusRide(WGraph<String, Integer> graph, Route route, String vehicleNum, int timeStart, int speed, int price) {
        this.graph = graph;
        this.route = route;
        this.vehicleNum = vehicleNum;
        this.timeStart = timeStart;
        this.speed = speed;
        this.price = price;
        calcStationTimings();
    }

    private void calcStationTimings() {
        stationTimings = new HashMap<>();
        stationPrices = new HashMap<>();
        stationPathLength = new HashMap<>();

        int time = timeStart;
        int drivePrice = 0;
        int length = 0;

        main:
        for (int i = 0; i < route.getStations().size() - 1; i++) {
            stationTimings.put(route.getStations().get(i), time);
            stationPrices.put(route.getStations().get(i), drivePrice);
            stationPathLength.put(route.getStations().get(i), length);
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(route.getStations().get(i))) {
                if (edge.to().equals(route.getStations().get(i + 1))) {
                    if (length != Integer.MAX_VALUE)
                        length += edge.weight();
                    if (time != Integer.MAX_VALUE)
                        time += edge.weight() / speed;
                    if (drivePrice != Integer.MAX_VALUE)
                        drivePrice += price * edge.weight();
                    continue main;
                }
            }
            time = Integer.MAX_VALUE;
            drivePrice = Integer.MAX_VALUE;
            length = Integer.MAX_VALUE;
        }
//        System.out.println("Номер авто " + vehicleNum);
        stationTimings.put(route.getStations().get(route.getStations().size() - 1), time);
        stationPrices.put(route.getStations().get(route.getStations().size() - 1), drivePrice);
        stationPathLength.put(route.getStations().get(route.getStations().size() - 1), length);
//        for (Map.Entry<String, Integer> entry : stationTimings.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//        System.out.println();
//        for (Map.Entry<String, Integer> entry : stationPrices.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//        System.out.println();
    }

    public String getNextStation(String s) {
        boolean wasReq = false;
        for (String station : route.getStations()) {
            if (wasReq) return station;
            if (Objects.equals(s, station)) wasReq = true;
        }
        return null;
    }

    public boolean containsStation(String s) {
        return stationTimings.containsKey(s);
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPrice() {
        return price;
    }

    public Map<String, Integer> getStationTimings() {
        return stationTimings;
    }

    public Map<String, Integer> getStationPrices() {
        return stationPrices;
    }

    public Map<String, Integer> getStationPathLength() {
        return stationPathLength;
    }

    public Route getRoute() {
        return route;
    }
}
