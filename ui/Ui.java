package com.oldmansmarch.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.states.State;

public class Ui {

	protected Stage stage;
	protected Skin skin;
	protected State masterState; //This is the state where the ui belongs
	
	public Ui(final State masterState,SpriteBatch batch){
		stage=new Stage(new ScreenViewport(),batch);
		this.masterState=masterState;
	}
	public void render(float delta){
		this.stage.act(delta);
		this.stage.draw();
	}
	public void dispose(){
		this.stage.dispose();
		this.skin.dispose();
	}
	public void defaultSkin(){
		skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
	}
	
	//Getters
	public Stage getStage(){
		return this.stage;
	}
	public void resize(int width,int height){
		this.stage.getViewport().update(width,height,true);
		System.out.println("Resized");
	}
}
