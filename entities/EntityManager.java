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
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.AssetsManager;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.projectiles.Fireball;
import com.oldmansmarch.entities.units.HeavyUndead;
import com.oldmansmarch.entities.units.Infantry;
import com.oldmansmarch.entities.units.Mounted;
import com.oldmansmarch.entities.units.UndeadMage;
import com.oldmansmarch.entities.units.Wizard;
import com.oldmansmarch.entities.units.Zombie;

public class EntityManager {
	/*Filters
	First bit defines if it's wall or unit(0=wall 1=unit), second bit defines if it's enemy or player(0=enemy, 1=player);*/
	public static final short WALL=0b1000000;
	public static final short UNIT=0b1000001;
	public static final int TYPE_BIT=0;
	public static final short ARMY=0x1<<1;
	public static enum EntityType{
		WALL,
		UNIT,
		PROJECTILE
	}
	public static enum UnitType{
		INFANTRY,
		ZOMBIE,
		WIZARD,
		MOUNTED,
		HEAVY_UNDEAD,
		UNDEAD_MAGE
	}
	public static enum ProjectileType{
		FIREBALL
	}
	//How much an entity will cost?
	public static HashMap<UnitType,Float> entityCosts;
	static{
		entityCosts=new HashMap<UnitType,Float>();
		entityCosts.put(UnitType.INFANTRY, 1f);
		entityCosts.put(UnitType.ZOMBIE, 1f);
		entityCosts.put(UnitType.WIZARD,4f);
		entityCosts.put(UnitType.MOUNTED, 3f);
		entityCosts.put(UnitType.HEAVY_UNDEAD, Configuration.heavyUndeadCost);
		entityCosts.put(UnitType.UNDEAD_MAGE,Configuration.undeadMageCost);
	}
	
	public static enum Faction{
		PLAYER,
		ENEMY
	}
	//Containers
	private ConcurrentHashMap<Integer, Entity> entities; //This will hold the entities present on PlayerState

	//Textures
	public HashMap<UnitType,AssetsManager.Sheets> unitTextures;
	public HashMap<ProjectileType,AssetsManager.Sheets> projectileTextures;
	
	//Id variables
	private Array<Integer> lastFreedId;
	private int nextId;
	private int entCount;
	public Array<Integer> toDelete;

	//AssetsManager
	private AssetsManager assetesManager;

	//Getters and Setters
	public int getEntCount(){
		return this.entCount;
	}
	
	public EntityManager(){
		this.assetesManager=new AssetsManager();
		this.entities=new ConcurrentHashMap<Integer,Entity>();
		this.toDelete=new Array<Integer>();
		this.lastFreedId=new Array<Integer>();
		entCount=0;
		nextId=0;
		
		//Create Unit Textures
		unitTextures=new HashMap<UnitType,AssetsManager.Sheets>();
		unitTextures.put(UnitType.INFANTRY,AssetsManager.Sheets.HUMANS);
		unitTextures.put(UnitType.MOUNTED,AssetsManager.Sheets.HUMANS);
		unitTextures.put(UnitType.WIZARD,AssetsManager.Sheets.HUMANS);
		unitTextures.put(UnitType.ZOMBIE,AssetsManager.Sheets.UNDEADS);
		unitTextures.put(UnitType.HEAVY_UNDEAD,AssetsManager.Sheets.UNDEADS);
		unitTextures.put(UnitType.UNDEAD_MAGE,AssetsManager.Sheets.UNDEADS);
		
		//Create Projectile Texture
		projectileTextures=new HashMap<ProjectileType,AssetsManager.Sheets>();
		projectileTextures.put(ProjectileType.FIREBALL,AssetsManager.Sheets.SPELLS);
	}
	public void draw(SpriteBatch batch){
		for(Entry<Integer, Entity> ent:this.entities.entrySet()){
			if(ent.getValue().getType()!=EntityType.WALL){
				ent.getValue().draw(batch);
			}
			
		}
	}
	public Texture getUnitTexture(UnitType type){
		return this.assetesManager.textures.get(this.unitTextures.get(type));
	}
	public Texture getProjectileTexture(ProjectileType type){
		return this.assetesManager.textures.get(this.projectileTextures.get(type));
	}
	public void createUnit(UnitType type,Commander commander,World world,Vector2 spawnPoint){
		int id;
		id=requestId();
		switch(type){
		case INFANTRY:
			this.entities.put(id, new Infantry(id,commander,world,getUnitTexture(type),spawnPoint));
			break;
		case ZOMBIE:
			this.entities.put(id, new Zombie(id,commander,world,getUnitTexture(type),spawnPoint,true,true));
			break;
		case WIZARD:
			this.entities.put(id,new Wizard(id,commander,this,world,getUnitTexture(type),spawnPoint,true,true));
			break;
		case MOUNTED:
			this.entities.put(id,new Mounted(id,commander,world,getUnitTexture(type),spawnPoint));
			break;
		case HEAVY_UNDEAD:
			this.entities.put(id,new HeavyUndead(id,commander,world,getUnitTexture(type),spawnPoint));
			break;
		case UNDEAD_MAGE:
			this.entities.put(id,new UndeadMage(id,commander,this,world,getUnitTexture(type),spawnPoint));
			break;
		default:
			
			break;
		}
		entCount++;
		
	}
	public void createProjectile(ProjectileType type,Commander commander,World world,Vector2 spawnPoint,Vector2 initialSpeed){
		int id;
		id=requestId();
		switch(type){
		case FIREBALL:
			this.entities.put(id, new Fireball(id, commander,world, getProjectileTexture(type), spawnPoint, initialSpeed));
			break;
		default:
			break;
		}
	}
	public void createWall(World world,Commander commander,Vector2 pos){
		int id;
		id=requestId();
		this.entities.put(id, new Wall(id, commander, world,pos));
	}
	//Unit vs Unit
	public void combat(Unit entA,Unit entB){
		entA.health-=entB.damage;
		entB.health-=entA.damage;
		System.out.println(entB.damage+" "+entA.damage);
		System.out.println("Combat");
		
		//Kill the entities if they are 0 hp
		if(entA.health<=0){
			//if entA is the enemy, add its cost as score
			if(entB.commander.getFaction()==Faction.PLAYER){ //EntB belongs to player
				entB.commander.updateScore(entityCosts.get(entB.getUnitType()));
			}
			this.toDelete.add(entA.id);
		}
		if(entB.health<=0){
			//If entB is the enemy
			if(entA.commander.getFaction()==Faction.PLAYER){//EntA belongs to player
				entA.commander.updateScore(entityCosts.get(entB.getUnitType()));
			}
			this.toDelete.add(entB.id);
		}
	}
	//Projectile vs Unit
	public void impact(Projectile pro,Unit unit){
		pro.doStuff(unit);
		if(unit.getHealth()<=0){
			this.toDelete.add(unit.getId());
		}
		this.toDelete.add(pro.getId());
	}
	public void impact(Unit unit,Projectile pro){
		pro.doStuff(unit);
		if(unit.getHealth()<=0){
			this.toDelete.add(unit.getId());
		}
		this.toDelete.add(pro.getId());
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
		for(Entry<Integer, Entity> ent:this.entities.entrySet()){
				ent.getValue().update(delta);

		}
	}
	public void deleteEntities(World world){
		for(int i=0;i<toDelete.size;i++){
			Entity ent=this.entities.get(toDelete.get(i));
			world.destroyBody(ent.body);
			this.entities.remove(toDelete.get(i));
			lastFreedId.add(toDelete.get(i));
			entCount--;
		}
		
	}
	public void dispose(){
		this.assetesManager.dispose();
	}
}
