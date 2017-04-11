package com.biogame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.biogame.game.BioGame;
import com.biogame.game.Resources;
import com.biogame.game.Utils;
import com.biogame.game.model.Difficulty;
import com.biogame.game.model.Generator_new;
import com.biogame.game.model.LetterPermutation;
import com.biogame.game.model.Tile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import static com.biogame.game.Utils.getReplacementInDnaMessage;
import static com.biogame.game.Utils.getMoreReplacementInDnaMessage;
import static com.biogame.game.Utils.getReverseAlignMessage;
import static com.biogame.game.Utils.getScore;
import static com.biogame.game.Utils.getStartGameMessage;
import static com.biogame.game.levels.Level.EIGHTH;
import static com.biogame.game.levels.Level.FIFTH;
import static com.biogame.game.levels.Level.FIRST;
import static com.biogame.game.levels.Level.FOURTH;
import static com.biogame.game.levels.Level.SECOND;
import static com.biogame.game.levels.Level.SEVENTH;
import static com.biogame.game.levels.Level.SIXTH;
import static com.biogame.game.levels.Level.THIRD;
import static com.biogame.game.model.ScoreCounter.countScore;
import static java.lang.Float.compare;

public final class FlyingBricksLevel implements GameLevel {

    private static final int NUMBER_OF_TILES_IN_GENOME = 12;
    private static final int NUMBER_OF_TILES_IN_READ = 5;
    private static final int NUMBER_OF_HEARTS = 3;

    @NotNull
    private final Level levelNumber;

    @NotNull
    private Difficulty difficulty;
    @NotNull
    private BioGame bioGame;

    private float WORLD_TO_BOX = .01f;
    private float BOX_TO_WORLD = 100f;

    private float start_velocity = -1.5f;
    private float velocity = 0.0f;

    private final int width;
    private final int height;

    private int font_scale;

    private float mute_x;
    private float mute_y;
    private float reverse_x;
    private float reverse_y;

    private ParticleEffect levelParticleEffect;
    private ParticleEffect failParticleEffect;
    private ParticleEffect winParticleEffect;

    private AssetManager assetManager;

    @NotNull
    private World world;
    private Stage stage;
    private Label scoreLabel;
    private Label gameoverLabel;
    private Label levelNumberLabel;
    private Label messageLabel;

    private int subsublevel_count = 0;
    private int levelScore;

    private long lastTapTime;
    private Texture reverseTexture;
    private Texture noteTexture;
    private Texture noteMutedTexture;

    private boolean gameover;
    private boolean reversed;
    private boolean showMessage;
    private boolean muted = false;

    @NotNull
    private final String currentMessage;
    @Nullable
    private Music winMusic;
    @Nullable
    private Music loseMusic;

    @NotNull
    private String readString = "";
    @NotNull
    private String genomeString = "";
    @NotNull
    private String genomeHintString = "";

    @NotNull
    private ArrayList<Tile> genome = new ArrayList<Tile>();
    @NotNull
    private ArrayList<Tile> read = new ArrayList<Tile>();
    @NotNull
    private ArrayList<Tile> hearts = new ArrayList<Tile>();
    @NotNull
    private ArrayList<Tile> genomeHint = new ArrayList<Tile>();

    private final int padding;
    private final int tileSize;

