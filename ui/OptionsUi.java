package com.oldmansmarch.ui;

import com.badlogic.gdx.Gdx;
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
		
		//.......Label......
		Label textLabel=new Label("Under Construct",skin);
		
		//.....Back Button........

		final TextButton backButton=new TextButton("Back",this.skin);
		backButton.setWidth(200);
		backButton.setHeight(50);
		backButton.pad(20);
		backButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.changeMenu(new MainMenuUi(masterState,masterState.batch));
			}
		});
		
		//Table
		Table table=new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(Gdx.graphics.getHeight());
		table.add(textLabel);
		table.row();
		table.add(backButton);
		
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
