package com.mygdx.tetris;

import java.util.Random;

/**
 * The game's rules, with no drawing or input: the grid of settled blocks, the falling piece, and score and level.
 * Row 0 is the top of the board.
 * @author Nkosingiphile
 */
public class Board {
    public static final int ROWS = 21;
    public static final int COLS = 10;
    static final int POINTS_PER_LINE = 50;
    static final int LINES_PER_LEVEL = 10;
    static final float SOFT_DROP_SECONDS = 0.05f;
    private static final int[] KICKS = {0, -1, 1, -2, 2, -3, 3};

    private final int[][] grid = new int[ROWS][COLS];
    private final Random random;
    private int[][] piece;
    private int pieceRow;
    private int pieceCol;
    private int nextIndex;
    private float fallTimer;
    private int score;
    private int level = 1;
    private int lines;
    private boolean gameOver;

    public Board() {
        this(new Random());
    }

    Board(Random random) {
        this.random = random;
        nextIndex = random.nextInt(Tetronimoes.tetronimoes.length);
        spawn();
    }

    /** Lets the piece fall with time; a piece that can't fall any further settles and the next one appears. */
    public void update(float delta, boolean softDrop) {
        if (gameOver) {
            return;
        }
        fallTimer += delta;
        float interval = softDrop ? Math.min(SOFT_DROP_SECONDS, fallInterval()) : fallInterval();
        while (fallTimer >= interval && !gameOver) {
            fallTimer -= interval;
            stepDown();
        }
    }

    /** Seconds per row: 0.8 at level 1, a little faster each level, never quicker than a soft drop. */
    float fallInterval() {
        return Math.max(SOFT_DROP_SECONDS, 0.8f * (float) Math.pow(0.85, level - 1));
    }

    public boolean moveLeft() {
        return tryMove(pieceRow, pieceCol - 1);
    }

    public boolean moveRight() {
        return tryMove(pieceRow, pieceCol + 1);
    }

    /** Rotates clockwise, nudging sideways off a wall or block if needed; stays put when no position fits. */
    public boolean rotate() {
        if (gameOver) {
            return false;
        }
        int[][] turned = rotated(piece);
        for (int kick : KICKS) {
            if (!collides(turned, pieceRow, pieceCol + kick)) {
                piece = turned;
                pieceCol += kick;
                return true;
            }
        }
        return false;
    }

    /** Drops the piece straight to where it lands and settles it. */
    public void hardDrop() {
        if (gameOver) {
            return;
        }
        while (!collides(piece, pieceRow + 1, pieceCol)) {
            pieceRow++;
        }
        settle();
    }

    private void stepDown() {
        if (!tryMove(pieceRow + 1, pieceCol)) {
            settle();
        }
    }

    private boolean tryMove(int row, int col) {
        if (gameOver || collides(piece, row, col)) {
            return false;
        }
        pieceRow = row;
        pieceCol = col;
        return true;
    }

    private void settle() {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    grid[pieceRow + i][pieceCol + j] = piece[i][j];
                }
            }
        }
        clearLines();
        fallTimer = 0;
        spawn();
    }

    private void clearLines() {
        int cleared = 0;
        for (int row = ROWS - 1; row >= 0; row--) {
            if (isFull(grid[row])) {
                // Rows move down by copying values: sharing one array between two rows made later blocks appear twice.
                for (int r = row; r > 0; r--) {
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, COLS);
                }
                grid[0] = new int[COLS];
                cleared++;
                row++;
            }
        }
        if (cleared > 0) {
            score += cleared * POINTS_PER_LINE;
            lines += cleared;
            level = 1 + lines / LINES_PER_LEVEL;
        }
    }

    private static boolean isFull(int[] row) {
        for (int cell : row) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    private void spawn() {
        piece = copy(Tetronimoes.tetronimoes[nextIndex]);
        nextIndex = random.nextInt(Tetronimoes.tetronimoes.length);
        pieceRow = 0;
        pieceCol = (COLS - piece[0].length) / 2;
        if (collides(piece, pieceRow, pieceCol)) {
            gameOver = true;
        }
    }

    boolean collides(int[][] shape, int row, int col) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                if (r < 0 || r >= ROWS || c < 0 || c >= COLS || grid[r][c] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    static int[][] rotated(int[][] shape) {
        int[][] turned = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                turned[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        return turned;
    }

    private static int[][] copy(int[][] shape) {
        int[][] out = new int[shape.length][];
        for (int i = 0; i < shape.length; i++) {
            out[i] = new int[shape[i].length];
            System.arraycopy(shape[i], 0, out[i], 0, shape[i].length);
        }
        return out;
    }

    public int cell(int row, int col) {
        return grid[row][col];
    }

    public int[][] getPiece() {
        return piece;
    }

    public int getPieceRow() {
        return pieceRow;
    }

    public int getPieceCol() {
        return pieceCol;
    }

    public int[][] getNextPiece() {
        return Tetronimoes.tetronimoes[nextIndex];
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLines() {
        return lines;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    void setCell(int row, int col, int value) {
        grid[row][col] = value;
    }
}
