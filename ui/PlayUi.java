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
import com.oldmansmarch.entities.UnitTemplate;
import com.oldmansmarch.states.MainMenuState;
import com.oldmansmarch.states.PlayState;
import com.oldmansmarch.states.State;

public class PlayUi extends Ui{
	Table table;
	Table rootTable;
	Table gameScreen;
	Label scoreLabel;
	Label goldLabel;
	Label healthLabel;
	TextButton overButton;
	Texture panelImage;
	Stack highlightedButton;
	ArrayList<Stack> spawnButtons;
	public Table getTable(){
		return this.table;
	}
	DecimalFormat df;
	public PlayUi(final PlayState masterState, SpriteBatch batch,float gameWorldWidth,float gameWorldHeight) {
		super(masterState, batch);
		defaultSkin();
		
		//Label format
		df=new DecimalFormat("#");
		
		
		//Button Group
		this.spawnButtons=new ArrayList<Stack>();
		
		//Pause Button
		final TextButton pauseButton=new TextButton("Pause",skin,"default");
		pauseButton.setWidth(mediumButtonWidth);
		pauseButton.setHeight(mediumButtonHeight);
		pauseButton.getLabel().setFontScale(smallTextScaleX, smallTextScaleY);;
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
		exitButton.getLabel().setFontScale(smallTextScaleX, smallTextScaleY);
		exitButton.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				masterState.bigBoss.changeState(new MainMenuState(masterState.bigBoss));
			}
		});
		
		//Over Button
		overButton=new TextButton("Game Over",skin,"default");
		overButton.getLabel().setFontScale(smallTextScaleX, smallTextScaleY);
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
		scoreLabel.setFontScale(this.mediumTextScaleX,this.mediumTextScaleY);
		
		goldLabel=new Label("Gold:",skin);
		goldLabel.setFontScale(mediumTextScaleX,mediumTextScaleY);
	
		healthLabel=new Label("Health:",skin);
		healthLabel.setFontScale(mediumTextScaleX,mediumTextScaleY);
		//Stack
		Stack stack=new Stack();

		
		rootTable=new Table().align(Align.bottom);
		rootTable.setFillParent(true);
		
		//Hud Table
		table=new Table();
		table.add(createSpawnButton(0,masterState))
			.padRight(10).width(mediumButtonWidth).height(mediumButtonHeight);
		table.padRight(100);
		table.add(createSpawnButton(1,masterState)).padRight(10).width(mediumButtonWidth).height(mediumButtonHeight);
		table.add(createSpawnButton(2,masterState)).padRight(10).width(mediumButtonWidth).height(mediumButtonHeight);
		table.add(pauseButton).padRight(10).width(mediumButtonWidth).height(mediumButtonHeight);
		table.add(exitButton).width(mediumButtonWidth).height(mediumButtonHeight);
		//table.add(goldLabel);
		//table.setBackground(skin.getDrawable("default-pane"));
		table.setWidth(Gdx.graphics.getWidth());
		
		//GameScreen (Creating a table on top of the hud so that user can click on it, spawn soldiers)
		gameScreen=new Table();
		gameScreen.top();
		gameScreen.setTouchable(Touchable.enabled);
		gameScreen.add(healthLabel).pad(30);
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
	public Stack createSpawnButton(int unitTemplateId, final PlayState masterState){
		//spawn button is a stack because i want to show the cost of the unit too
		
		final Stack stack=new Stack();
		final UnitTemplate template=masterState.getEntityManager().unitTemplates.get(unitTemplateId);
		final Image button=new Image(template.getPreview());
		button.setWidth(mediumButtonWidth);
		button.setHeight(mediumButtonHeight);
		//this.buttonGroup.add(button);
		stack.add(button);
		
		//Cost text
		Table textTable=new Table(); //Needed to position the text
		textTable.bottom();
		Label text=new Label("Cost:"+df.format(template.getCost()),skin);
		text.setFontScale(smallTextScaleX, smallTextScaleY);
		textTable.add(text);
		textTable.setFillParent(true);
		
		stack.addCaptureListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				//masterState.getEntityManager().createEntity(type, masterState.getWorld(), masterState.spawnPoint);
				masterState.playerCom.setCurrentType(template);
				highlightedButton=stack;
			}
		});
		stack.add(textTable);
		this.spawnButtons.add(stack);
		return stack;
	}
	public void update(){
		for(int i=0;i<spawnButtons.size();i++){
			//First child of the stack(spawn button) is the image
			spawnButtons.get(i).getChildren().get(0).setColor(Color.WHITE);
		}
		if(highlightedButton!=null){
			//First child of the stack(spawn button) is the image
			highlightedButton.getChildren().get(0).setColor(Color.GREEN);
		}

		scoreLabel.setText("Score:"+df.format(((PlayState) masterState).playerCom.getScore()));
		goldLabel.setText("Gold:"+df.format(((PlayState)masterState).playerCom.getGold()));
		healthLabel.setText("Health:"+df.format(((PlayState)masterState).playerCom.getHealth()));
		
	}
	public void gameOver(){
		//Game Over
		gameScreen.row();
		gameScreen.add(overButton).width(largeButtonWidth).height(largeButtonHeight).colspan(3).padTop(Gdx.graphics.getHeight()/5f);
	}
	public void dispose(){
		super.dispose();
		this.panelImage.dispose();
	}
}
