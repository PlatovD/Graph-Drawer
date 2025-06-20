package ru.vsu.cs.dplatov.vvp.task8;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.PopupConnector;
import ru.vsu.cs.dplatov.vvp.task8.logic.TripleValue;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

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

    public GraphicEdge createEdge(String from, String to, int dist, int time, int price) {
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
        edge.getDistField().setText(String.valueOf(dist));
        edge.getTimeField().setText(String.valueOf(time));
        edge.getPriceField().setText(String.valueOf(price));
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
        Color accentColor = Color.web("#5C6BC0");
        if (activeObject instanceof Rectangle rect) {
            rect.setStroke(accentColor);
            rect.setStrokeWidth(2);
        } else if (activeObject instanceof GraphicNode node) {
            node.setActive();
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
                PopupConnector popup = new PopupConnector(node1, node2);
                popup.getButton().setOnAction(e -> {
                    if (!popup.getDistanceField().getText().isEmpty() &&
                            !popup.getTimeField().getText().isEmpty() &&
                            !popup.getPriceField().getText().isEmpty()) {

                        String params = String.join(";",
                                popup.getDistanceField().getText(),
                                popup.getTimeField().getText(),
                                popup.getPriceField().getText());

                        controller.connectNodesFromMouseMenu(node1.getText(), node2.getText(), params);
                    }
                    popup.hide();
                });
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

    public void parseToBack(WGraph<String, TripleValue<Integer>> graphToFill) {
        graphToFill.clear();
        for (GraphicNode node : nodes) {
            graphToFill.addNode(node.getText());
        }
        for (GraphicEdge edge : edges) {
            graphToFill.addEdge(edge.getFrom().getText(), edge.getTo().getText(), new TripleValue<>(edge.getDist(), edge.getTime(), edge.getPrice()));
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


    public void lightPath(List<String> values) {
        for (GraphicNode node : nodes) {
            if (values.contains(node.getText())) {
                node.light();
            }
        }
    }

    public void offLightPath() {
        for (GraphicNode node : nodes) {
            node.offLight();
        }
    }

    public static Controller getController() {
        return controller;
    }
}
