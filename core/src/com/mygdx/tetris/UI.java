package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Nkosingiphile
 */
public class UI {
    private static int score = 0;
    private static int level = 1;
    private static BitmapFont font = new BitmapFont(Gdx.files.internal("calibri.fnt"),Gdx.files.internal("calibri.png"),false);
    private static String str;
    private static SpriteBatch spriteBatch = new SpriteBatch();


    public static void render(Board board, ShapeRenderer square){
        nextShapeRender(board.getShape().getNextShape(), square);
        spriteBatch.begin();
        font.draw(spriteBatch, "Next Piece", 380, 650);
        spriteBatch.end();
        
        str = "Score: "+String.valueOf(score);
        spriteBatch.begin();
        font.draw(spriteBatch, str, 380, 500);
        spriteBatch.end();
        
        str = "Level: "+String.valueOf(level);
        spriteBatch.begin();
        font.draw(spriteBatch, str, 380, 400);
        spriteBatch.end();
    }
    
    private static void nextShapeRender(int[][] shape, ShapeRenderer square){
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if(shape[i][j] != 0){
                    square.begin(ShapeRenderer.ShapeType.Filled);
                    square.setColor(SquareColors.getColorByID(shape[i][j]));
                    square.rect(380+(j*15), 600-(i*15), 15, 15);
                    square.end();
                }
            }
        }
    }
    
    public static void addScore(int points){
        score += points;
    }
    
    public static void addLevel(){
        level += 1;
        Shape.level++;
    }

}