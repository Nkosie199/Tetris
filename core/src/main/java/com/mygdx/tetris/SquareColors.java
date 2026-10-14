package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Nkosingiphile
 */
public class SquareColors {
    private static final Color[] COLORS = {
        Color.BLACK, Color.YELLOW, Color.CYAN, Color.RED, Color.ROYAL, Color.GREEN, Color.ORANGE, Color.MAGENTA,
    };

    public static Color getColorByID(int id) {
        return id >= 0 && id < COLORS.length ? COLORS[id] : Color.WHITE;
    }
}
