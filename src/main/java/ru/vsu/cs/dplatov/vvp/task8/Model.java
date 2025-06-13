package ru.vsu.cs.dplatov.vvp.task8;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.logic.utils.WGraph;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private Node activeObject;
    private static Model instance;
    private static Controller controller;
    private List<GraphicNode> nodes = new ArrayList<>();
    private List<GraphicEdge> edges = new ArrayList<>();

    public Node getActiveObject() {
        return activeObject;
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public List<GraphicNode> getNodes() {
        return nodes;
    }

    public GraphicNode createNode(double x, double y, String value) {
        GraphicNode node = new GraphicNode(value);
        node.setLayoutX(x - node.getWidth() / 2);
        node.setLayoutY(y - node.getWidth() / 2);
        node.calcPoint2D();
        nodes.add(node);
        setNodeHandlers(node);
        return node;
    }

    public GraphicNode createNode(double x, double y) {
        return createNode(x, y, "Node");
    }

    private void setNodeHandlers(GraphicNode node) {
        node.setOnMousePressed(event -> {
            if (event.isShiftDown()) return;
            Object lastActive = getActiveObject();
            deactivateObj();
            if (!event.getSource().equals(lastActive)) setActiveObject(node);
            dragStartHandler(event, node);
        });
        node.setOnMouseDragged(event -> {
            if (getActiveObject() == null || !getActiveObject().equals(node)) {
                deactivateObj();
                setActiveObject(node);
            }
            dragHandler(event, node);
        });
        node.setOnMouseReleased(this::dragStopHandler);
    }

    public GraphicEdge createEdge(String from, String to, int weight) {
        GraphicNode first = findNode(from);
        GraphicNode second = findNode(to);
        if (first == null || second == null) return null;
        for (GraphicEdge graphicEdge : first.getConnectedEdges()) {
            if (graphicEdge.getTo().equals(second) || graphicEdge.getFrom().equals(second)) {
                return null;
            }
        }
        GraphicEdge edge = new GraphicEdge(first, second);
        edges.add(edge);
        first.addConnectedEdges(edge);
        second.addConnectedEdges(edge);
        edge.setStartX(first.getCenterX());
        edge.setStartY(first.getCenterY());
        edge.setEndX(second.getCenterX());
        edge.setEndY(second.getCenterY());
        edge.getWeightField().setText(String.valueOf(weight));
        return edge;
    }

    private GraphicNode findNode(String val) {
        for (GraphicNode node : nodes) {
            if (node.getText().equals(val)) return node;
        }
        return null;
    }

    public List<GraphicEdge> getEdges() {
        return edges;
    }

    private Model() {
    }

    public void setActiveObject(Node activeObject) {
        if (activeObject instanceof Rectangle rect) {
            rect.setStroke(Color.GREEN);
            rect.setStrokeWidth(2);
        } else if (activeObject instanceof GraphicNode node) {
            BorderStroke borderStroke = new BorderStroke(
                    Color.GREEN,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(0),
                    new BorderWidths(2)
            );

            node.setBorder(new Border(borderStroke));
        }
        this.activeObject = activeObject;
    }

    public void deactivateObj() {
        if (activeObject == null) return;
        if (activeObject instanceof Rectangle rect) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
        } else if (activeObject instanceof GraphicNode node) {
            BorderStroke borderStroke = new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(0),
                    new BorderWidths(1)
            );
            node.setBorder(new Border(borderStroke));
        }
        activeObject = null;
    }

    private void dragHandler(MouseEvent event, GraphicNode node) {
        node.setWasDragged(true);
        Point2D mouseInParent = node.localToParent(event.getX(), event.getY());
        node.setLayoutX(mouseInParent.getX() - node.getClickX());
        node.setLayoutY(mouseInParent.getY() - node.getClickY());
        node.updateEdgesPositions();
    }

    private void dragStopHandler(MouseEvent event) {
        if (event.isShiftDown()) {
            if (activeObject != null && activeObject instanceof GraphicNode node1 && !event.getSource().equals(node1) && event.getSource() instanceof GraphicNode node2) {
                Popup popup = createPopupConnector(node1, node2);
                popup.show(controller.getDrawingPane(), controller.getDrawingPane().getWidth() / 2 - popup.getWidth() / 2, controller.getDrawingPane().getWidth() / 2 - popup.getHeight() / 2);
            }
        }
        if (event.getSource() instanceof GraphicNode node && node.isWasDragged()) {
            node.setWasDragged(false);
        }
    }

    private void dragStartHandler(MouseEvent event, GraphicNode node) {
        node.setClickX(event.getX());
        node.setClickY(event.getY());
    }

    public void deleteNode(GraphicNode node) {
        for (GraphicEdge edge : node.getConnectedEdges()) {
            edges.remove(edge);
            if (!edge.getPointPosition(node).equals(GraphicEdge.PointPosition.START))
                edge.getFrom().getConnectedEdges().remove(edge);
            if (!edge.getPointPosition(node).equals(GraphicEdge.PointPosition.END))
                edge.getTo().getConnectedEdges().remove(edge);
        }

        nodes.remove(node);
        activeObject = null;
    }

    public void parseToBack(WGraph<String, Integer> graphToFill) {
        graphToFill.clear();
        for (GraphicNode node : nodes) {
            graphToFill.addNode(node.getText());
        }
        for (GraphicEdge edge : edges) {
            graphToFill.addEdge(edge.getFrom().getText(), edge.getTo().getText(), edge.getWeight());
        }
    }

    public void clear() {
        // перед вызовом надо еще Pane очищать
        deactivateObj();
        nodes.clear();
        edges.clear();
    }

    public static void setController(Controller controller) {
        Model.controller = controller;
    }

    private static Popup createPopupConnector(GraphicNode node1, GraphicNode node2) {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        Label label = new Label("Введите вес ребра:");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 0 0 5 0;");

        TextField textField = new TextField();
        textField.setPromptText("Введите число");
        textField.setStyle("""
                    -fx-font-size: 14px;
                    -fx-padding: 5px;
                    -fx-background-radius: 3px;
                    -fx-border-radius: 3px;
                    -fx-border-color: #ccc;
                    -fx-border-width: 1px;
                """);

        Button button = new Button("Построить ребро");
        button.setStyle("""
                    -fx-font-size: 14px;
                    -fx-background-color: #006400;
                    -fx-text-fill: white;
                    -fx-padding: 5px 15px;
                    -fx-background-radius: 3px;
                    -fx-cursor: hand;
                """);
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #3a6bdc;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-background-color: #4a7bec;"));

        VBox vBox = new VBox(10, label, textField, button);
        vBox.setStyle("""
                    -fx-background-color: white;
                    -fx-padding: 15px;
                    -fx-border-color: #ddd;
                    -fx-border-width: 1px;
                    -fx-border-radius: 5px;
                    -fx-background-radius: 5px;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
                """);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(15));

        popup.getContent().add(vBox);


        button.setOnAction(e -> {
            if (!textField.getText().isEmpty()) {
                controller.connectNodesFromMouseMenu(node1.getText(), node2.getText(), textField.getText());
            }
            popup.hide();
        });

        textField.setOnAction(e -> button.fire());

        return popup;
    }

    public static Controller getController() {
        return controller;
    }
}