    private FlyingBricksLevel(@NotNull BioGame bioGame,
                              @NotNull World world,
                              @NotNull Difficulty difficulty,
                              @NotNull Level levelNumber,
                              @NotNull String message) {
        this.world = world;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.difficulty = difficulty;
        this.levelNumber = levelNumber;
        this.bioGame = bioGame;

        currentMessage = message;
        showMessage = !currentMessage.isEmpty();

        assetManager = new AssetManager();
        padding = (int) (width * 0.1);

        final int genomeWidth = (int) (width * 0.8);
        tileSize = genomeWidth / NUMBER_OF_TILES_IN_GENOME;

        if (width < 500) {
            font_scale = 2;
            mute_x = bioGame.GAME_WIDTH - tileSize * 4f;
            mute_y = bioGame.GAME_HEIGHT - tileSize * 10f;
            reverse_x = bioGame.GAME_WIDTH - 200;
            reverse_y = tileSize * 11;
        } else if (width < 800) {
            font_scale = 3;
            mute_x = bioGame.GAME_WIDTH - tileSize * 3f;
            mute_y = bioGame.GAME_HEIGHT - tileSize * 6f;
            reverse_x = bioGame.GAME_WIDTH - 155;
            reverse_y = tileSize * 7;
        } else if (width < 1200) {
            font_scale = 4;
            mute_x = bioGame.GAME_WIDTH - tileSize * 2f;
            mute_y = bioGame.GAME_HEIGHT - tileSize * 3.5f;
            reverse_x = bioGame.GAME_WIDTH - 105;
            reverse_y = tileSize * 5;
        } else {
            font_scale = 5;
            mute_x = bioGame.GAME_WIDTH - tileSize * 2f;
            mute_y = bioGame.GAME_HEIGHT - tileSize * 2.5f;
            reverse_x = bioGame.GAME_WIDTH - 105;
            reverse_y = tileSize * 5;
        }

        setupSounds();
    }

