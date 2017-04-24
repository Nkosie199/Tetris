package com.mygdx.tetris;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * @author Nkosingiphile
 */
public class Board {
    private int[][] grid = new int[21][10];
    private Shape shape;
    public static int width = 10;
    public static int height = 10;
    private int levercounter = 0;
    
    private static int elapsed=0;
    private static int moveDelay = 50; //move delay in ms
    
    public Board(){
        for (int i=0; i<21; i++)
            for (int j=0;j<10;j++)
                this.grid[i][j]=0;
        
        this.shape = new Shape(true);    
    }
    
    public void render(Tetris t, ShapeRenderer square){
        //this.shape = t.shape;
        this.boardRender(square);
        this.shapeRender(square);    
    }
    
    public boolean update(int u){
        //printGrid();
        elapsed += 1;
        if (elapsed>=moveDelay){
            elapsed=0;
            //this is where we can print updates ... 
            printGrid();    
        }    
        if(this.canMoveDown()){
            this.shape.update(u); 
        } 
        else{
            //System.out.println("cant move down!");
            if(!this.checkFirstLine())
                return false;
            this.setShapeToBoard();
            this.checkLines();
            this.shape = new Shape(false);
        }
        return true;
        
    }
    
    private void checkLines(){
        for (int i = grid.length-1;i>0 ; i--) {
            if(checkOneLine(this.grid[i])){
                this.deleteOneLine(i);
                i++;
            }
        }
    }
    
    private boolean checkFirstLine(){
        int[] line = this.grid[0];
        for (int i = 0; i < line.length; i++) {
            if(line[i]!=0){
                return false;
            }
        }
        return true;
    }
    
    private boolean checkOneLine(int[] line){
        for (int i = 0; i < line.length; i++) {
            if(line[i] == 0){
                return false;                
            }
        }
        return true;
    }
    
    private void deleteOneLine(int line){
        for (int i = line; i > 0; i--) {
            this.grid[i] = this.grid[i-1];
        }
        for (int i=0; i <this.grid[0].length;i++){
            this.grid[0][i] =0;
        }
        UI.addScore(50);
        levelCounter();
    }
    
    //counts down to when to change the level
    private void levelCounter(){
        this.levercounter += 1;
        if (this.levercounter == 10){
            UI.addLevel();
            this.levercounter = 0;
        }
        
    }
    
    //paints at bottom of the board/grid
    private void boardRender(ShapeRenderer square){
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 10; j++) {
                if (this.grid[i][j]!=0) {
                    square.begin(ShapeType.Filled);
                    square.setColor(SquareColors.getColorByID(this.grid[i][j]));
                    square.rect((j*35),700-(i*35), 35, 35);
                    square.end();
                                   
                }
            }
        }
        printGrid();
    }
    
    //paints a shape at the index of get board position
    private void shapeRender(ShapeRenderer square){
        int[][] shape = this.shape.getShape();
        int dx = this.shape.getBoardPos()[1]*35;
        int dy = this.shape.getBoardPos()[0]*35;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if(shape[i][j]!=0){
                    square.begin(ShapeType.Filled);
                    square.setColor(SquareColors.getColorByID(shape[i][j]));
                    square.rect((j*35 +dx),700-(i*35 +dy), 35, 35);
                    square.end();                    
                }
            }
        }
    }
    
    private boolean canMoveDown(){
        if((this.shape.getBoardPos()[0]+this.shape.getShape().length)*35 >= 735){
            return false;
        } 
        int[][] newshape = this.shape.getShape();
        for (int i = 0; i < newshape.length; i++) {
            for (int j = 0; j < newshape[0].length; j++) {
                if(newshape[i][j]!=0){
                    if(this.grid[this.shape.getBoardPos()[0]+i+1][this.shape.getBoardPos()[1]+j] != 0)
                        return false;
                }       
            }
        }
        return true;
    }
    
    private void setShapeToBoard(){
        int[][] newshape = this.shape.getShape();
        for (int i = 0; i < newshape.length; i++) {
            for (int j = 0; j < newshape[0].length; j++) {
                if(newshape[i][j]!=0)
                this.grid[this.shape.getBoardPos()[0]+i][this.shape.getBoardPos()[1]+j] = newshape[i][j];
            }
        }
    }  
    
    public Shape getShape(){
        return shape;
    }
    
    public int[][] getGrid(){
        return grid;
    }
    
    public void printGrid(){

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                System.out.print(this.grid[i][j]+" ");                
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
}
