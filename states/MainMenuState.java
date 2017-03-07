package com.oldmansmarch.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.ui.MainMenuUi;
import com.oldmansmarch.ui.Ui;

public class MainMenuState extends State{
	
	Sprite background;
	public Ui currentUi;
	private InputMultiplexer im;
	
	public MainMenuState(GameManager bigBoss){
		super(bigBoss);
		currentUi=new MainMenuUi(this,this.batch);
		background=new Sprite(new Texture("war.jpg"));
		background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		background.setPosition(0, 0);
		//Setting the Input Processor
		im=new InputMultiplexer();
		im.addProcessor(currentUi.getStage());
		Gdx.input.setInputProcessor(im);
	}
	public void changeMenu(Ui newUi){
		im.removeProcessor(currentUi.getStage());
		currentUi.dispose();
		currentUi=newUi;
		im.addProcessor(currentUi.getStage());
		
	}
	@Override
	public void render() {
		// TODO Auto-generated method stub
		super.render();
		this.batch.begin();
		this.background.draw(batch);
		this.batch.end();
		currentUi.render(Gdx.graphics.getDeltaTime());
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		currentUi.dispose();
		background.getTexture().dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		this.currentUi.resize(width, height);
		this.background.setSize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}

	
}
