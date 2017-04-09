package com.biogame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.biogame.game.BioGame;

import org.jetbrains.annotations.NotNull;

import static com.biogame.game.levels.Level.TITLE_SCREEN;

public final class TitleScreen implements GameLevel {

    @NotNull
    private BioGame bioGame;
    private Stage stage;
    private Texture buttonTexture;
    private Texture logoTexture;

    public TitleScreen(@NotNull BioGame bioGame) {
        this.bioGame = bioGame;
    }

    @NotNull
    @Override
    public String getBackgroundFileName() {
        return "intro_new_red.png";
    }

    @NotNull
    @Override
    public String getMusicFileName() {
        return "music.mp3";
    }

    @Override
    public void setupUI() {
        stage = new Stage(new ScreenViewport());

        setupButton();
        setupLogo();
    }

    private void setupButton() {
        buttonTexture = new Texture(Gdx.files.internal("new_play_yellow.png"));
        final TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        final ImageButton imageButton = new ImageButton(textureRegionDrawable);
        imageButton.setPosition(Gdx.graphics.getWidth() / 2 - 200, (float) (Gdx.graphics.getHeight() * 0.07));
        imageButton.setSize(400, 200);

        stage.addActor(imageButton);

        imageButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bioGame.changeLevelToNext();
                return false;
            }
        });
    }

    private void setupLogo() {
        logoTexture = new Texture(Gdx.files.internal("logo_red.png"));
        final TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(logoTexture));

        final Image image = new Image(textureRegionDrawable);
        image.setPosition(Gdx.graphics.getWidth() / 2 - 250, (float) (Gdx.graphics.getHeight() * 0.47));

        stage.addActor(image);
    }

    @Override
    public void deleteResources() {
        buttonTexture.dispose();
        logoTexture.dispose();
        stage.dispose();
    }

    @Override
    public void setGestureListeners() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(@NotNull Batch batch) {

    }

    @Override
    public void drawUi() {
        stage.act();
        stage.draw();
    }

    @Override
    @NotNull
    public Level getLevelNumber() {
        return TITLE_SCREEN;
    }
}
