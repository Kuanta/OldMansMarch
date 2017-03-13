package com.oldmansmarch.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.commanders.EnemyCommander;
import com.oldmansmarch.commanders.PlayerCommander;
import com.oldmansmarch.entities.Entity;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.Projectile;
import com.oldmansmarch.entities.Unit;
import com.oldmansmarch.ui.PlayUi;

public class PlayState extends State implements InputProcessor{
	//Sub states
	public boolean isPaused;
	public boolean isOver;
	
	//Game World Sizes
	final float pixPerUnit=64;
	float gameWorldWidth;
	float gameWorldHeight;
	final float camWidth=20;
	final float camHeight=50;
	private OrthographicCamera camera;

	
	//Ui
	PlayUi ui;
	
	//Spawn Points
	public Vector2 spawnPoint;
	public Vector2 enemySpawnPoint;

	//Sprites
	private Sprite background;

	//Entities
	private EntityManager em;
	
	//Box2D
	World world;
	Box2DDebugRenderer debug;
	
	//Input
	private InputMultiplexer im;
	
	//Commanders
	public PlayerCommander playerCom;
	public EnemyCommander enemyCom;
	
	//Gameplay
	int difficulty;
	int scoreThres=5;
	//Getters
	public OrthographicCamera getCamera(){
		return this.camera;
	}
	public EntityManager getEntityManager(){
		return this.em;
	}
	public World getWorld(){
		return this.world;
	}
	public float getWorldWidth(){
		return this.gameWorldWidth;
	}
	public float getWorldHeight(){
		return this.gameWorldHeight;
	}
	public boolean isPaused(){
		return this.isPaused;
	}
	//Setters
	public void setPaused(boolean paused){
		this.isPaused=paused;
	}
	public PlayState(GameManager bigBoss) {
		super(bigBoss);
		isPaused=false;
		isOver=false;
		
		difficulty=1;
		
		//Camera
		//camera=new OrthographicCamera(Gdx.graphics.getWidth()/pixPerUnit,Gdx.graphics.getWidth()/pixPerUnit);
		float aspectRatio=Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		camera=new OrthographicCamera(Configuration.gameWorldWidth,Configuration.gameWorldHeight);
		/*view=new FitViewport(Gdx.graphics.getWidth()/pixPerUnit,Gdx.graphics.getHeight()/pixPerUnit,camera);
		view.apply();*/
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
		camera.update();
				
		background=new Sprite(new Texture(Gdx.files.internal("grass.png")));
		background.setSize(Configuration.gameWorldWidth, Configuration.gameWorldHeight);
		background.setPosition(0, 0);
		//Entity Manager
		em=new EntityManager();
		
		//Ui
		this.ui=new PlayUi(this,batch,Configuration.gameWorldWidth,Configuration.gameWorldHeight);
		
		
		
		//TestCode
		world=new World(new Vector2(0,0),true);
		world.setContactListener(new ContactListener(){
			public void beginContact(Contact contact) {
				//All the needed information is in the userData
				Entity entA=(Entity) contact.getFixtureA().getBody().getUserData();
				Entity entB=(Entity) contact.getFixtureB().getBody().getUserData();
				
				//Check if they belong in the same faction
				if(entA.getCommander().getFaction()!=entB.getCommander().getFaction()){
					//Check the types
					switch(entA.getType()){
						case UNIT:
							switch(entB.getType()){
								case UNIT:
									//ENTB:UNIT
									em.combat((Unit)entA,(Unit) entB);
									break;
								case WALL:
									//ENTB:WALL
									//Delete EntA and Damage the wall(B)
									entB.getCommander().damageCommander(((Unit)entA).getDamage());
									em.toDelete.add(entA.getId());
									break;
								case PROJECTILE:
									//ENTB:PROJECTILE
									//doStuff to entB
									em.impact((Projectile)entB, (Unit)entA);
									break;
							}
							break;
						case WALL:
							switch(entB.getType()){
							case UNIT:
								//ENTB:UNIT
								//Delete EntB damage the wall(A)
								entA.getCommander().damageCommander(((Unit)entB).getDamage());
								em.toDelete.add(entB.getId());
								break;
							case WALL:
								//ENTB:WALL
								//Nothing
								break;
							case PROJECTILE:
								//ENTB:PROJECTILE
								//Delete EntB
								em.toDelete.add(entB.getId());
								break;
						}
							break;
						case PROJECTILE:
							switch(entB.getType()){
							case UNIT:
								//ENTB:UNIT
								em.impact((Projectile)entA, (Unit)entB);
								break;
							case WALL:
								//ENTB:WALL
								em.toDelete.add(entA.getId());
								break;
							case PROJECTILE:
								//ENTB:PROJECTILE
								//Nothing
								break;
						}
							break;
					}
				}
				
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
		});
		debug=new Box2DDebugRenderer();
		
		//Spawn Points
		spawnPoint=new Vector2(0,Configuration.gameWorldHeight/2);
		enemySpawnPoint=new Vector2(camera.viewportWidth,spawnPoint.y);
		
		//InputProcessor
		im=new InputMultiplexer();
		im.addProcessor(this.ui.getStage());
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
		
		//Commanders
		playerCom=new PlayerCommander(this.world,5f-Configuration.baseEntityWidth,this.em);
		enemyCom=new EnemyCommander(this.world,Configuration.gameWorldWidth+Configuration.baseEntityWidth,this.em,Configuration.gameWorldHeight/10f);
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		//this.ui.getStage().getViewport().update(width,height,true);
		this.ui.resize(width, height);

	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		this.isPaused=true;
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		this.isPaused=false;
	}
	@Override
	public void render() {
		// TODO Auto-generated method stub
		super.render();
		if(!isPaused && !isOver){
			world.step(1/60f, 6, 2);
			playerCom.update(Gdx.graphics.getDeltaTime());
			enemyCom.update(Gdx.graphics.getDeltaTime(),this);
			em.update(Gdx.graphics.getDeltaTime());
		}
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		camera.update();
		
		this.batch.begin();
		batch.setProjectionMatrix(camera.combined);
		this.background.draw(batch);
		em.draw(batch);
		this.batch.end();
		debug.render(world, camera.combined);
		this.ui.update();
		this.ui.render(Gdx.graphics.getDeltaTime());
		
		em.deleteEntities(world);
		em.toDelete.clear();
		
		//Update Difficulty
		if(playerCom.getScore()>this.scoreThres*this.difficulty){
			this.difficulty++;
			enemyCom.updateSpeed(1f);
			/*if(enemyCom.getSpawnCooldown()>0.1){
				enemyCom.updateSpawnCooldown(-0.1f);
			}*/
		}
		//Check if the game is over
		if(playerCom.getHealth()<=0){
			//Game Over
			gameOver();
			
		}
	}public void gameOver(){
		//Call this once if the game is over
		if(!isOver){
			this.ui.gameOver();
			isOver=true;
		}
		
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		background.getTexture().dispose();
		em.dispose();
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode==Input.Keys.LEFT){
			camera.translate(-1, 0);
		}
		if(keycode==Input.Keys.RIGHT){
			camera.translate(1, 0);
		}
		if(keycode==Input.Keys.SPACE){
			
		}
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		//Vector3 position=camera.unproject(new Vector3((float)screenX,(float)screenY,0f));
		//System.out.println(position);
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
