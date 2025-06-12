package ru.vsu.cs.dplatov.vvp.task8.graphic.utils;

import org.controlsfx.control.PropertySheet;
import ru.vsu.cs.dplatov.vvp.task8.Model;
import ru.vsu.cs.dplatov.vvp.task8.graphic.elements.GraphicNode;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.util.Random;

public class ForceDirectionLayoutCalculator {
    private static final double REPULSION = 1000;
    private static final double SPRING_LENGTH = 100;
    private static final double SPRING_CONSTANT = 0.01;
    private static final double DAMPING = 0.5;
    private static final Random random = new Random();


    public static void calculateForceDirectedCoordinates(WGraph<String, Integer> graph, Model model) {
        for (String val : graph.allNodes()) {
            model.createNode(random.nextInt(80, (int) (Model.getController().getDrawingPane().getWidth() - 80)), random.nextInt(80, (int) (Model.getController().getDrawingPane().getHeight() - 80)), val);
        }

        for (String val : graph.allNodes()) {
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(val)) {
                model.createEdge(edge.from(), edge.to(), edge.weight());
            }
        }


    }
}
