package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Nkosingiphile
 */
public class SquareColors {
    public static Color getColorByID(int id){
        switch(id){
            case 0: return new Color(Color.BLACK);
            case 1: return new Color(Color.YELLOW);
            case 2: return new Color(Color.BLUE);
            case 3: return new Color(Color.RED);
            case 4: return new Color(Color.NAVY);
            case 5: return new Color(Color.GREEN);
            case 6: return new Color(Color.ORANGE);
            case 7: return new Color(Color.CYAN);
            default: return new Color(Color.WHITE);
        }
    }
}
