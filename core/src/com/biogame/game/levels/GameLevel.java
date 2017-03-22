package com.biogame.game.levels;

import com.badlogic.gdx.graphics.g2d.Batch;

import org.jetbrains.annotations.NotNull;

public interface GameLevel {

    @NotNull
    String getBackgroundFileName();

    @NotNull
    String getMusicFileName();

    void setupUI();

    void deleteResources();

    void setGestureListeners();

    void render(@NotNull Batch batch);

    void drawUi();

    @NotNull
    Level getLevelNumber();
}
