package ru.vsu.cs.dplatov.vvp.task8.graphic.utils;

import javafx.geometry.Point2D;
import ru.vsu.cs.dplatov.vvp.task8.Model;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.util.HashMap;
import java.util.Map;

public class ForceDirectionLayoutCalculator {
    private static final int ITERATIONS = 100;


    public static void calculateForceDirectedCoordinates(WGraph<String, Integer> graph, Model model) {
        DataGetter.fromGraphToModel(graph, model);
        double W = Model.getController().getDrawingPane().getWidth() - 80;
        double L = Model.getController().getDrawingPane().getHeight() - 80;
        double area = W * L;
        double t = Math.max(W, L) * 0.1;
        double coolingRate = 0.05;
        double k = Math.sqrt(area / model.getNodes().size());

        Map<GraphicNode, Point2D> dispersion = new HashMap<>();
        for (GraphicNode node : model.getNodes()) {
            dispersion.put(node, new Point2D(0, 0));
        }

        for (int i = 0; i < ITERATIONS; i++) {
            for (GraphicNode v : model.getNodes()) {
                dispersion.put(v, new Point2D(0, 0));

                // repulsive forces
                for (GraphicNode u : model.getNodes()) {
                    if (v != u) {
                        Point2D delta = v.getPosition().subtract(u.getPosition());
                        double dist = delta.magnitude();
                        if (dist > 0) {
                            dispersion.put(v, delta.normalize().multiply(fr(dist, k)).add(dispersion.get(v)));
                        }
                    }
                }

                // attractive forces
                for (GraphicNode u : v.getConnectedNodes()) {
                    Point2D delta = v.getPosition().subtract(u.getPosition());
                    double dist = delta.magnitude();
                    dispersion.put(v, dispersion.get(v).subtract(delta.normalize().multiply(fa(dist, k))));
                    dispersion.put(u, dispersion.get(u).add(delta.normalize().multiply(fa(dist, k))));
                }

                for (GraphicNode u : model.getNodes()) {
                    u.setPosition(u.getPosition().add(dispersion.get(u).normalize().multiply(Math.min(t, u.getPosition().magnitude()))));
                    u.setPosition(new Point2D(Math.min(W, Math.max(0, u.getPosition().getX())), Math.min(L, Math.max(0, u.getPosition().getY()))));
                }
                t = t * (1.0 - coolingRate);
            }
        }
        for (GraphicNode node : model.getNodes()) {
            node.updateEdgesPositions();
        }
    }

    private static double fa(double x, double k) {
        return Math.pow(x, 2) / k;
    }

    private static double fr(double x, double k) {
        return Math.pow(k, 2) / x;
    }

    private static Point2D constrainPosition(Point2D position, double maxWidth, double maxHeight) {
        double x = Math.max(70, Math.min(maxWidth, position.getX()));
        double y = Math.max(70, Math.min(maxHeight, position.getY()));
        return new Point2D(x, y);
    }

}
