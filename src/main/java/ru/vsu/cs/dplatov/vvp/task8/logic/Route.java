package ru.vsu.cs.dplatov.vvp.task8.logic;

import java.util.List;

public class Route {
    private final List<String> stations;

    public Route(List<String> route) {
        stations = route;
    }

    public List<String> getStations() {
        return stations;
    }
}
