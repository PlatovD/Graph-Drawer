package ru.vsu.cs.dplatov.vvp.task8.graphic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GraphicEdge extends Line {
    private int weight;
    private GraphicNode from;
    private GraphicNode to;

    public GraphicEdge(GraphicNode from, GraphicNode to) {
        this.from = from;
        this.to = to;
        setFill(Color.GRAY);
        toBack();
    }
}
