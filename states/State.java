package com.oldmansmarch.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.ui.Ui;

public abstract class State{
	
	public SpriteBatch batch;
	public GameManager bigBoss;
	
	public State(GameManager bigBoss){
		this.bigBoss=bigBoss;
		batch=new SpriteBatch();
	}
	public void render(){
		
	}
	public void dispose(){
		this.batch.dispose();
	}
	public void resize(int width, int height) {
		
	}

	public void pause() {
		
	}
	public void resume() {
		
	}
	public void changeMenu(Ui newUi) {
		// TODO Auto-generated method stub
		
	}
	
}
