package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GraphicEdge extends Line {
    private GraphicNode from;
    private GraphicNode to;
    private TextField weightField;

    public GraphicEdge(GraphicNode from, GraphicNode to) {
        this.from = from;
        this.to = to;
        setFill(Color.GRAY);
        createCustomizedWeightField();
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

    private void createCustomizedWeightField() {
        weightField = new TextField("1");
        weightField.setPrefWidth(50);
        weightField.setAlignment(Pos.CENTER);
        weightField.setPrefWidth(50);
        weightField.setAlignment(Pos.CENTER);
        weightField.setEditable(true);
        weightField.setFocusTraversable(false);

        weightField.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 5px 10px 5px 10px;
                    -fx-border-color: transparent;
                    -fx-font-size: 15px;
                    -fx-text-fill: #006400;
                    -fx-padding: 2 6 2 6;
                    -fx-opacity: 1;
                """);
    }

    public enum PointPosition {
        START, END
    }

    public int getWeight() {
        int weight = 0;
        try {
            weight = Integer.parseInt(weightField.getText());
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

    public TextField getWeightField() {
        return weightField;
    }
}
