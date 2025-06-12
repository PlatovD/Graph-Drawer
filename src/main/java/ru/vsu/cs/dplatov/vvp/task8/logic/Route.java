package ru.vsu.cs.dplatov.vvp.task8.logic;

public class Route {
    String startStation;
    String endStation;
    int vehicleNum;
    int timeStart;
    int timeStop;

    public Route(String startStation, String endStation, int vehicleNum, int timeStart, int timeStop) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.vehicleNum = vehicleNum;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }
}
