package com.biogame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.biogame.game.levels.FlyingBricksLevel;
import com.biogame.game.levels.GameLevel;
import com.biogame.game.levels.TitleScreen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.biogame.game.levels.FlyingBricksLevel.eighthSublevel;
import static com.biogame.game.levels.FlyingBricksLevel.seventhSublevel;

public class BioGame extends ApplicationAdapter {

    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;
    public Viewport viewport;
    public int GAME_WIDTH = 1080;
    public int GAME_HEIGHT = 1920;

    @Nullable
    private GameLevel gameLevel;
    private AssetManager assetManager = new AssetManager();

    @Override
    public void create() {
        setWorld();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        viewport.apply();
        batch = new SpriteBatch();
        showTitleScreen();
    }

    @Override
    public void render() {
        world.step(1 / 45f, 6, 2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameLevel != null) {
            gameLevel.render(batch);
        }
        batch.end();

        gameLevel.drawUi();
    }

    @Override
    public void dispose() {
        world.dispose();
        box2DDebugRenderer.dispose();
        batch.dispose();
        assetManager.dispose();
        Resources.dispose();
        background.dispose();

        if (gameLevel != null) {
            gameLevel.deleteResources();
        }
    }

    public void changeLevelToNext() {
        if (gameLevel == null) {
            return;
        } else {
            gameLevel.deleteResources();
        }

        switch (gameLevel.getLevelNumber()) {
            case TITLE_SCREEN:
                setGameLevel(FlyingBricksLevel.firstSublevel(this, world));
                break;
            case FIRST:
                setGameLevel(FlyingBricksLevel.secondSublevel(this, world));
                break;
            case SECOND:
                setGameLevel(FlyingBricksLevel.thirdSublevel(this, world));
                break;
            case THIRD:
                setGameLevel(FlyingBricksLevel.sixthSublevel(this, world));
                break;
            case SIXTH:
                setGameLevel(seventhSublevel(this, world));
                break;
            case SEVENTH:
            case EIGHTH:
                Random random = new Random();
                setGameLevel(random.nextBoolean() ? seventhSublevel(this, world) : eighthSublevel(this, world));
                setGameLevel(seventhSublevel(this, world));
                break;
        }
    }

    public void showTitleScreen() {
        if (gameLevel != null) {
            gameLevel.deleteResources();
        }
        setGameLevel(new TitleScreen(this));
        Utils.setScore(0);
    }

    private void setWorld() {
//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

//        final OrthographicCamera camera = new OrthographicCamera();
//        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        final Viewport viewport = new FitViewport(800, 400, camera);
//        viewport.apply();
//        final Viewport viewport = new FitViewport(1920, 1080, camera);

//        camera.setToOrtho(false, 800, 480);
        box2DDebugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0f, 0f), false);
    }

    private void setGameLevel(@NotNull GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        gameLevel.setupUI();
        setMusicResource(gameLevel.getMusicFileName());
        background = new Texture(Gdx.files.internal(gameLevel.getBackgroundFileName()));
        gameLevel.setGestureListeners();
    }

    private void setMusicResource(@NotNull String resourceName) {
//        assetManager.load(resourceName, Music.class);
//        assetManager.finishLoading();
//        assetManager.update();
//        Music music = assetManager.get(resourceName);
//        music.play();
//        music.setLooping(true);
    }
}
