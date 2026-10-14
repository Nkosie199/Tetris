package com.mygdx.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Draws the board and reads the controls. Keyboard: arrows move, Up rotates, Down drops faster, Space drops,
 * P pauses, Enter starts again. Touch: tap left or right of the board to move, the middle to rotate, the bottom
 * to drop, the side panel to pause.
 */
public class Tetris extends ApplicationAdapter {
    static final int CELL = 35;
    static final int BOARD_WIDTH = Board.COLS * CELL;
    static final int BOARD_HEIGHT = Board.ROWS * CELL;
    static final int PANEL_X = BOARD_WIDTH + 15;
    static final int WORLD_WIDTH = BOARD_WIDTH + 170;
    private static final float REPEAT_DELAY = 0.17f;
    private static final float REPEAT_EVERY = 0.05f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(WORLD_WIDTH, BOARD_HEIGHT, camera);
    private final Vector2 touch = new Vector2();
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;
    private Board board;
    private boolean paused;
    private int heldDirection;
    private float repeatTimer;

    @Override
    public void create() {
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("calibri.fnt"), Gdx.files.internal("calibri.png"), false);
        board = new Board();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
        handleInput(delta);
        if (!paused) {
            board.update(delta, Gdx.input.isKeyPressed(Keys.DOWN));
        }
        draw();
    }

    private void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            handleTap();
            return;
        }
        if (board.isGameOver()) {
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                restart();
            }
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            paused = !paused;
        }
        if (paused) {
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            board.rotate();
        }
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            board.hardDrop();
        }
        repeatSideways(delta);
    }

    /** One step per key press, then repeated steps while the key stays down, instead of a step every frame. */
    private void repeatSideways(float delta) {
        int direction = Gdx.input.isKeyPressed(Keys.LEFT) ? -1 : Gdx.input.isKeyPressed(Keys.RIGHT) ? 1 : 0;
        if (direction == 0) {
            heldDirection = 0;
            return;
        }
        if (direction != heldDirection) {
            heldDirection = direction;
            repeatTimer = -REPEAT_DELAY;
            step(direction);
            return;
        }
        repeatTimer += delta;
        while (repeatTimer >= REPEAT_EVERY) {
            repeatTimer -= REPEAT_EVERY;
            step(direction);
        }
    }

    private void step(int direction) {
        if (direction < 0) {
            board.moveLeft();
        } else {
            board.moveRight();
        }
    }

    private void handleTap() {
        viewport.unproject(touch.set(Gdx.input.getX(), Gdx.input.getY()));
        if (board.isGameOver()) {
            restart();
        } else if (touch.x > BOARD_WIDTH) {
            paused = !paused;
        } else if (paused) {
            paused = false;
        } else if (touch.y < BOARD_HEIGHT * 0.2f) {
            board.hardDrop();
        } else if (touch.x < BOARD_WIDTH / 3f) {
            board.moveLeft();
        } else if (touch.x > BOARD_WIDTH * 2 / 3f) {
            board.moveRight();
        } else {
            board.rotate();
        }
    }

    private void restart() {
        board = new Board();
        paused = false;
        heldDirection = 0;
    }

    private void draw() {
        ScreenUtils.clear(0.02f, 0.03f, 0.08f, 1f);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        shapes.begin(ShapeType.Filled);
        shapes.setColor(0.08f, 0.09f, 0.16f, 1f);
        shapes.rect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cell(row, col) != 0) {
                    cell(board.cell(row, col), col * CELL, (Board.ROWS - 1 - row) * CELL, CELL);
                }
            }
        }
        int[][] piece = board.getPiece();
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    cell(piece[i][j], (board.getPieceCol() + j) * CELL, (Board.ROWS - 1 - board.getPieceRow() - i) * CELL, CELL);
                }
            }
        }
        int[][] next = board.getNextPiece();
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next[i].length; j++) {
                if (next[i][j] != 0) {
                    cell(next[i][j], PANEL_X + j * 20, 600 - i * 20, 20);
                }
            }
        }
        shapes.end();

        if (paused || board.isGameOver()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapes.begin(ShapeType.Filled);
            shapes.setColor(0f, 0f, 0f, 0.6f);
            shapes.rect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
            shapes.end();
        }

        batch.begin();
        font.getData().setScale(0.75f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Next", PANEL_X, 690);
        font.draw(batch, "Score " + board.getScore(), PANEL_X, 480);
        font.draw(batch, "Level " + board.getLevel(), PANEL_X, 430);
        font.draw(batch, "Lines " + board.getLines(), PANEL_X, 380);
        font.getData().setScale(0.45f);
        font.setColor(0.7f, 0.75f, 0.9f, 1f);
        font.draw(batch, "Arrows: move\nUp: rotate\nDown: faster\nSpace: drop\nP: pause\n\nTap the board\nsides, middle,\nor bottom", PANEL_X, 300);
        if (board.isGameOver() || paused) {
            font.getData().setScale(1f);
            font.setColor(Color.WHITE);
            font.draw(batch, board.isGameOver() ? "Game over" : "Paused", 0, BOARD_HEIGHT / 2f + 40, BOARD_WIDTH, 1, false);
            font.getData().setScale(0.55f);
            font.draw(batch, board.isGameOver() ? "Press Enter or tap to play again" : "Press P or tap to carry on",
                    0, BOARD_HEIGHT / 2f - 10, BOARD_WIDTH, 1, false);
        }
        batch.end();
    }

    private void cell(int id, float x, float y, float size) {
        shapes.setColor(SquareColors.getColorByID(id));
        shapes.rect(x + 1, y + 1, size - 2, size - 2);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }
}
