package com.biogame.game.model;

public final class Difficulty {

    public final int substitutions;
    public final boolean gap;
    public final boolean reverse;
    public int maxPoints;

    public Difficulty(int substitutions,
                      boolean gap,
                      boolean reverse,
                      int maxPoints) {
        this.substitutions = substitutions;
        this.gap = gap;
        this.reverse = reverse;
        this.maxPoints = maxPoints;
    }
}
