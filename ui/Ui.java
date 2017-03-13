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
	
	//Size variables
	//Labels
	protected float largeTextScaleX;
	protected float mediumTextScaleX;
	protected float smallTextScaleX;
	protected float largeTextScaleY;
	protected float mediumTextScaleY;
	protected float smallTextScaleY;
	
	//Buttons
	protected float largeButtonWidth;
	protected float largeButtonHeight;
	protected float mediumButtonWidth;
	protected float mediumButtonHeight;
	
	
	public Ui(final State masterState,SpriteBatch batch){
		stage=new Stage(new ScreenViewport(),batch);
		this.masterState=masterState;
		
		//Labels
		largeTextScaleX=Gdx.graphics.getWidth()/200f;
		largeTextScaleY=Gdx.graphics.getHeight()/150f;
		mediumTextScaleX=Gdx.graphics.getWidth()/400f;
		mediumTextScaleY=Gdx.graphics.getHeight()/300f;
		smallTextScaleX=Gdx.graphics.getWidth()/800f;
		smallTextScaleY=Gdx.graphics.getHeight()/600f;
		
		largeButtonWidth=Gdx.graphics.getWidth()/5f;
		largeButtonHeight=Gdx.graphics.getHeight()/10f;
		mediumButtonWidth=Gdx.graphics.getWidth()/10f;
		mediumButtonHeight=Gdx.graphics.getHeight()/10f;
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
	}
}
