package ru.vsu.cs.dplatov.vvp.task8.logic;

public class TripleValue<N extends Number> {
    private final N dist;
    private final N time;
    private final N price;

    public TripleValue(N dist, N time, N price) {
        this.dist = dist;
        this.time = time;
        this.price = price;
    }

    public N getDist() {
        return dist;
    }

    public N getTime() {
        return time;
    }

    public N getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.join(":", dist.toString(), time.toString(), price.toString());
    }
}
