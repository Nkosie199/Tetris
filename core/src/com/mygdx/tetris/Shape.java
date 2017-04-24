package com.mygdx.tetris;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

/**
 * @author Nkosingiphile
 */
public class Shape {
    private int[][] shape;
    static int level=1;
    //board position of the shape where [0] --> horizontal & [1] --> vertical 
    private int[] boardPos = new int[2];
    
    private static int[][] nextShape;
    private static int elapsed=0;
    private static int moveDelay = 50; //move delay in ms
    private static Random rand = new Random();
    private int random = rand.nextInt(7); 
    
    public Shape(boolean first){
        //creates shapes           
        if (first){
            this.shape = Tetronimoes.tetronimoes[random];
            this.nextShape = Tetronimoes.tetronimoes[rand.nextInt(7)];
        }else{
            this.shape = this.nextShape.clone();
            this.nextShape = Tetronimoes.tetronimoes[rand.nextInt(7)];
        }
        
        this.boardPos[0] = 0;
        this.boardPos[1] = (int)Board.width/2 - this.shape[0].length/2;
            
    }
    
    public void render(Tetris t, ShapeRenderer s){
        //draws a shape at the bottom of the board (begins as next shape)
        int dx = this.boardPos[1]*35;
        int dy = this.boardPos[0]*35;
        for (int i = 0; i < this.shape.length; i++) {
            for (int j = 0; j < this.shape[0].length; j++) {
                if (this.shape[i][j] != 0){
                    s.begin(ShapeRenderer.ShapeType.Filled);
                    s.setColor(SquareColors.getColorByID(shape[i][j]));
                    //s.rect((j*35 +dx),700-(i*35 +dy), 35, 35);
                    s.end(); 
                }
            }
        }
    }
    
    public void update(int u){
        //moves shapes down over time
        elapsed += u;
        if (elapsed>=moveDelay){
            elapsed=0;
            this.moveDown();
            //this is where we can print updates ... 
            //printShape();      
        }
        setMoveDelay((int)50/level);

    }
    
    public static void setMoveDelay(int moveDelay){
        Shape.moveDelay = moveDelay;
    }
    
    public void rotateShape(){
        int[][] newShape = new int[this.shape[0].length][this.shape.length];
        for (int i = 0; i < this.shape.length; i++) {
            for (int j = 0; j < this.shape[0].length; j++) {
                newShape[j][this.shape.length -1- i] = this.shape[i][j];
            }
        }
        this.shape = newShape;
        if(!this.isOnWindow()){
            if(this.boardPos[1] > 5){
                do{
                    this.moveLeft();
                }while(!this.isOnWindow());
            }
        }
    }
    
    private void printShape(){
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < this.shape[0].length; j++) {
                System.out.print(this.shape[i][j]+" ");
            }
            System.out.println("");
        }
    }
    
    private boolean isOnWindow(){
        boolean b = true;
        for (int i = 0; i < this.shape.length; i++) {
            for (int j = 0; j < this.shape[0].length; j++) {
                if (this.shape[i][j] != 0) {
                    if(this.boardPos[1]*35 + j*35 >= 350){
                        b = false;
                    }
                }
            }
        }
        //System.out.println(b);
        return b;
    }
    
    public int[][] getNextShape(){
        return this.nextShape;
    }

    public void moveLeft(){
        if(this.boardPos[1]>0)
            this.boardPos[1]--;
    }
    
    public void moveRight(){
        if(this.boardPos[1]+this.shape[0].length<10)
            this.boardPos[1]++;
    }
    
    public void moveDown(){
        this.boardPos[0]++;
    }
    
    public int[][] getShape(){
        return shape;
    }
    
    public int[] getBoardPos(){
        return boardPos;
    }
}
