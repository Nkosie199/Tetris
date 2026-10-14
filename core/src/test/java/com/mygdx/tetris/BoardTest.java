package com.mygdx.tetris;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class BoardTest {
    private static final int SQUARE = 0;
    private static final int LINE = 1;
    private static final int T = 4;

    /** Always deals the same piece, so each test knows what falls next. */
    private static Board boardDealing(int piece) {
        return new Board(new Random() {
            @Override
            public int nextInt(int bound) {
                return piece;
            }
        });
    }

    private static void fillRow(Board board, int row, int gapCol) {
        for (int col = 0; col < Board.COLS; col++) {
            board.setCell(row, col, col == gapCol ? 0 : 9);
        }
    }

    private static int filledCells(Board board) {
        int count = 0;
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                count += board.cell(row, col) != 0 ? 1 : 0;
            }
        }
        return count;
    }

    @Test
    void clearingALineNeverLeavesTwoRowsSharingCells() {
        Board board = boardDealing(LINE);
        fillRow(board, Board.ROWS - 1, 4);
        board.hardDrop();

        assertEquals(1, board.getLines());
        board.setCell(0, 0, 7);
        assertEquals(0, board.cell(1, 0), "a block written to the top row must not also appear in the row below");
        for (int row = 0; row < Board.ROWS - 1; row++) {
            board.setCell(row, 9, 0);
        }
        board.setCell(Board.ROWS - 1, 9, 5);
        assertEquals(0, board.cell(Board.ROWS - 2, 9));
    }

    @Test
    void piecesStackWithoutOverlapAfterManyLineClears() {
        Board board = boardDealing(LINE);
        for (int round = 0; round < 30; round++) {
            fillRow(board, Board.ROWS - 1, 4);
            fillRow(board, Board.ROWS - 2, 4);
            fillRow(board, Board.ROWS - 3, 4);
            fillRow(board, Board.ROWS - 4, 4);
            int before = filledCells(board);
            board.hardDrop();
            assertEquals(before + 4 - 40, filledCells(board), "round " + round);
        }
        assertEquals(120, board.getLines());
        assertFalse(board.isGameOver());
    }

    @Test
    void sidewaysMovesStopAtSettledBlocksAndWalls() {
        Board board = boardDealing(SQUARE);
        int col = board.getPieceCol();
        board.setCell(0, col - 1, 9);
        assertFalse(board.moveLeft());
        assertEquals(col, board.getPieceCol());
        for (int i = 0; i < 20; i++) {
            board.moveRight();
        }
        assertEquals(Board.COLS - 2, board.getPieceCol());
        assertFalse(board.moveRight());
    }

    @Test
    void rotationNudgesOffAWallAndRefusesWhenNothingFits() {
        Board board = boardDealing(LINE);
        for (int i = 0; i < 10; i++) {
            board.moveRight();
        }
        assertTrue(board.rotate());
        assertEquals(1, board.getPiece().length);
        assertEquals(Board.COLS - 4, board.getPieceCol(), "kicked left to fit against the right wall");

        Board blocked = boardDealing(LINE);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (col != blocked.getPieceCol()) {
                    blocked.setCell(row, col, 9);
                }
            }
        }
        assertFalse(blocked.rotate());
        assertEquals(4, blocked.getPiece().length);
    }

    @Test
    void rotatingAtTheFloorNeverPushesAPieceOutOfTheBoard() {
        Board board = boardDealing(T);
        for (int col = 0; col < Board.COLS; col++) {
            for (int r = 0; r < 4; r++) {
                board.rotate();
                board.update(10f, false);
            }
            board.hardDrop();
        }
        assertTrue(filledCells(board) > 0);
    }

    @Test
    void piecesFallOneRowPerIntervalAndFasterWhenHeldDown() {
        Board board = boardDealing(SQUARE);
        board.update(0.79f, false);
        assertEquals(0, board.getPieceRow());
        board.update(0.02f, false);
        assertEquals(1, board.getPieceRow());
        board.update(Board.SOFT_DROP_SECONDS * 3, true);
        assertEquals(4, board.getPieceRow());
    }

    @Test
    void scoreAndLevelGoUpWithClearedLines() {
        Board board = boardDealing(LINE);
        for (int round = 0; round < 3; round++) {
            for (int r = 1; r <= 4; r++) {
                fillRow(board, Board.ROWS - r, 4);
            }
            board.hardDrop();
        }
        assertEquals(12, board.getLines());
        assertEquals(12 * Board.POINTS_PER_LINE, board.getScore());
        assertEquals(2, board.getLevel());
        assertTrue(board.fallInterval() < 0.8f);
    }

    @Test
    void theGameEndsWhenANewPieceHasNoRoomAndThenIgnoresInput() {
        Board board = boardDealing(SQUARE);
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (col != 0) {
                    board.setCell(row + 2, col, 9);
                }
            }
        }
        board.hardDrop();
        assertTrue(board.isGameOver());
        int[][] piece = board.getPiece();
        assertFalse(board.moveLeft());
        assertFalse(board.rotate());
        board.update(10f, true);
        assertArrayEquals(piece, board.getPiece());
    }
}
