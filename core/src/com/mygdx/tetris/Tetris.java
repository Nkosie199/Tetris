package com.mygdx.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class Tetris extends Game {
        private Board board;
        static Shape shape;

	ShapeRenderer s;
        Batch batch;
        Pixmap p;
        Tetris tetris;
        boolean gameover = false;
        
	@Override
	public void create () {
            this.board = new Board();
            this.shape = new Shape(true);
            s = new ShapeRenderer();           
            batch = new SpriteBatch();
        }
        //@Override
        public void render(){
            Gdx.gl.glClearColor(0, 0.77f, 0.85f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            controls();
            
            render(tetris,s);
        }
        
	//@Override
	public void render (Tetris t, ShapeRenderer s) {
            s.begin(ShapeType.Filled);
            s.setColor(Color.CLEAR);
            s.rect(0, 0, 370, 735);
            s.end();
            
            this.board.render(t, s);
            this.shape.render(t, s);
            //
            UI.render(board, s);
            update(t,1);
        }
        
        @Override
        public void dispose(){
           
        }
        public void update(Tetris t, int i){
            if (gameover == false)
                board.update(i);
        }
        
        public void controls(){
            //controls...
            //left
            if(Gdx.input.isKeyPressed(Keys.LEFT)) 
                this.board.getShape().moveLeft();
            //right
            if(Gdx.input.isKeyPressed(Keys.RIGHT)) 
                this.board.getShape().moveRight();
            //rotate
            if(Gdx.input.isKeyJustPressed(Keys.UP)) {
                this.board.getShape().rotateShape();
            } 
            //down
            if(Gdx.input.isKeyPressed(Keys.DOWN)) {
                Shape.setMoveDelay(5);
            }
            //Pause
            if(Gdx.input.isKeyPressed(Keys.P)) {
                pause(); //not yet implemented
            }
            //end of controls...
        }

}
