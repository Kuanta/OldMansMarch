package com.oldmansmarch.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
import com.oldmansmarch.AssetsManager;
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
	float labelScaleWidth;
	float labelScaleHeight;
	Texture panelImage;
	Image highlightedButton;
	ArrayList<Image> spawnButtons;
	public Table getTable(){
		return this.table;
	}
	DecimalFormat df;
	public PlayUi(final PlayState masterState, SpriteBatch batch,float gameWorldWidth,float gameWorldHeight) {
		super(masterState, batch);
		defaultSkin();
		
		//Label format
		df=new DecimalFormat("#");
		labelScaleWidth=Gdx.graphics.getWidth()/400f;
		labelScaleHeight=Gdx.graphics.getHeight()/300f;
		
		buttonWidth=stage.getWidth()/10f;
		buttonHeight=stage.getHeight()/10f;
		
		//Button Group
		buttonGroup=new ButtonGroup();
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(0);
		buttonGroup.setUncheckLast(true);
		this.spawnButtons=new ArrayList<Image>();
		
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
		panelImage=new Texture(Gdx.files.internal("woodBackground.png"));
		Image panel=new Image(panelImage);
		//panel.setHeight(Gdx.graphics.getHeight()/10f);
		
		//Labels
		scoreLabel=new Label("Score:",skin);
		scoreLabel.setFontScale(labelScaleWidth,labelScaleHeight);
		
		goldLabel=new Label("Gold:",skin);
		goldLabel.setFontScale(labelScaleWidth,labelScaleHeight);
	
		healthLabel=new Label("Health:",skin);
		healthLabel.setFontScale(labelScaleWidth,labelScaleHeight);
		//Stack
		Stack stack=new Stack();

		
		rootTable=new Table().align(Align.bottom);
		rootTable.setFillParent(true);
		
		//Hud Table
		table=new Table();
		table.add(createSpawnButton(AssetsManager.infantryRect,EntityManager.UnitType.INFANTRY,masterState))
			.padRight(10).width(buttonWidth).height(buttonHeight);
		table.padRight(100);
		table.add(createSpawnButton(AssetsManager.wizardRect,EntityManager.UnitType.WIZARD,masterState)).padRight(10).width(buttonWidth).height(buttonHeight); //Zombie Button
		table.add(createSpawnButton(AssetsManager.mountedRect,EntityManager.UnitType.MOUNTED,masterState)).padRight(10).width(buttonWidth).height(buttonHeight);
		table.add(pauseButton).padRight(10).width(buttonWidth).height(buttonHeight);
		table.add(exitButton).width(buttonWidth).height(buttonHeight);
		//table.add(goldLabel);
		//table.setBackground(skin.getDrawable("default-pane"));
		table.setWidth(Gdx.graphics.getWidth());
		
		//GameScreen (Creating a table on top of the hud so that user can click on it, spawn soldiers)
		gameScreen=new Table();
		gameScreen.setTouchable(Touchable.enabled);
		gameScreen.top().left();
		gameScreen.add(healthLabel).pad(30);
		gameScreen.top().right();
		gameScreen.add(scoreLabel).pad(30);
		gameScreen.add(goldLabel).pad(30);
		gameScreen.addListener(new ClickListener(){
	        @Override
	        public void clicked(InputEvent event, float x, float y) { //origin is top left according to stage2d
	        	//Give the clicked coordinates to the commander and let him decide where to spawn
	        	if(!masterState.isPaused() && !masterState.isOver){
	        		Vector3 clicked=masterState.getCamera().unproject(new Vector3(x,y,0));
	        		masterState.playerCom.spawn(masterState.getEntityManager(), masterState.getWorld(), 
	        			new Vector2(clicked.x,(Configuration.gameWorldHeight-clicked.y)+Configuration.baseEntityHeight/2f));
	        	}
	        	
	        }
		});
		
		stack.add(panel);
		stack.add(table);
		rootTable.add(gameScreen).expand().fill();
		rootTable.row();
		rootTable.add(stack).fillX().align(Align.bottom).height(Gdx.graphics.getHeight()/10f);
		this.stage.addActor(rootTable);
	}
	public Image createSpawnButton(Rectangle rect, final EntityManager.UnitType type, final PlayState masterState){
		TextureRegion region=new TextureRegion(masterState.getEntityManager().getUnitTexture(type),(int)rect.x,(int)rect.y,(int)rect.width,(int)rect.height);
		final Image button=new Image(region);
		System.out.println("X:"+rect.getX()+" Y:"+rect.getY());
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				//masterState.getEntityManager().createEntity(type, masterState.getWorld(), masterState.spawnPoint);
				masterState.playerCom.setCurrentType(type);
				highlightedButton=button;
			}
		});
		//this.buttonGroup.add(button);
		this.spawnButtons.add(button);
		return button;
	}
	public void update(){
		for(int i=0;i<spawnButtons.size();i++){
			spawnButtons.get(i).setColor(Color.WHITE);
		}
		if(highlightedButton!=null){
			highlightedButton.setColor(Color.GREEN);
		}

		scoreLabel.setText("Score:"+df.format(((PlayState) masterState).playerCom.getScore()));
		goldLabel.setText("Gold:"+df.format(((PlayState)masterState).playerCom.getGold()));
		healthLabel.setText("Health:"+df.format(((PlayState)masterState).playerCom.getHealth()));
		
		if(((PlayState)masterState).isOver){
			//Game Over
			gameScreen.add(overButton).width(buttonWidth).height(buttonHeight);
		}
	}
	public void dispose(){
		super.dispose();
		this.panelImage.dispose();
	}
}
