package com.oldmansmarch.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.states.PlayState;
import com.oldmansmarch.states.State;

public class MainMenuUi extends Ui{
	
	Table table;
	public MainMenuUi(final State masterState,SpriteBatch batch) {
		super(masterState,batch);
		float pad=this.stage.getHeight()/15f;
		//Default Skin
		defaultSkin();
		
		//.......Start Button.........
		final TextButton startButton=new TextButton("Start",this.skin);
		startButton.setWidth(largeButtonWidth);
		startButton.setHeight(largeButtonHeight);
		startButton.getLabel().setFontScale(mediumTextScaleX, mediumTextScaleY);
		startButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.bigBoss.changeState(new PlayState(masterState.bigBoss));
			}
		});
	
		
		//.......Highest Score......
		final TextButton optionsButton=new TextButton("HighScore",this.skin);
		optionsButton.setWidth(largeButtonWidth);
		optionsButton.setHeight(largeButtonHeight);
		optionsButton.getLabel().setFontScale(mediumTextScaleX, mediumTextScaleY);
		optionsButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.changeMenu(new OptionsUi(masterState,masterState.batch));
			}
		});
		
		//......Exit Button........
		final TextButton exitButton=new TextButton("Exit",this.skin);
		exitButton.setWidth(largeButtonWidth);
		exitButton.setHeight(largeButtonHeight);
		exitButton.getLabel().setFontScale(mediumTextScaleX, mediumTextScaleY);
		exitButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				Gdx.app.exit();
			}
		});
		
		table=new Table();
		table.setFillParent(true);
		table.add(startButton).width(largeButtonWidth).height(largeButtonHeight).padBottom(pad);
		table.row();
		table.add(optionsButton).width(largeButtonWidth).height(largeButtonHeight).padBottom(pad);
		table.row();
		table.add(exitButton).width(largeButtonWidth).height(largeButtonHeight).padBottom(pad);
		stage.addActor(table);
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}
	
}
