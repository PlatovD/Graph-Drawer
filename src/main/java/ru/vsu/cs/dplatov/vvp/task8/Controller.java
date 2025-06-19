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
                    drawingPane.getChildren().remove(edge.getWeightField());
                }

                storage.deleteNode(node);
                drawingPane.getChildren().remove(node);
            }
        }
    }

    public void connectNodesFromMouseMenu(String firstNode, String secondNode, String weight) {
        if (Objects.equals(firstNode, secondNode)) return;
        toConnect1.setText(firstNode);
        toConnect2.setText(secondNode);
        weightField.setText(weight);
        connectNodes(null);
    }

    @FXML
    private void connectNodes(ActionEvent event) {
        String firstNode = toConnect1.getText();
        String secondNode = toConnect2.getText();
        int weight = 1;
        try {
            weight = Integer.parseInt(weightField.getText());
        } catch (NumberFormatException e) {
            System.err.println("Wrong weight of the edge");
        }

        if (firstNode.isEmpty() | secondNode.isEmpty()) {
            return;
        }

        GraphicEdge edge = storage.createEdge(firstNode, secondNode, weight);
        if (edge != null) {
            drawingPane.getChildren().addAll(edge, edge.getWeightField());
            edge.toBack();
        }
    }

    private void onChangeTab(ObservableValue<? extends Tab> obs, Tab lastTab, Tab newTab) {
        if (leftMenuPane.getSelectionModel().isSelected(1)) return;
        WGraph<String, Integer> graph = new DefaultGraph<>();
        storage.parseToBack(graph);

        // обновление list нотации
        StringBuilder sb = new StringBuilder();
        for (String s : graph.allNodes()) {
            int cnt = 0;
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(s)) {
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
        onChangeTab(null, null, null);
        WGraph<String, Integer> graph = DataGetter.getDataFromStringNotationArea(stringNotationArea);
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
}