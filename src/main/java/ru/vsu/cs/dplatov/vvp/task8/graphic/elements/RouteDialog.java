package ru.vsu.cs.dplatov.vvp.task8.graphic.elements;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.vsu.cs.dplatov.vvp.task8.logic.BusRide;
import ru.vsu.cs.dplatov.vvp.task8.logic.LogicModel;

public class RouteDialog {
    public static String generateRouteDescription(LogicModel.TripInfo trip) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════╗\n");
        sb.append("║        ПЛАН МАРШРУТА               ║\n");
        sb.append("╚════════════════════════════════════╝\n\n");

        if (trip == null || trip.stations().isEmpty()) {
            return sb.append("Маршрут не содержит остановок").toString();
        }

        BusRide lastRide = null;
        int totalStops = trip.stations().size();

        for (int i = 0; i < totalStops; i++) {
            String station = trip.stations().get(i);
            BusRide currentRide = i < trip.rides().size() ? trip.rides().get(i) : null;

            if (i == totalStops - 1) {
                sb.append("╔════════════════════════════════════╗\n");
                sb.append(String.format("║  🏁 Конечная: %-25s ║\n", station));
                sb.append("╚════════════════════════════════════╝\n");
                break;
            }

            if (currentRide != null && !currentRide.equals(lastRide)) {
                sb.append(String.format("🚏 %d. %s\n", i + 1, station));

                if (lastRide == null) {
                    sb.append(String.format("   🚍 Транспорт: №%s\n", currentRide.getVehicleNum()));
                } else {
                    sb.append(String.format("   ♻ Пересадка на №%s\n", currentRide.getVehicleNum()));
                }

                sb.append(String.format("   Маршрут: %s → %s\n\n", currentRide.getRoute().getStations().get(0), currentRide.getRoute().getStations().get(currentRide.getRoute().getStations().size() - 1)));
            }
            else {
                sb.append(String.format("⏩ %d. Проезжаем %s\n\n", i + 1, station));
            }

            lastRide = currentRide;
        }

        return sb.toString();
    }

    public static void showRoute(LogicModel.TripInfo info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ваш маршрут");
        alert.setHeaderText("Детали поездки");

        TextArea textArea = new TextArea(generateRouteDescription(info));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");

        VBox.setVgrow(textArea, Priority.ALWAYS);
        VBox root = new VBox(textArea);
        root.setPrefSize(500, 300);

        alert.getDialogPane().setContent(root);
        alert.showAndWait();
    }

}
