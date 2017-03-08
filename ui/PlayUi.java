package com.oldmansmarch.ui;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.states.MainMenuState;
import com.oldmansmarch.states.PlayState;
import com.oldmansmarch.states.State;

public class PlayUi extends Ui{
	Table table;
	Table rootTable;
	Table gameScreen;
	ButtonGroup buttonGroup;
	float buttonWidth;
	float buttonHeight;
	Label scoreLabel;
	Label goldLabel;
	Label healthLabel;
	TextButton overButton;
	public Table getTable(){
		return this.table;
	}
	DecimalFormat df;
	public PlayUi(final PlayState masterState, SpriteBatch batch,float gameWorldWidth,float gameWorldHeight) {
		super(masterState, batch);
		defaultSkin();
		
		//Label format
		df=new DecimalFormat("#");
		
		buttonWidth=stage.getWidth()/10f;
		buttonHeight=stage.getHeight()/10f;
		
		//ImageButton imgBut=new ImageButton();
		
		//Button Group
		buttonGroup=new ButtonGroup();
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(0);
		buttonGroup.setUncheckLast(true);
		
		//Pause Button
		final TextButton pauseButton=new TextButton("Pause",skin,"default");
		pauseButton.setWidth(buttonWidth);
		pauseButton.setHeight(buttonHeight);
		pauseButton.getLabel().setFontScale(1.3f);
		pauseButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				if(!masterState.isPaused()){
					System.out.println("Pause");
					masterState.setPaused(true);
					pauseButton.setText("Resume");
				}else{
					System.out.println("Resume");
					masterState.setPaused(false);
					pauseButton.setText("Pause");
				}

			}
		});
		
		//Exit Button(Back to Main Menu)
		TextButton exitButton=new TextButton("Exit",skin,"default");
		exitButton.setWidth(buttonWidth);
		exitButton.setHeight(buttonHeight);
		exitButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.bigBoss.changeState(new MainMenuState(masterState.bigBoss));
			}
		});
		
		//Over Button
		overButton=new TextButton("Game Over",skin,"default");
		overButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.bigBoss.changeState(new MainMenuState(masterState.bigBoss));
			}
		});
		
		//Back Panel
		Image panel=new Image(skin.getDrawable("default-pane"));
		
		//Labels
		scoreLabel=new Label("Score:",skin);
		scoreLabel.setFontScale(1.5f);
		
		goldLabel=new Label("Gold:",skin);
		goldLabel.setFontScale(1.5f,1.5f);
	
		healthLabel=new Label("Health:",skin);
		healthLabel.setFontScale(1.5f);
		//Stack
		Stack stack=new Stack();

		
		rootTable=new Table().align(Align.bottom);
		rootTable.setFillParent(true);
		
		//Hud Table
		table=new Table();
		table.add(createSpawnButton("Test",EntityManager.UnitType.TEST,masterState))
			.padRight(10).width(buttonWidth).height(buttonHeight);
		table.padRight(100);
		table.add(createSpawnButton("Wizard",EntityManager.UnitType.WIZARD,masterState)).padRight(10).width(buttonWidth).height(buttonHeight); //Zombie Button
		table.add(pauseButton).padRight(10).width(buttonWidth).height(buttonHeight);
		table.add(exitButton).width(buttonWidth).height(buttonHeight);
		table.add(goldLabel);
		//table.setBackground(skin.getDrawable("default-pane"));
		table.setWidth(Gdx.graphics.getWidth());
		
		//GameScreen (Creating a table on top of the hud so that user can click on it, spawn soldiers)
		gameScreen=new Table();
		gameScreen.setTouchable(Touchable.enabled);
		gameScreen.top().left();
		gameScreen.add(healthLabel).pad(30);
		gameScreen.top().right();
		gameScreen.add(scoreLabel).pad(30);
		gameScreen.addListener(new ClickListener(){
	        @Override
	        public void clicked(InputEvent event, float x, float y) { //origin is top left according to stage2d
	        	//Give the clicked coordinates to the commander and let him decide where to spawn
	        	if(!masterState.isPaused() && !masterState.isOver){
	        		Vector3 clicked=masterState.getCamera().unproject(new Vector3(x,y,0));
	        		masterState.playerCom.spawn(masterState.getEntityManager(), masterState.getWorld(), 
	        			new Vector2(clicked.x,Configuration.gameWorldHeight-clicked.y));
	        	}
	        	
	        }
		});
		
		stack.add(panel);
		stack.add(table);
		rootTable.add(gameScreen).expand().fill();
		rootTable.row();
		rootTable.add(stack).fillX().align(Align.bottom);
		this.stage.addActor(rootTable);
	}
	/*public TextButton createSpawnButton(String text,final EntityManager.EntityType type,final PlayState masterState){
		TextButton button=new TextButton(text,skin,"default");
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.setBackground((Drawable) masterState.getEntityManager().textures.get(type));
		button.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				//masterState.getEntityManager().createEntity(type, masterState.getWorld(), masterState.spawnPoint);
				masterState.playerCom.setCurrentType(type);
			}
		});
		this.buttonGroup.add(button);
		return button;
	}*/
	public Image createSpawnButton(String text,final EntityManager.UnitType type,final PlayState masterState){
		Image button=new Image( new TextureRegion(masterState.getEntityManager().textures.get(type),0,0,32,32));
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				//masterState.getEntityManager().createEntity(type, masterState.getWorld(), masterState.spawnPoint);
				masterState.playerCom.setCurrentType(type);
			}
		});
		//this.buttonGroup.add(button);
		return button;
	}
	public void update(){
		for(int i=0;i<this.buttonGroup.getButtons().size;i++){
			TextButton button=(TextButton) buttonGroup.getButtons().get(i);
			button.setColor(Color.WHITE);
		}
		if(this.buttonGroup.getChecked()!=null){
			this.buttonGroup.getChecked().setColor(Color.LIGHT_GRAY);
		}
		scoreLabel.setText("Score:"+df.format(((PlayState) masterState).playerCom.getScore()));
		goldLabel.setText("Gold:"+df.format(((PlayState)masterState).playerCom.getGold()));
		healthLabel.setText("Health:"+df.format(((PlayState)masterState).playerCom.getHealth()));
		
		if(((PlayState)masterState).isOver){
			//Game Over
			gameScreen.add(overButton).width(buttonWidth).height(buttonHeight);
		}
	}
}
