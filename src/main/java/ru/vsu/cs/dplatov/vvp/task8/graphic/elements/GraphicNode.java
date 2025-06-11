package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GraphicNode extends HBox {
    private final TextField textField = new TextField("Node");
    private final List<GraphicEdge> connectedEdges = new ArrayList<>();

    private double clickX = -1;
    private double clickY = -1;
    private boolean wasDragged = false;

    public GraphicNode() {
        reset();
        getChildren().add(textField);
    }

    public void reset() {
        textField.setFont(Font.font("Arial", 12));
        textField.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        textField.setMaxWidth(60);
        textField.setAlignment(Pos.CENTER);

        setStyle("-fx-background-color: #f8f8f8; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1;");
        setPrefSize(70, 70);
        setAlignment(Pos.CENTER);
        toFront();
    }

    public void updateEdgesPositions() {
        for (GraphicEdge edge : connectedEdges) {
            switch (edge.getPointPosition(this)) {
                case START -> {
                    edge.setStartX(getCenterX());
                    edge.setStartY(getCenterY());
                }
                case END -> {
                    edge.setEndX(getCenterX());
                    edge.setEndY(getCenterY());
                }
            }
        }
    }

    public double getCenterX() {
        return getLayoutX() + getWidth() / 2;
    }

    public double getCenterY() {
        return getLayoutY() + getHeight() / 2;
    }

    public String getText() {
        return textField.getText();
    }

    public double getClickX() {
        return clickX;
    }

    public void setClickX(double clickX) {
        this.clickX = clickX;
    }

    public double getClickY() {
        return clickY;
    }

    public void setClickY(double clickY) {
        this.clickY = clickY;
    }

    public boolean isWasDragged() {
        return wasDragged;
    }

    public void setWasDragged(boolean wasDragged) {
        this.wasDragged = wasDragged;
    }

    public void addConnectedEdges(GraphicEdge edge) {
        connectedEdges.add(edge);
    }
}
