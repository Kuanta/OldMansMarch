package com.oldmansmarch.commanders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.states.PlayState;

public class EnemyCommander extends Commander{
	private EntityManager.EntityType[] entityValues;
	private int entityValuesLength;
	private int index=0;
	private Vector2[]  spawnPoints;
	private int spawnPointsLength;
	private float spawnPointOffset; //Don't spawn enemies behind the HUD. This offsets tries to avoid that
	public EnemyCommander(World world,float posX,EntityManager em,float offset){
		super(world,posX,em);
		currentType=EntityManager.UnitType.ZOMBIE; //Set as default
		this.health=Configuration.enemyHealth;
		this.faction=EntityManager.Faction.ENEMY;
		createWall(world,this.em);
		this.gold=100;
		entityValues=EntityManager.EntityType.values();
		entityValuesLength=entityValues.length;
		this.spawnCooldown=5f;
		this.spawnPointOffset=offset;
		this.direction=-1;
		createSpawnPoints();
	}
	public void update(float delta,PlayState masterState){
		//Let the man think
		super.update(delta);
		this.gold=100; //Unlimited resources
		index=(int)(Math.random()*entityValuesLength);
		//this.currentType=entityValues[index];
		spawn(masterState.getEntityManager(),masterState.getWorld(),spawnPoints[(int) (Math.random()*spawnPointsLength)]);
	}
	public void resize(){
		//Call this when the windows is resized
	}
	public void createSpawnPoints(){
		spawnPointsLength=(int) ((Configuration.gameWorldHeight-spawnPointOffset)/Configuration.baseEntityHeight);
		this.spawnPoints=new Vector2[spawnPointsLength];
		for(int i=0;i<spawnPointsLength;i++){
			spawnPoints[i]=new Vector2(this.posX,i*Configuration.baseEntityHeight+spawnPointOffset);
		}
	}
}
