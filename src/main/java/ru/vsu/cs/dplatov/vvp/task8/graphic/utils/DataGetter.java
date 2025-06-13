package ru.vsu.cs.dplatov.vvp.task8.graphic.utils;

import javafx.scene.control.TextArea;
import ru.vsu.cs.dplatov.vvp.task8.Model;
import ru.vsu.cs.dplatov.vvp.task8.logic.Route;
import ru.vsu.cs.dplatov.vvp.task8.logic.utils.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.utils.WGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGetter {
    private static final Pattern EDGE_PATTERN =
            Pattern.compile("^(\\w+)\\s*->\\s*\\[(\\d+)]\\s*(\\w+)$");
    private static final Pattern NODE_PATTERN = Pattern.compile("^(\\w+)\\s*$");
    private static final Random random = new Random(System.currentTimeMillis());

    public static WGraph<String, Integer> getDataFromStringNotationArea(TextArea textArea) {
        String s = textArea.getText();
        s = s.replace("\n", "");

        WGraph<String, Integer> graph = new DefaultGraph<>();
        String[] edges = s.split(";");
        for (String ss : edges) {
            createNodesAndAddEdgeFromStingNotation(ss, graph);
        }
        return graph;
    }

    private static void createNodesAndAddEdgeFromStingNotation(String s, WGraph<String, Integer> graph) {
        Matcher matcherSeparatedNode = NODE_PATTERN.matcher(s);
        if (matcherSeparatedNode.matches()) {
            graph.addNode(matcherSeparatedNode.group(0));
            return;
        }
        Matcher matcher = EDGE_PATTERN.matcher(s);
        String source;
        int weight;
        String target;
        if (!matcher.matches()) return;
        try {
            source = matcher.group(1);
            weight = Integer.parseInt(matcher.group(2));
            target = matcher.group(3);
        } catch (NumberFormatException e) {
            return;
        }
        graph.addNode(source);
        graph.addNode(target);
        graph.addEdge(source, target, weight);
    }

//    public static WGraph<String, Integer> get - это будет для TableView

    public static void fromGraphToModel(WGraph<String, Integer> graph, Model model) {
        for (String val : graph.allNodes()) {
            model.createNode(random.nextInt(80, (int) (Model.getController().getDrawingPane().getWidth() - 20)), random.nextInt(80, (int) (Model.getController().getDrawingPane().getHeight() - 80)), val);
        }

        for (String val : graph.allNodes()) {
            for (WGraph.Edge<String, Integer> edge : graph.adjacentEdges(val)) {
                model.createEdge(edge.from(), edge.to(), edge.weight());
            }
        }
    }

    public static Route parsePathFromStringNotation(String s) {
        List<String> path = new ArrayList<>();
        for (String node : s.split("-")) {
            path.add(node.strip());
        }
        return new Route(path);
    }
}
