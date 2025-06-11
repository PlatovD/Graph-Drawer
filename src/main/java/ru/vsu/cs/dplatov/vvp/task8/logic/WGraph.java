package ru.vsu.cs.dplatov.vvp.task8.logic;

public interface WGraph<T, N extends Number> {

    /**
     * Количество вершин в графе
     *
     * @return кол-во
     */
    int nodesCnt();

    /**
     * Количество ребер в графе
     *
     * @return кол-во
     */
    int edgesCnt();

    /**
     * Добавить ребро в граф
     *
     * @param v1     начало
     * @param v2     конец
     * @param weight вес ребра
     */
    void addEdge(T v1, T v2, N weight);


    /**
     * Добавить узел в граф
     *
     * @param value занчение узла
     */
    void addNode(T value);

    boolean containsNode(T value);

    boolean containsEdge(T from, T to);

    /**
     * @param v1 начало
     * @param v2 конец
     * @return успешность удаления
     */
    boolean removeEdge(T v1, T v2);

    boolean removeNode(T node);

    /**
     * Найти все смежные вершины к данной
     *
     * @param v вершина
     * @return Итерируемый объект из связанных вершин
     */
    Iterable<T> adjacentNodes(T v);


    /**
     * Найти все ребра от этого объекта
     *
     * @return итерируемый объект из ребер
     */
    Iterable<Edge<T, N>> adjacentEdges(T v);

    /**
     * @param <T> Тип значений узлов, которые связывает ребро
     * @param <N> Тип значения веса ребра
     */
    interface Edge<T, N extends Number> {
        T from();

        T to();

        N weight();
    }

}
