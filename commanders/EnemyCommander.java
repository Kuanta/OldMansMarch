package com.oldmansmarch.commanders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.states.PlayState;

import java.util.ArrayList;

public class EnemyCommander extends Commander{
	private int index=0;
	private Vector2[]  spawnPoints;
	private int spawnPointsLength;
	private Array<Integer> enemyUnitTemplateIds;
	private float spawnPointOffset; //Don't spawn enemies behind the HUD. This offsets tries to avoid that
	public EnemyCommander(World world,float posX,EntityManager em,float offset){
		super(world,posX,em);
		currentUnitTemplate=em.enemyUnitTemplates.get(0); //Set as default
		
		this.health=Configuration.enemyHealth;
		this.faction=EntityManager.Faction.ENEMY;
		createWall(world,this.em);
		this.gold=100;

		this.spawnCooldown=0.8f;
		this.spawnPointOffset=offset;
		this.direction=-1;
		createSpawnPoints();
		
		//....Create the available unit pool.......(the number of loops increase the spawn chance of that unit)
		this.enemyUnitTemplateIds=new Array<Integer>();
		
		//Id=0 (Zombie)
		for(int i=0;i<5;i++){
			enemyUnitTemplateIds.add(0);
		}
		
		for(int i=0;i<2;i++){
			enemyUnitTemplateIds.add(1);
		}
		
		for(int i=0;i<2;i++){
			enemyUnitTemplateIds.add(2);
		}
		
		for(int i=0;i<1;i++){
			enemyUnitTemplateIds.add(3);
		}
	}
	public void update(float delta,PlayState masterState){
		//Let the man think
		super.update(delta);
		this.gold=100; //Unlimited resources
		
		//Decide which unit to spawn
		int key=enemyUnitTemplateIds.get((int) (Math.random()*enemyUnitTemplateIds.size));
		this.currentUnitTemplate=masterState.getEntityManager().enemyUnitTemplates.get(key);
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
