package ru.vsu.cs.dplatov.vvp.task8.graphic.utils;

import javafx.scene.control.TextArea;
import ru.vsu.cs.dplatov.vvp.task8.logic.DefaultGraph;
import ru.vsu.cs.dplatov.vvp.task8.logic.WGraph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGetter {
    private static final Pattern EDGE_PATTERN =
            Pattern.compile("^(\\w+)\\s*->\\s*\\[(\\d+)]\\s*(\\w+)$");

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
}
