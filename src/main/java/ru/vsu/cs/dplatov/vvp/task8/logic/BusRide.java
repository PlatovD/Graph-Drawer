package ru.vsu.cs.dplatov.vvp.task8.logic;

import ru.vsu.cs.dplatov.vvp.task8.logic.utils.WGraph;

import java.util.HashMap;
import java.util.Map;

public class BusRide {
    private WGraph<String, Integer> graph;
    private Route route;
    private String vehicleNum;
    private int timeStart;
    private int speed;
    private int price;

    Map<String, Integer> stationTimings;
    Map<String, Integer> stationPrices;


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

        int time = timeStart;
        int drivePrice = 0;

        main:
        for (int i = 0; i < route.getStations().size() - 1; i++) {
            stationTimings.put(route.getStations().get(i), time);
            stationPrices.put(route.getStations().get(i), drivePrice);
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(route.getStations().get(i))) {
                if (edge.to().equals(route.getStations().get(i + 1))) {
                    time += edge.weight() / speed;
                    drivePrice += price * edge.weight();
                    continue main;
                }
            }
        }
        for (Map.Entry<String, Integer> entry : stationTimings.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
        for (Map.Entry<String, Integer> entry : stationPrices.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

    }

}
