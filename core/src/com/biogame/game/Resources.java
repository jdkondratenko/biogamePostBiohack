package com.biogame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public final class Resources {

    public Texture heartTexture = new Texture(Gdx.files.internal("heart.png"));
    public Texture mask = new Texture(Gdx.files.internal("mask.png"));
    public Texture mask_black = new Texture(Gdx.files.internal("mask_black.png"));

    public Texture tTexture = new Texture(Gdx.files.internal("t_nucleotide.png"));
    public Texture aTexture = new Texture(Gdx.files.internal("a_nucleotide.png"));
    public Texture gTexture = new Texture(Gdx.files.internal("g_nucleotide.png"));
    public Texture cTexture = new Texture(Gdx.files.internal("c_nucleotide.png"));

    public Texture tHintTexture = new Texture(Gdx.files.internal("t_nucleotide_hint.png"));
    public Texture aHintTexture = new Texture(Gdx.files.internal("a_nucleotide_hint.png"));
    public Texture gHintTexture = new Texture(Gdx.files.internal("g_nucleotide_hint.png"));
    public Texture cHintTexture = new Texture(Gdx.files.internal("c_nucleotide_hint.png"));

    public void dispose() {
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
