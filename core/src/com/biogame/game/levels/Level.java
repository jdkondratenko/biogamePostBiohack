package com.biogame.game.levels;

public enum Level {
    TITLE_SCREEN(0),
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6),
    SEVENTH(7),
    EIGHTH(8);

    private int number;

    Level(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