    @NotNull
    public static FlyingBricksLevel firstSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(0, false, false, 20), FIRST, getStartGameMessage());
    }

    @NotNull
    public static FlyingBricksLevel secondSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(1, false, false, 30), SECOND, getReplacementInDnaMessage());
    }

    @NotNull
    public static FlyingBricksLevel thirdSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(2, false, false, 40), THIRD, getMoreReplacementInDnaMessage());
    }

    @NotNull
    public static FlyingBricksLevel fourthSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(0, true, false, 50), FOURTH, ""); //TODO: add message?
    }

    @NotNull
    public static FlyingBricksLevel fifthSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(1, true, false, 60), FIFTH, "");
    }

    @NotNull
    public static FlyingBricksLevel sixthSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(0, false, true, 70), SIXTH, getReverseAlignMessage());
    }

    @NotNull
    public static FlyingBricksLevel seventhSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(2, false, true, 80), SEVENTH, "");
    }

    @NotNull
    public static FlyingBricksLevel eighthSublevel(@NotNull BioGame bioGame, @NotNull World world) {
        return new FlyingBricksLevel(bioGame, world, new Difficulty(2, false, false, 80), EIGHTH, "");
    }

    @NotNull
    @Override
    public String getBackgroundFileName() {
        return "background_raw_small.png";
    }

    @NotNull
    @Override
    public String getMusicFileName() {
        return "music.mp3";
    }

    @Override
    public void setupUI() {
        stage = new Stage(bioGame.viewport);
        hearts = createHearts();
        setupParticles();
        setupScoreText();
        setupGameoverMessage();
        setupReverseButton();
        setupLevelLabel();
        setupNoteButton();

        if (showMessage) {
            setupMessageLabel();
        } else {
            startPlaying();
        }
    }

    private void setupParticles() {
        levelParticleEffect = new ParticleEffect();
        levelParticleEffect.load(Gdx.files.internal("effect1.parti"), Gdx.files.internal(""));

        winParticleEffect = new ParticleEffect();
        winParticleEffect.load(Gdx.files.internal("win_effect.parti"), Gdx.files.internal(""));
        winParticleEffect.scaleEffect(64 * NUMBER_OF_TILES_IN_READ * Utils.getResizeCoef());

        failParticleEffect = new ParticleEffect();
        failParticleEffect.load(Gdx.files.internal("fail_effect.parti"), Gdx.files.internal(""));
        failParticleEffect.scaleEffect(64 * NUMBER_OF_TILES_IN_READ * Utils.getResizeCoef());
    }

    private void startPlaying() {
        read = createRead();
        genome = createGenome();
    }

    @Override
    public void deleteResources() {
        if (reverseTexture != null) {
            reverseTexture.dispose();
        }

        if (noteTexture != null) {
            noteTexture.dispose();
        }

        if (noteMutedTexture != null) {
            noteMutedTexture.dispose();
        }

        levelParticleEffect.dispose();
        winParticleEffect.dispose();
        failParticleEffect.dispose();

        stage.dispose();
        assetManager.dispose();
    }

    @Override
    public void setGestureListeners() {
        final InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(new GestureListener()));
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(@NotNull Batch batch) {
        drawTileSequence(genome, batch, !reversed);
        drawTileSequence(read, batch, false);
        drawTileSequence(hearts, batch, false);

        failParticleEffect.update(Gdx.graphics.getDeltaTime());
        failParticleEffect.draw(batch);

        winParticleEffect.update(Gdx.graphics.getDeltaTime());
        winParticleEffect.draw(batch);

        if (reversed) {
            drawTileSequence(genomeHint, batch, true);
        }

        checkForIntersection();

        if (!hearts.isEmpty()) {
            levelNumberLabel.getStyle().font.draw(
                    batch, "Level " + levelNumber.getNumber(), padding, hearts.get(0).body.getPosition().y * BOX_TO_WORLD - 70);
            if (getLevelNumber() != FIRST) {
                levelParticleEffect.update(Gdx.graphics.getDeltaTime());
                levelParticleEffect.draw(batch);
            }
        }

        if (gameover) {
            batch.draw(Resources.mask_black, 0, 0, width, height);
            gameoverLabel.getStyle().font.draw(
                    batch, "Game over! Your score: " + String.valueOf(getScore()), padding, height / 2 - 100);
        } else {
            final Body heart = hearts.get(0).body;
            scoreLabel.getStyle().font.draw(batch, String.valueOf(getScore()),
                    (float) (width - tileSize * 2.5), heart.getPosition().y * BOX_TO_WORLD + tileSize / 1.3f);
        }

        if (showMessage) {
            batch.draw(Resources.mask_black, 0, 0, width, height);
            messageLabel.getStyle().font.draw(batch, currentMessage, padding / 2, height / 2 + 50);
        }
    }

    @Override
    public void drawUi() {
        stage.act();
        stage.draw();
    }

    @Override
    @NotNull
    public Level getLevelNumber() {
        return levelNumber;
    }

    private void setupSounds() {
        final String winSoundRes = "win_sound.mp3";
        final String loseSoundRes = "lose_sound.mp3";

        assetManager.load(winSoundRes, Music.class);
        assetManager.load(loseSoundRes, Music.class);
        assetManager.finishLoading();
        assetManager.update();

        winMusic = assetManager.get(winSoundRes);
        loseMusic = assetManager.get(loseSoundRes);
    }

    private void setupLevelLabel() {
        final LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.setColor(Color.WHITE);
        labelStyle.font.getData().setScale(font_scale);
        levelNumberLabel = new Label("Level " + getLevelNumber().getNumber() + 1, labelStyle);

        levelParticleEffect.getEmitters().first().setPosition(padding + 100, hearts.get(0).body.getPosition().y * BOX_TO_WORLD - 100);
        levelParticleEffect.start();
    }

    private void setupMessageLabel() {
        final LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.setColor(Color.LIME);
        labelStyle.font.getData().setScale(font_scale);
        messageLabel = new Label("", labelStyle);
        messageLabel.setWrap(true);
        messageLabel.setSize(width - padding, height);
    }

    private void setupReverseButton() {
        if (levelNumber == Level.SIXTH || levelNumber == Level.SEVENTH || levelNumber == Level.EIGHTH) {

            reverseTexture = new Texture(Gdx.files.internal("reverse.png"));
            final TextureRegion textureRegion = new TextureRegion(reverseTexture);
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

            final ImageButton imageButton = new ImageButton(textureRegionDrawable);
            imageButton.setPosition(reverse_x, reverse_y);
            imageButton.setSize(96, 96);

            stage.addActor(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    reverseRead();
                    return false;
                }
            });
        }
    }

    private void setupNoteButton() {
            noteTexture = new Texture(Gdx.files.internal("note.png"));
            noteMutedTexture = new Texture(Gdx.files.internal("note_muted.png"));
            final TextureRegion textureRegion = new TextureRegion(noteTexture);
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
            final TextureRegion textureRegionDown = new TextureRegion(noteMutedTexture);
            TextureRegionDrawable textureRegionDrawableDown = new TextureRegionDrawable(textureRegionDown);

            final ImageButton imageButton = new ImageButton(textureRegionDrawable, textureRegionDrawableDown, textureRegionDrawableDown);
            imageButton.setPosition(mute_x, mute_y);
            imageButton.setSize(96, 96);

            stage.addActor(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    muted = !muted;
                    return false;
                }
            });
    }

    private void setupGameoverMessage() {
        final LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.setColor(Color.RED);
        labelStyle.font.getData().setScale(font_scale);
        gameoverLabel = new Label("Game Over!", labelStyle);
    }

    private void setupScoreText() {
        final LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.setColor(Color.RED);
        labelStyle.font.getData().setScale(font_scale);
        scoreLabel = new Label(String.valueOf(getScore()), labelStyle);
    }

    private void checkForIntersection() {
        if (read.isEmpty() || genome.isEmpty() || gameover) {
            return;
        }

        final int stopPositionCoef = reversed ? 2 : 1;
        if (read.get(0).body.getPosition().y <= genome.get(0).body.getPosition().y * stopPositionCoef + tileSize * WORLD_TO_BOX) {


            final int addScore = countScore(getFullReadString(), reversed ? genomeHintString : genomeString, difficulty);
            changeScore(addScore);

            final boolean madeMistake = addScore < difficulty.maxPoints;
            final Vector2 centerTilePosition = read.get(2).body.getPosition();

            if (madeMistake) {
                failParticleEffect.getEmitters().first()
                        .setPosition(centerTilePosition.x * BOX_TO_WORLD + tileSize / 2, centerTilePosition.y * BOX_TO_WORLD + tileSize / 2);
                failParticleEffect.start();

                if (loseMusic != null && !muted) {
                    loseMusic.play();
                }
                decreaseHeartsNumber();
                if (hearts.isEmpty()) {
                    showGameOver();
                    return;
                }
            } else {
                winParticleEffect.getEmitters().first()
                        .setPosition(centerTilePosition.x * BOX_TO_WORLD + tileSize / 2, centerTilePosition.y * BOX_TO_WORLD + tileSize);
                winParticleEffect.start();

                if (winMusic != null && !muted) {
                    winMusic.play();
                }

                subsublevel_count++;
                if (subsublevel_count % 3 == 0 && subsublevel_count != 0){
                    levelScore++;
                }
                if (levelScore == 5) {
                    bioGame.changeLevelToNext();
                    start_velocity = start_velocity * 1.1f;
                }

//                changeReadSpeed(velocity * 1.2f);
            }

            startPlaying();
            reversed = false;
        }
    }

    @NotNull
    private String getFullReadString() {
        final StringBuilder fullReadBuilder = new StringBuilder();
        for (int i = 0; i < getFirstEntry(); i++) {
            fullReadBuilder.append('_');
        }
        fullReadBuilder.append(readString);
        final int remainingChars = NUMBER_OF_TILES_IN_GENOME - fullReadBuilder.length();
        for (int i = 0; i < remainingChars; i++) {
            fullReadBuilder.append('_');
        }

        return fullReadBuilder.toString();
    }

    private int getFirstEntry() {
        int firstEntry = 0;
        for (int i = 0; i < genome.size(); i++) {
            if (compare(Math.round(read.get(0).body.getPosition().x * 100d), Math.round(genome.get(i).body.getPosition().x * 100d) ) == 0) {
                firstEntry = i;
                break;
            }
        }
        return firstEntry;
    }

    private void showGameOver() {
        changeReadSpeed(0);
        gameover = true;
    }

    private void changeReadSpeed(float speed) {
        for (Tile tile : read) {
            tile.body.setLinearVelocity(0, speed);
        }
        velocity = speed;
    }

    private void decreaseHeartsNumber() {
        hearts.remove(hearts.get(hearts.size() - 1));
    }

    private void changeScore(int addScore) {
        Utils.setScore(getScore() + addScore);
    }

    private void reverseRead() {
        if (gameover || read.isEmpty() || genome.isEmpty()) {
            return;
        }

        Collections.reverse(read);
        readString = new StringBuilder(readString).reverse().toString();

        final float firstTilePosition = read.get(0).body.getPosition().x;
        final float secondTilePosition = read.get(1).body.getPosition().x;

        read.get(0).body.setTransform(read.get(4).body.getPosition().x, read.get(0).body.getPosition().y, 0);
        read.get(1).body.setTransform(read.get(3).body.getPosition().x, read.get(1).body.getPosition().y, 0);

        read.get(3).body.setTransform(secondTilePosition, read.get(3).body.getPosition().y, 0);
        read.get(4).body.setTransform(firstTilePosition, read.get(4).body.getPosition().y, 0);

        if (reversed) {
            reversed = false;
        } else {
            reversed = true;
            genomeHint = createGenomeHint();
        }
    }

    private void drawTileSequence(@NotNull ArrayList<Tile> tileSequence,
                                  @NotNull Batch batch,
                                  boolean mask) {
        if (!tileSequence.isEmpty()) {
            for (int i = 0; i < tileSequence.size(); i++) {
                final Tile tile = tileSequence.get(i);
                final Texture texture = tile.texture;
                final Vector2 position = tile.body.getPosition();
                batch.draw(texture, position.x * BOX_TO_WORLD, position.y * BOX_TO_WORLD, width * 0.063f, width * 0.063f);

                final int firstEntry = getFirstEntry();
                if (mask && i >= firstEntry && i < firstEntry + 5) {
                    batch.draw(Resources.mask, position.x * BOX_TO_WORLD, position.y * BOX_TO_WORLD, width * 0.063f, width * 0.063f);
                }
            }
        }
    }

    @NotNull
    private Body createTile(float x, float y, boolean staticBody) {
        final Body body;
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = staticBody ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(tileSize * WORLD_TO_BOX, tileSize * WORLD_TO_BOX);
        body.createFixture(shape, 1);
        shape.dispose();
        body.getFixtureList().get(0).setSensor(true);
        return body;
    }

    @NotNull
    private ArrayList<Tile> createHearts() {
        final ArrayList<Tile> hearts = new ArrayList<Tile>();
        float startPositionX = padding * WORLD_TO_BOX;
        for (int i = 0; i < NUMBER_OF_HEARTS; i++) {
            final Body body = createTile(startPositionX, (height - tileSize * 2) * WORLD_TO_BOX, true);
            final Tile tile = Tile.withHeart(body);
            hearts.add(tile);
            startPositionX += (tileSize) * WORLD_TO_BOX;
        }
        return hearts;
    }

    @NotNull
    private ArrayList<Tile> createRead() {
        final String readSequence = Generator_new.generateRead();
        readString = readSequence;

        final ArrayList<Tile> read = new ArrayList<Tile>();
        final float startPositionY = (height - tileSize * 2) * WORLD_TO_BOX;

        float startPositionX = (padding + (tileSize * 4)) * WORLD_TO_BOX;

        for (int i = 0; i < NUMBER_OF_TILES_IN_READ; i++) {
            final char nucleotide = readSequence.charAt(i);
            final Tile tile;
            final Body body = createTile(startPositionX, startPositionY, false);
            body.setLinearVelocity(0, start_velocity);
            velocity = start_velocity;

            switch (nucleotide) {
                case 'A':
                    tile = Tile.withLetterA(body);
                    break;
                case 'C':
                    tile = Tile.withLetterC(body);
                    break;
                case 'G':
                    tile = Tile.withLetterG(body);
                    break;
                case 'T':
                    tile = Tile.withLetterT(body);
                    break;
                default:
                    throw new IllegalArgumentException("The letter is not a, c, g or t");
            }

            read.add(tile);
            startPositionX += (tileSize) * WORLD_TO_BOX;
        }

        return read;
    }

    @NotNull
    private ArrayList<Tile> createGenome() {
        final LetterPermutation letterPermutation;
        switch (levelScore) {
            case 0:
                letterPermutation = LetterPermutation.EASY;
                break;
            case 1:
                letterPermutation = LetterPermutation.MEDIUM;
                break;
            case 2:
                letterPermutation = LetterPermutation.MEDIUM;
                break;
            case 3:
                letterPermutation = LetterPermutation.HARD;
                break;
            default:
                letterPermutation = LetterPermutation.VERY_HARD;
                break;
        }

        final String genomeSequence = Generator_new.generateGenome(difficulty, readString, letterPermutation, subsublevel_count);
        final ArrayList<Tile> genome = new ArrayList<Tile>();

        float startPositionX = padding * WORLD_TO_BOX;

        for (int i = 0; i < NUMBER_OF_TILES_IN_GENOME; i++) {
            final char nucleotide = genomeSequence.charAt(i);
            final Tile tile;
            final Body body = createTile(startPositionX, tileSize * WORLD_TO_BOX, true);

            switch (nucleotide) {
                case 'A':
                    tile = Tile.withLetterA(body);
                    break;
                case 'C':
                    tile = Tile.withLetterC(body);
                    break;
                case 'G':
                    tile = Tile.withLetterG(body);
                    break;
                case 'T':
                    tile = Tile.withLetterT(body);
                    break;
                default:
                    throw new IllegalArgumentException("The letter is not a, c, g or t");
            }

            genome.add(tile);
            startPositionX += (tileSize) * WORLD_TO_BOX;
        }

        genomeString = genomeSequence;
        return genome;
    }

    @NotNull
    private ArrayList<Tile> createGenomeHint() {
        final StringBuilder reverseSequenceBuilder = new StringBuilder();

        for (int i = 0; i < genomeString.length(); i++) {
            final char letter = genomeString.charAt(i);
            switch (letter) {
                case 'A':
                    reverseSequenceBuilder.append('T');
                    break;
                case 'C':
                    reverseSequenceBuilder.append('G');
                    break;
                case 'G':
                    reverseSequenceBuilder.append('C');
                    break;
                case 'T':
                    reverseSequenceBuilder.append('A');
                    break;
                default:
                    throw new IllegalArgumentException("The letter is not a, c, g or t");
            }
        }

        genomeHintString = reverseSequenceBuilder.toString();
        final ArrayList<Tile> genome = new ArrayList<Tile>();

        float startPositionX = padding * WORLD_TO_BOX;

        for (int i = 0; i < NUMBER_OF_TILES_IN_GENOME; i++) {
            final char nucleotide = genomeHintString.charAt(i);
            final Tile tile;
            final Body body = createTile(startPositionX, tileSize * 2 * WORLD_TO_BOX, true);

            switch (nucleotide) {
                case 'A':
                    tile = Tile.hintWithLetterA(body);
                    break;
                case 'C':
                    tile = Tile.hintWithLetterC(body);
                    break;
                case 'G':
                    tile = Tile.hintWithLetterG(body);
                    break;
                case 'T':
                    tile = Tile.hintWithLetterT(body);
                    break;
                default:
                    throw new IllegalArgumentException("The letter is not a, c, g or t");
            }

            genome.add(tile);
            startPositionX += (tileSize) * WORLD_TO_BOX;
        }

        return genome;
    }

    private class GestureListener implements GestureDetector.GestureListener {

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            if (gameover) {
                bioGame.showTitleScreen();
            }

            if (showMessage) {
                showMessage = false;
                startPlaying();
            }
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            winParticleEffect.getEmitters().first()
                    .setPosition(read.get(2).body.getPosition().x * BOX_TO_WORLD + tileSize / 2, read.get(2).body.getPosition().y * BOX_TO_WORLD + tileSize);
            winParticleEffect.start();
            changeReadSpeed(velocity * 10);
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (read.isEmpty() || genome.isEmpty()) {
                return false;
            }

            if ((lastTapTime > System.currentTimeMillis() - 600 && (deltaX < 20 && deltaX > -20))
                    || (compare(Math.round(read.get(0).body.getPosition().x), Math.round(genome.get(0).body.getPosition().x)) == 0 && deltaX <= 0)
                    || (compare(read.get(NUMBER_OF_TILES_IN_READ - 1).body.getPosition().x, genome.get(NUMBER_OF_TILES_IN_GENOME - 1).body.getPosition().x) == 0 && deltaX >= 0)) {
                return false;
            }

            for (Tile tile : read) {
                final Vector2 position = tile.body.getPosition();
                if (deltaX > 0) {
                    tile.body.setTransform(position.x + (tileSize * WORLD_TO_BOX), position.y, 0);
                } else {
                    tile.body.setTransform(position.x - (tileSize * WORLD_TO_BOX), position.y, 0);
                }
            }
            lastTapTime = System.currentTimeMillis();

            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {

            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }
    }
}
