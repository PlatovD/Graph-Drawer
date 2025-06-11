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
import ru.vsu.cs.dplatov.vvp.task8.graphic.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.logic.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.net.URL;
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
    private Button drawFromListNotationButton;

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
                storage.deleteNode(node);
                drawingPane.getChildren().remove(node);
            }
        }
    }

    @FXML
    private void connectNodes(ActionEvent event) {
        String firstNode = toConnect1.getText();
        String secondNode = toConnect2.getText();

        if (firstNode.isEmpty() | secondNode.isEmpty()) {
            return;
        }

        GraphicEdge edge = storage.createEdge(firstNode, secondNode);
        if (edge != null) {
            drawingPane.getChildren().addAll(edge, edge.getWeightField());
            edge.toBack();
        }
    }

    private void onChangeTab(ObservableValue<? extends Tab> obs, Tab lastTab, Tab newTab) {
        DefaultGraph<String, Integer> graph = new DefaultGraph<>();
        storage.parseToBack(graph);

        // обновление list нотации
        StringBuilder sb = new StringBuilder();
        for (String s : graph.allNodes()) {
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(s)) {
                sb.append(s).append(" ->[").append(edge.weight()).append("] ").append(edge.to()).append(";\n");
            }
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
}