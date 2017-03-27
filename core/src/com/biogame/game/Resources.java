package com.biogame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public final class Resources {

    private Resources() {
    }

    public static Texture heartTexture = new Texture(Gdx.files.internal("heart.png"));
    public static Texture mask = new Texture(Gdx.files.internal("mask5.png"));
    public static Texture mask_black = new Texture(Gdx.files.internal("mask_black.png"));

    public static Texture tTexture = new Texture(Gdx.files.internal("t_nucleotide.png"));
    public static Texture aTexture = new Texture(Gdx.files.internal("a_nucleotide.png"));
    public static Texture gTexture = new Texture(Gdx.files.internal("g_nucleotide.png"));
    public static Texture cTexture = new Texture(Gdx.files.internal("c_nucleotide.png"));

    public static Texture tHintTexture = new Texture(Gdx.files.internal("t_nucleotide_hint.png"));
    public static Texture aHintTexture = new Texture(Gdx.files.internal("a_nucleotide_hint.png"));
    public static Texture gHintTexture = new Texture(Gdx.files.internal("g_nucleotide_hint.png"));
    public static Texture cHintTexture = new Texture(Gdx.files.internal("c_nucleotide_hint.png"));

    static void dispose() {
        heartTexture.dispose();
        mask.dispose();
        mask_black.dispose();

        tTexture.dispose();
        aTexture.dispose();
        gTexture.dispose();
        cTexture.dispose();

        tHintTexture.dispose();
        aHintTexture.dispose();
        gHintTexture.dispose();
        cHintTexture.dispose();
    }
}
