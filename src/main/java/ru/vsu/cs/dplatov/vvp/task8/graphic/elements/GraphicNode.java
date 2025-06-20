package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GraphicNode extends HBox {
    private final static Color accentColor = Color.web("#5C6BC0");
    private final TextField textField = new TextField("Node");
    private final List<GraphicEdge> connectedEdges = new ArrayList<>();
    private Point2D point2D;

    private double clickX = -1;
    private double clickY = -1;
    private boolean wasDragged = false;

    public GraphicNode(String value) {
        reset();
        getChildren().add(textField);
        textField.setText(value);
    }

    public void calcPoint2D() {
        point2D = new Point2D(getCenterX(), getCenterY());
    }

    public GraphicNode() {
        new GraphicNode("Node");
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
            edge.toBack();
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

    public void setActive() {
        BorderStroke borderStroke = new BorderStroke(
                accentColor,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(2)
        );
        setBorder(new Border(borderStroke));
    }

    public void light() {
        setStyle("-fx-background-color: #f8f8f8; " +
                "-fx-border-color: red; " +
                "-fx-border-width: 1;");
    }

    public void offLight() {
        setStyle("-fx-background-color: #f8f8f8; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1;");
    }

    public double getCenterX() {
        return getLayoutX() + getPrefWidth() / 2;
    }

    public double getCenterY() {
        return getLayoutY() + getPrefHeight() / 2;
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

    public List<GraphicEdge> getConnectedEdges() {
        return connectedEdges;
    }

    public List<GraphicNode> getConnectedNodes() {
        List<GraphicNode> connected = new ArrayList<>();
        for (GraphicEdge edge : connectedEdges) {
            if (!edge.getFrom().equals(this)) {
                connected.add(edge.getFrom());
            } else {
                connected.add(edge.getTo());
            }
        }
        return connected;
    }

    public Point2D getPosition() {
        return point2D;
    }

    public void setPosition(Point2D point2D) {
        setLayoutX(point2D.getX() - getWidth() / 2);
        setLayoutY(point2D.getY() - getHeight() / 2);
        this.point2D = point2D;
    }
}

