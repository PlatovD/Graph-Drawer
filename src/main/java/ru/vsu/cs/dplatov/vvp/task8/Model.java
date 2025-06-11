package ru.vsu.cs.dplatov.vvp.task8;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicEdge;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.logic.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private Node activeObject;
    private static Model instance;
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

    public GraphicNode createNode(double x, double y) {
        GraphicNode node = new GraphicNode();
        node.setLayoutX(x - node.getWidth() / 2);
        node.setLayoutY(y - node.getWidth() / 2);
        nodes.add(node);
        setNodeHandlers(node);
        return node;
    }

    private void setNodeHandlers(GraphicNode node) {
        node.setOnMousePressed(event -> {
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

    public GraphicEdge createEdge(String from, String to) {
        GraphicNode first = findNode(from);
        GraphicNode second = findNode(to);
        if (first == null || second == null) return null;
        GraphicEdge edge = new GraphicEdge(first, second);
        edges.add(edge);
        first.addConnectedEdges(edge);
        second.addConnectedEdges(edge);
        edge.setStartX(first.getCenterX());
        edge.setStartY(first.getCenterY());
        edge.setEndX(second.getCenterX());
        edge.setEndY(second.getCenterY());
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
        if (event.getSource() instanceof GraphicNode node && node.isWasDragged()) {
            node.setWasDragged(false);
        }
    }

    private void dragStartHandler(MouseEvent event, GraphicNode node) {
        node.setClickX(event.getX());
        node.setClickY(event.getY());
    }

    public void deleteNode(GraphicNode node) {
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
}
