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
import com.oldmansmarch.entities.UnitTemplate;

public abstract class Commander {
	protected float spawnCooldown=0.2f;
	protected float spawnTimer;
	protected boolean onCooldown;
	protected float health=1f;
	protected UnitTemplate currentUnitTemplate;
	protected float posX; //This is the position in horizontal axis from which the units going to spawn
	protected EntityManager.Faction faction;
	protected float gold;
	protected float score;
	protected float baseSpeed;
	protected int direction;
	protected EntityManager em;
	protected float maxGold;
	
	//Getters
	public float getHealth(){
		return health;
	}
	public void damageCommander(float damage){
		this.health-=damage;
	}
	public void setCurrentType(UnitTemplate type){
		this.currentUnitTemplate=type;
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
	public float getMaxGold(){
		return this.maxGold;
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
	public void setMaxGold(float maxGold){
		this.maxGold=maxGold;
	}
	public void updateGold(float goldToAdd){
		this.maxGold+=goldToAdd;
	}
	public void setSpawnCooldown(float newCooldown){this.spawnCooldown=newCooldown;}
	public void updateSpawnCooldown(float cd){this.spawnCooldown+=cd;}
	public Commander(World world,float posX,EntityManager em){
		this.em=em;
		this.posX=posX;
		onCooldown=false;
		spawnTimer=0f;
		score=0f;
		this.baseSpeed=Configuration.baseEntitySpeed;
		
	}
	public void spawn(EntityManager em,World world,Vector2 clicked){
		if(!onCooldown){
			//Check if the commander has enough gold
			if(currentUnitTemplate.getCost()<=this.gold){
				Vector2 spawnPoint=new Vector2(this.posX,clicked.y);
				if(clicked.y>Configuration.gameWorldHeight-Configuration.baseEntityHeight){
					spawnPoint.y=Configuration.gameWorldHeight-Configuration.baseEntityHeight;
				}
				em.createUnit(this.currentUnitTemplate,this,world,spawnPoint);
				onCooldown=true;
				this.gold-=currentUnitTemplate.getCost();
			}else{
				System.out.println("Not enough gold");
			}	
		}else{
			//Cant spawn
		}
	}
	public void createWall(World world,EntityManager em){
		em.createWall(world, this, new Vector2(this.posX,0));
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
