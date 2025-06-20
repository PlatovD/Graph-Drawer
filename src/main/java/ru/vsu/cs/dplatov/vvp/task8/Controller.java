package ru.vsu.cs.dplatov.vvp.task8;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.graphic.utils.ForceDirectionLayoutCalculator;
import ru.vsu.cs.dplatov.vvp.task8.logic.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.TripleValue;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;
import ru.vsu.cs.dplatov.vvp.task8.graphic.utils.DataGetter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Pane drawingPane;

    @FXML
    private TabPane leftMenuPane;

    private final Model storage = Model.getInstance();

    //List
    @FXML
    private TextArea stringNotationArea;

    @FXML
    private Button drawFromNotationButton;

    //Graphic
    @FXML
    private Rectangle exampleNode;

    @FXML
    private Button connectBtn;

    @FXML
    private TextField toConnect1;

    @FXML
    private TextField toConnect2;

    @FXML
    private TextField weightField;

    @FXML
    private ChoiceBox<String> fromChoice;

    @FXML
    private ChoiceBox<String> toChoice;

    @FXML
    private Button findShortestButton;

    @FXML
    private Button findFastestButton;

    @FXML
    private Button findCheapestButton;

    @FXML
    private TextField distInfoField;

    @FXML
    private TextField timeInfoField;

    @FXML
    private TextField priceInfoField;

    private WGraph<String, TripleValue<Integer>> graph;

    @FXML
    private void handleChoose(MouseEvent e) {
        Node lastActive = storage.getActiveObject();
        if (storage.getActiveObject() != null) {
            storage.deactivateObj();
        }
        if (e.getSource().equals(lastActive) && lastActive instanceof GraphicNode node && !node.isWasDragged()) {
            return;
        }
        storage.setActiveObject((Node) e.getSource());
    }

    @FXML
    private void handleDrawingPaneClick(MouseEvent e) {
        if (storage.getActiveObject() == null) {
            return;
        }
        if (storage.getActiveObject().equals(exampleNode)) {
            Node node = storage.createNode(e.getX(), e.getY());
            drawingPane.getChildren().add(node);
        }
    }

    @FXML
    private void removeNode(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            if (storage.getActiveObject() instanceof GraphicNode node) {
                List<GraphicEdge> edgesToRemove = new ArrayList<>(node.getConnectedEdges());

                for (GraphicEdge edge : edgesToRemove) {
                    drawingPane.getChildren().remove(edge);
                    drawingPane.getChildren().remove(edge.getDistField());
                    drawingPane.getChildren().remove(edge.getTimeField());
                    drawingPane.getChildren().remove(edge.getPriceField());
                }

                storage.deleteNode(node);
                drawingPane.getChildren().remove(node);
            }
        }
    }

    public void connectNodesFromMouseMenu(String firstNode, String secondNode, String weight) {
        toConnect1.setText(firstNode);
        toConnect2.setText(secondNode);
        weightField.setText(weight);
        connectNodes(null);
    }

    @FXML
    private void connectNodes(ActionEvent event) {
        String firstNode = toConnect1.getText();
        String secondNode = toConnect2.getText();
        if (Objects.equals(firstNode, secondNode)) return;
        int dist = 1;
        int time = 1;
        int price = 1;

        try {
            String[] params = weightField.getText().split(";");
            dist = Integer.parseInt(params[0]);
            time = Integer.parseInt(params[1]);
            price = Integer.parseInt(params[2]);
        } catch (NumberFormatException e) {
            System.err.println("Wrong weight of the edge");
        }

        if (firstNode.isEmpty() | secondNode.isEmpty()) {
            return;
        }

        GraphicEdge edge = storage.createEdge(firstNode, secondNode, dist, time, price);
        if (edge != null) {
            drawingPane.getChildren().addAll(edge, edge.getWeightField());
            edge.toBack();
        }
    }

    private void updateChoiceFields() {
        fromChoice.getItems().clear();
        toChoice.getItems().clear();
        for (GraphicNode node : storage.getNodes()) {
            fromChoice.getItems().add(node.getText());
            toChoice.getItems().add(node.getText());
        }
    }

    private void onChangeTab(ObservableValue<? extends Tab> obs, Tab lastTab, Tab newTab) {
        updateChoiceFields();
        storage.offLightPath();
        graph = new DefaultGraph<>();
        storage.parseToBack(graph);

        // обновление list нотации
        StringBuilder sb = new StringBuilder();
        for (String s : graph.allNodes()) {
            int cnt = 0;
            for (WGraph.Edge<String, TripleValue<Integer>> edge : graph.adjacentEdges(s)) {
                sb.append(s).append(" ->[").append(edge.weight()).append("] ").append(edge.to()).append(";\n");
                cnt++;
            }
            if (cnt == 0) sb.append(s).append(";\n");
        }
        if (sb.isEmpty()) return;
        stringNotationArea.setText(sb.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftMenuPane.getSelectionModel().selectedItemProperty().addListener((obs, last, current) -> {
            if (!last.equals(current)) storage.deactivateObj();
            onChangeTab(obs, last, current);
        });
    }

    @FXML
    private void drawFromNotationHandler() {
        if (!leftMenuPane.getSelectionModel().isSelected(0))
            onChangeTab(null, null, null);
        graph = DataGetter.getDataFromStringNotationArea(stringNotationArea);
        drawingPane.getChildren().clear();
        storage.clear();
        ForceDirectionLayoutCalculator.calculateForceDirectedCoordinates(graph, storage);
        for (GraphicEdge edge : storage.getEdges()) {
            drawingPane.getChildren().add(edge);
            drawingPane.getChildren().add(edge.getWeightField());
        }
        for (GraphicNode node : storage.getNodes()) {
            drawingPane.getChildren().add(node);
            node.updateEdgesPositions();
        }
    }

    public Pane getDrawingPane() {
        return drawingPane;
    }

    @FXML
    private void calcShortest(ActionEvent event) {
        storage.offLightPath();
        String from = fromChoice.getValue();
        String to = toChoice.getValue();
        graph = new DefaultGraph<>();
        storage.parseToBack(graph);
        for (WGraph.Edge<String, TripleValue<Integer>> edge : graph.adjacentEdges("Node1")) {
            System.out.println(edge.to());
            System.out.println(edge.weight());
            System.out.println();

        }
    }

    @FXML
    private void calcFastest(ActionEvent event) {
        storage.offLightPath();
    }

    @FXML
    private void calcCheapest(ActionEvent event) {
        storage.offLightPath();
    }
}