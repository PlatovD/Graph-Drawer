package ru.vsu.cs.dplatov.vvp.task8.graphic.utils;

import javafx.scene.control.TextArea;
import ru.vsu.cs.dplatov.vvp.task8.Model;
import ru.vsu.cs.dplatov.vvp.task8.logic.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.TripleValue;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGetter {
    private static final Pattern EDGE_PATTERN =
            Pattern.compile("^(\\w+)\\s*->\\s*\\[(\\d+):(\\d+):(\\d+)]\\s*(\\w+)$");
    private static final Pattern NODE_PATTERN = Pattern.compile("^(\\w+)\\s*$");
    private static final Random random = new Random(System.currentTimeMillis());

    public static WGraph<String, TripleValue<Integer>> getDataFromStringNotationArea(TextArea textArea) {
        String s = textArea.getText();
        s = s.replace("\n", "");

        WGraph<String, TripleValue<Integer>> graph = new DefaultGraph<>();
        String[] edges = s.split(";");
        for (String ss : edges) {
            createNodesAndAddEdgeFromStingNotation(ss, graph);
        }
        return graph;
    }

    private static void createNodesAndAddEdgeFromStingNotation(String s, WGraph<String, TripleValue<Integer>> graph) {
        Matcher matcherSeparatedNode = NODE_PATTERN.matcher(s);
        if (matcherSeparatedNode.matches()) {
            graph.addNode(matcherSeparatedNode.group(0));
            return;
        }
        Matcher matcher = EDGE_PATTERN.matcher(s);
        String source;
        int dist;
        int time;
        int price;
        String target;
        if (!matcher.matches()) return;
        try {
            source = matcher.group(1);
            dist = Integer.parseInt(matcher.group(2));
            time = Integer.parseInt(matcher.group(3));
            price = Integer.parseInt(matcher.group(4));
            target = matcher.group(5);
        } catch (NumberFormatException e) {
            return;
        }
        graph.addNode(source);
        graph.addNode(target);
        graph.addEdge(source, target, new TripleValue<>(dist, time, price));
    }

//    public static WGraph<String, Integer> get - это будет для TableView

    public static void fromGraphToModel(WGraph<String, TripleValue<Integer>> graph, Model model) {
        for (String val : graph.allNodes()) {
            model.createNode(random.nextInt(80, (int) (Model.getController().getDrawingPane().getWidth() - 20)), random.nextInt(80, (int) (Model.getController().getDrawingPane().getHeight() - 80)), val);
        }

        for (String val : graph.allNodes()) {
            for (WGraph.Edge<String, TripleValue<Integer>> edge : graph.adjacentEdges(val)) {
                model.createEdge(edge.from(), edge.to(), edge.weight().getDist(), edge.weight().getTime(), edge.weight().getPrice());
            }
        }
    }


}
