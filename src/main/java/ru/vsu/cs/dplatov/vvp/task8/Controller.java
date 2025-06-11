package ru.vsu.cs.dplatov.vvp.task8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.vsu.cs.dplatov.vvp.task8.graphic.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.graphic.GraphicStorage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    @FXML
    private Pane drawingPane;

    private final GraphicStorage storage = GraphicStorage.getInstance();

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
            drawingPane.getChildren().add(edge);
            edge.toBack();
        }
    }
}