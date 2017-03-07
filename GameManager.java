package com.oldmansmarch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.oldmansmarch.states.MainMenuState;
import com.oldmansmarch.states.State;

public class GameManager {
	final public static float pixPerUnit=32f;
	public State currentState;
	
	public GameManager(){
		currentState=new MainMenuState(this);
	}
	public void render(){
		currentState.render();
		
	}
	public void changeState(State newState){
		currentState.dispose();
		currentState=newState;
	}
	public void dispose(){
		currentState.dispose();
	}
	public void resize(int width,int height){
		this.currentState.resize(width, height);
	}
}
