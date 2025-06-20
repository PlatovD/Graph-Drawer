package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GraphicEdge extends Line {
    private GraphicNode from;
    private GraphicNode to;
    private TextField distField;
    private TextField timeField;
    private TextField priceField;
    private HBox weightField;

    public GraphicEdge(GraphicNode from, GraphicNode to) {
        this.from = from;
        this.to = to;
        setFill(Color.GRAY);
        distField = createCustomizedWeightField();
        timeField = createCustomizedWeightField();
        priceField = createCustomizedWeightField();
        weightField = new HBox(distField, timeField, priceField);
        weightField.layoutXProperty().bind(
                startXProperty()
                        .add(endXProperty())
                        .divide(2)
                        .subtract(weightField.widthProperty().divide(2))
        );

        weightField.layoutYProperty().bind(
                startYProperty()
                        .add(endYProperty())
                        .divide(2)
                        .subtract(weightField.heightProperty().divide(2))
        );
        toBack();
    }

    private TextField createCustomizedWeightField() {
        TextField weightField = new TextField();
        weightField.setPrefWidth(40);
        weightField.setAlignment(Pos.CENTER);
        weightField.setPrefWidth(40);
        weightField.setAlignment(Pos.CENTER);
        weightField.setEditable(true);
        weightField.setFocusTraversable(false);

        weightField.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 5px 10px 5px 10px;
                    -fx-border-color: transparent;
                    -fx-font-size: 12px;
                    -fx-text-fill: #5C6BC0;
                    -fx-padding: 2 6 2 6;
                    -fx-opacity: 1;
                """);
        return weightField;
    }

    public enum PointPosition {
        START, END
    }

    public int getDist() {
        int weight = 0;
        try {
            weight = Integer.parseInt(distField.getText());
        } catch (NumberFormatException e) {
            System.err.println("Can't parse node weight. Reset to default (1)");
        }
        return weight;
    }

    public int getTime() {
        int weight = 0;
        try {
            weight = Integer.parseInt(timeField.getText());
        } catch (NumberFormatException e) {
            System.err.println("Can't parse node weight. Reset to default (1)");
        }
        return weight;
    }

    public int getPrice() {
        int weight = 0;
        try {
            weight = Integer.parseInt(priceField.getText());
        } catch (NumberFormatException e) {
            System.err.println("Can't parse node weight. Reset to default (1)");
        }
        return weight;
    }

    public GraphicNode getFrom() {
        return from;
    }

    public void setFrom(GraphicNode from) {
        this.from = from;
    }

    public GraphicNode getTo() {
        return to;
    }

    public void setTo(GraphicNode to) {
        this.to = to;
    }

    public PointPosition getPointPosition(GraphicNode node) {
        if (from.equals(node)) {
            return PointPosition.START;
        } else {
            return PointPosition.END;
        }
    }

    public HBox getWeightField() {
        return weightField;
    }

    public TextField getDistField() {
        return distField;
    }

    public TextField getTimeField() {
        return timeField;
    }

    public TextField getPriceField() {
        return priceField;
    }
}
