package com.oldmansmarch.commanders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.EntityManager.EntityType;

public abstract class Commander {
	protected float spawnCooldown=0.2f;
	protected float spawnTimer;
	protected boolean onCooldown;
	protected float health=1f;
	protected EntityManager.EntityType currentType;
	protected float posX; //This is the position in horizontal axis from which the units going to spawn
	protected EntityManager.Faction faction;
	protected float gold;
	protected float score;
	protected float baseSpeed;
	protected int direction;
	
	//Getters
	public float getHealth(){
		return health;
	}
	public void damageCommander(int damage){
		this.health-=damage;
	}
	public void setCurrentType(EntityManager.EntityType type){
		this.currentType=type;
	}
	public EntityManager.Faction getFaction(){
		return this.faction;
	}
	public float getGold(){
		return this.gold;
	}
	public float getScore(){
		return this.score;
	}
	public float getSpeed(){
		return this.baseSpeed;
	}
	public float getSpawnCooldown(){return this.spawnCooldown;}
	public int getDirection(){
		return this.direction;
	}
	//Setters
	public void setScore(float newScore){
		this.score=newScore;
	}
	public void updateScore(float scoreToAdd){
		this.score+=scoreToAdd;
	}
	public void setSpeed(float newSpeed){
		this.baseSpeed=newSpeed;
	}
	public void updateSpeed(float speed){
		this.baseSpeed+=speed;
	}
	public void setSpawnCooldown(float newCooldown){this.spawnCooldown=newCooldown;}
	public void updateSpawnCooldown(float cd){this.spawnCooldown+=cd;}
	public Commander(World world,float posX){
		this.posX=posX;
		onCooldown=false;
		spawnTimer=0f;
		score=0f;
		this.baseSpeed=Configuration.baseEntitySpeed;
		
	}
	public void spawn(EntityManager em,World world,Vector2 clicked){
		if(!onCooldown){
			//Check if the commander has enough gold
			if(EntityManager.entityCosts.get(this.currentType)<=this.gold){
				Vector2 spawnPoint=new Vector2(this.posX,clicked.y);
				if(clicked.y>Configuration.gameWorldHeight-Configuration.baseEntityHeight){
					spawnPoint.y=Configuration.gameWorldHeight-Configuration.baseEntityHeight;
				}
				em.createEntity(this.currentType,this,world,spawnPoint);
				onCooldown=true;
				this.gold-=EntityManager.entityCosts.get(this.currentType);
			}else{
				System.out.println("Not enough gold");
			}	
		}else{
			//Cant spawn
		}
	}
	public void createWall(World world,short categoryBits){
		//Create the wall
		BodyDef bd=new BodyDef();
		bd.type=BodyType.StaticBody;
		bd.position.set(posX,0);
		Body body=world.createBody(bd);
		body.setUserData(this);
		PolygonShape edge=new PolygonShape();
		edge.set(new Vector2[] {
				new Vector2(0,0),
				new Vector2(0.1f,0f),
				new Vector2(0,Configuration.gameWorldHeight),
				new Vector2(0.1f,Configuration.gameWorldHeight)
		});
		FixtureDef fixDef=new FixtureDef();
		fixDef.shape=edge;
		fixDef.isSensor=true;
		fixDef.filter.categoryBits=categoryBits;
		body.createFixture(fixDef);
		edge.dispose();
	}
	public void update(float delta){
		//Check if the commander is out of cooldown
		if(onCooldown){
			if(spawnTimer>spawnCooldown){
				this.onCooldown=false;
				spawnTimer=0;
			}else{
				this.spawnTimer+=delta;
			}
		}
	}
}