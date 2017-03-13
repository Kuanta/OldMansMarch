package com.oldmansmarch.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.oldmansmarch.states.State;

public class OptionsUi extends Ui{

	public OptionsUi(final State masterState,SpriteBatch batch) {
		super(masterState,batch);
		defaultSkin();
		
		float pad=this.stage.getHeight()/15f;
		final Preferences prefs=Gdx.app.getPreferences("GamePrefs");
		int highestScore=prefs.getInteger("Highscore",0);
		
		//.......Label......
		final Label highScore=new Label("Highest Score:"+highestScore,skin);
		highScore.setFontScale(mediumTextScaleX, mediumTextScaleY);
		
		//......Reset Button......
		final TextButton resetButton=new TextButton("Reset",this.skin);
		resetButton.getLabel().setFontScale(mediumTextScaleX, mediumTextScaleY);
		resetButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				prefs.putInteger("Highscore", 0);
				prefs.flush();
				highScore.setText("Highest Score:0");
			}
		});
		
		//.....Back Button........

		final TextButton backButton=new TextButton("Back",this.skin);
		backButton.getLabel().setFontScale(mediumTextScaleX, mediumTextScaleY);
		backButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.changeMenu(new MainMenuUi(masterState,masterState.batch));
			}
		});
		
		//Table
		Table table=new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(Gdx.graphics.getHeight());
		table.add(highScore).padBottom(pad);
		table.row();
		table.add(resetButton).width(largeButtonWidth).height(largeButtonHeight).padBottom(pad);
		table.row();
		table.add(backButton).width(largeButtonWidth).height(largeButtonHeight).padBottom(pad);
		
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

}
