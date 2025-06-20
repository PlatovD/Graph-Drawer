package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class PopupConnector extends Popup {
    private Button button;
    private TextField distanceField;
    private TextField timeField;
    private TextField priceField;

    public PopupConnector(GraphicNode node1, GraphicNode node2) {
        createPopupConnector(node1, node2);
    }

    private void createPopupConnector(GraphicNode node1, GraphicNode node2) {
        setAutoHide(true);

        Label titleLabel = new Label("Параметры ребра:");
        titleLabel.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 0 0 10 0;
            -fx-text-fill: #5C6BC0;
        """);

        distanceField = createParameterField("Дистанция", "100");
        timeField = createParameterField("Время", "60");
        priceField = createParameterField("Цена", "500");

        button = new Button("Соединить");
        button.setStyle("""
            -fx-font-size: 14px;
            -fx-background-color: #5C6BC0;
            -fx-text-fill: white;
            -fx-padding: 8px 20px;
            -fx-background-radius: 4px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0,0,1);
        """);

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.addRow(0, createFieldLabel("Дистанция:"), distanceField);
        grid.addRow(1, createFieldLabel("Время:"), timeField);
        grid.addRow(2, createFieldLabel("Цена:"), priceField);

        VBox vBox = new VBox(12, titleLabel, grid, button);
        vBox.setStyle("""
            -fx-background-color: #FFFFFF;
            -fx-padding: 20px;
            -fx-border-color: #5C6BC0;
            -fx-border-width: 1px;
            -fx-border-radius: 6px;
            -fx-background-radius: 6px;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
        """);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        getContent().add(vBox);

        EventHandler<ActionEvent> handleEnter = e -> button.fire();
        distanceField.setOnAction(handleEnter);
        timeField.setOnAction(handleEnter);
        priceField.setOnAction(handleEnter);
    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #5C6BC0; -fx-font-size: 14px;");
        return label;
    }

    private static TextField createParameterField(String prompt, String defaultValue) {
        TextField field = new TextField(defaultValue);
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 6px;
            -fx-pref-width: 120px;
            -fx-background-radius: 4px;
            -fx-border-radius: 4px;
            -fx-border-color: #5C6BC0;
            -fx-border-width: 1px;
            -fx-text-fill: #5C6BC0;
        """);
        return field;
    }

    public Button getButton() {
        return button;
    }

    public TextField getDistanceField() {
        return distanceField;
    }

    public TextField getTimeField() {
        return timeField;
    }

    public TextField getPriceField() {
        return priceField;
    }
}