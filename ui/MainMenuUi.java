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
	
	private float buttonWidth;
	private float buttonHeight;
	Table table;
	public MainMenuUi(final State masterState,SpriteBatch batch) {
		super(masterState,batch);
		buttonWidth=this.stage.getWidth()/5f;
		buttonHeight=this.stage.getHeight()/15f;
		float pad=this.stage.getHeight()/15f;
		//Default Skin
		defaultSkin();
		
		//.......Start Button.........
		final TextButton startButton=new TextButton("Start",this.skin);
		startButton.getLabel().setFontScale(1.5f, 1.2f);
		startButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.bigBoss.changeState(new PlayState(masterState.bigBoss));
			}
		});
	
		
		//.......Options Button......
		final TextButton optionsButton=new TextButton("Options",this.skin);
		optionsButton.getLabel().setFontScale(1.5f, 1.2f);
		optionsButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.changeMenu(new OptionsUi(masterState,masterState.batch));
			}
		});
		
		//......Exit Button........
		final TextButton exitButton=new TextButton("Exit",this.skin);
		exitButton.getLabel().setFontScale(1.5f, 1.2f);
		exitButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				Gdx.app.exit();
			}
		});
		
		table=new Table();
		table.setFillParent(true);
		table.add(startButton).width(buttonWidth).height(buttonHeight).padBottom(pad);
		table.row();
		table.add(optionsButton).width(buttonWidth).height(buttonHeight).padBottom(pad);
		table.row();
		table.add(exitButton).width(buttonWidth).height(buttonHeight).padBottom(pad);
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
