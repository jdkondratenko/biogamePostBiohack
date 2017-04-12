package com.biogame.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.biogame.game.Resources;

import org.jetbrains.annotations.NotNull;

public final class Tile {

    public final Texture texture;
    public final Body body;

    private Tile(@NotNull Texture texture, @NotNull Body body) {
        this.texture = texture;
        this.body = body;
    }

    public static Tile withLetterT(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.tTexture, body);
    }

    public static Tile withLetterC(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.cTexture, body);
    }

    public static Tile withLetterA(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.aTexture, body);
    }

    public static Tile withLetterG(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.gTexture, body);
    }

    public static Tile hintWithLetterT(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.tHintTexture, body);
    }

    public static Tile hintWithLetterC(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.cHintTexture, body);
    }

    public static Tile hintWithLetterA(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.aHintTexture, body);
    }

    public static Tile hintWithLetterG(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.gHintTexture, body);
    }

    public static Tile withHeart(@NotNull Body body, @NotNull Resources resources) {
        return new Tile(resources.heartTexture, body);
    }
}
