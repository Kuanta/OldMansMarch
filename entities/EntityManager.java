/*
 * 	Id Assignment Algorithm
 * When deleting an entity, save its id as the lastFreedId. If lastFreedId has
 * a value other than -1, set the next Entity's id as lastFreeId and set lastFreeId as -1. Otherwise give it
 * the nextId and increment the nextId.
 * Edit:More than one entity can be deleted in a loop. So, save their id in a freedId pool then use 
 * it to assign next Entity's id
 */

package com.oldmansmarch.entities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.commanders.Commander;

public class EntityManager {
	/*Filters
	First bit defines if it's wall or unit(0=wall 1=unit), second bit defines if it's enemy or player(0=enemy, 1=player);*/
	public static final short ENEMY_WALL=0b1000000;
	public static final short ENEMY_UNIT=0b1000001;
	public static final short PLAYER_WALL=0b1000010;
	public static final short PLAYER_UNIT=0b1000011;
	public static final int TYPE_BIT=0;
	public static final int FACTION_BIT=1;
	public static final short ARMY=0x1<<1;
	public static enum EntityType{
		INFANTRY,
		MOUNTED,
		ZOMBIE,
		TEST,
		WIZARD,
		FIREBALL
	}
	public static HashMap<EntityType,Float> entityCosts;
	static{
		entityCosts=new HashMap<EntityType,Float>();
		entityCosts.put(EntityType.INFANTRY, 1f);
		entityCosts.put(EntityType.MOUNTED, 2f);
		entityCosts.put(EntityType.ZOMBIE, 1f);
		entityCosts.put(EntityType.WIZARD,2f);
		entityCosts.put(EntityType.TEST, 1f);
	}
	
	public static enum Faction{
		PLAYER,
		ENEMY
	}
	//Containers
	private HashMap<Integer, Entity> entities; //This will hold the entities present on PlayerState
	public HashMap<EntityType,Texture> textures;
	
	//Id variables
	private Array<Integer> lastFreedId;
	private int nextId;
	private int entCount;
	public Array<Integer> toDelete;
	
	//Getters and Setters
	public int getEntCount(){
		return this.entCount;
	}
	
	public EntityManager(){
		this.entities=new HashMap<Integer,Entity>();
		this.toDelete=new Array<Integer>();
		this.lastFreedId=new Array<Integer>();
		entCount=0;
		nextId=0;
		
		//Create Textures
		textures=new HashMap<EntityType,Texture>();
		textures.put(EntityType.INFANTRY, new Texture(Gdx.files.internal("claudius.png")));
		textures.put(EntityType.MOUNTED, new Texture(Gdx.files.internal("claudius.png")));
		textures.put(EntityType.TEST, new Texture(Gdx.files.internal("claudius.png")));
		textures.put(EntityType.ZOMBIE, new Texture(Gdx.files.internal("zombies.png")));
		textures.put(EntityType.WIZARD,new Texture(Gdx.files.internal("laila.png")));
		textures.put(EntityType.FIREBALL,new Texture(Gdx.files.internal("fireball.png")));
	}
	public void draw(SpriteBatch batch){
		for(Entry<Integer, Entity> ent:this.entities.entrySet()){
			ent.getValue().draw(batch);
		}
	}
	public void createEntity(EntityType type,Commander commander,World world,Vector2 spawnPoint){
		int id;
		if(lastFreedId.size>0){
			id=lastFreedId.get(lastFreedId.size-1);
			lastFreedId.removeIndex(lastFreedId.size-1);
		}else{
			id=nextId;
			nextId++;
		}
		switch(type){
		case TEST:
			this.entities.put(id, new TestEntity(id,commander,world,textures.get(type),spawnPoint));
			break;
		case ZOMBIE:
			this.entities.put(id, new Zombie(id,commander,world,textures.get(type),spawnPoint));
			break;
		case WIZARD:
			this.entities.put(id,new Wizard(id,commander,this,world,textures.get(type),spawnPoint));
			break;
		case FIREBALL:
			this.entities.put(id,new Fireball(id,commander,world,textures.get(type),spawnPoint));
			break;
		default:
			//this.entities.put(id, new Zombie(id,commander,world,textures.get(EntityType.ZOMBIE),spawnPoint));
			break;
		}
		entCount++;
		
	}
	public void combat(Entity entA,Entity entB){
		entA.health-=entB.damage;
		entB.health-=entA.damage;
		
		System.out.println("Combat");
		
		//Kill the entities if they are 0 hp
		if(entA.health<=0){
			//if entA is the enemy, add its cost as score
			if(entB.commander.getFaction()==Faction.PLAYER){ //EntB belongs to player
				entB.commander.updateScore(entityCosts.get(entB.type));
			}
			this.toDelete.add(entA.id);
		}
		if(entB.health<=0){
			//If entB is the enemy
			if(entA.commander.getFaction()==Faction.PLAYER){//EntA belongs to player
				entA.commander.updateScore(entityCosts.get(entA.type));
			}
			this.toDelete.add(entB.id);
		}
	}
	public void addEntity(int id,Entity newEntity){
		this.entities.put(id,newEntity);
		entCount++;
	}
	public int requestId(){
		//If outside objects wants to create an Entity...
		int id;
		if(lastFreedId.size>0){
			id=lastFreedId.get(lastFreedId.size-1);
			lastFreedId.removeIndex(lastFreedId.size-1);
		}else{
			id=nextId;
			nextId++;
		}
		return id;
	}
	public void update(float delta){
		for(int id:entities.keySet()){
			if(entities.containsKey(id)){
				System.out.println("Ýd:"+id);
				entities.get(id).update(delta);
			}
			
		}
	}
	public void deleteEntities(World world){
		for(int i=0;i<toDelete.size;i++){
			Entity ent=this.entities.get(toDelete.get(i));
			world.destroyBody(ent.body);
			this.entities.remove(toDelete.get(i));
			lastFreedId.add(toDelete.get(i));
			entCount--;
			System.out.println("Deleted an entity with id of:"+i);
		}
		
	}
	public void dispose(){
		for(EntityType type:textures.keySet()){
			textures.get(type).dispose();
		}
	}
}
