package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class SimpleElementsCreater {
    public static HBox createVehicleBox() {
        HBox dataRow = new HBox();
        dataRow.setAlignment(Pos.CENTER_LEFT);
        dataRow.setSpacing(10);
        dataRow.setPadding(new Insets(5));

        TextField neTcField = new TextField();
        neTcField.setPromptText("Введите № TC");
        neTcField.setPrefWidth(80);
        neTcField.setStyle("-fx-font-family: 'Arial';");

        TextField routeField = new TextField();
        routeField.setPromptText("Маршрут");
        routeField.setPrefWidth(150);

        TextField timeField = new TextField();
        timeField.setPromptText("число");
        timeField.setPrefWidth(80);

        TextField speedField = new TextField();
        speedField.setPromptText("км/ч");
        speedField.setPrefWidth(80);

        TextField priceField = new TextField();
        priceField.setPromptText("руб");
        priceField.setPrefWidth(100);

        dataRow.getChildren().addAll(
                neTcField,
                routeField,
                timeField,
                speedField,
                priceField
        );
        return dataRow;
    }

    public static HBox createRouteBox(int num) {
        return new HBox() {{
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(10);

            Label numberLabel = new Label(String.format("%2d", num));
            numberLabel.setMinWidth(30);
            numberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");

            TextField textField = new TextField();
            textField.setPromptText("Введите маршрут...");
            textField.setStyle("-fx-pref-width: 200;");
            getChildren().addAll(numberLabel, textField);
        }};
    }
}
