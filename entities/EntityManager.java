/*
 * 	Id Assignment Algorithm
 * When deleting an entity, save its id as the lastFreedId. If lastFreedId has
 * a value other than -1, set the next Entity's id as lastFreeId and set lastFreeId as -1. Otherwise give it
 * the nextId and increment the nextId.
 * Edit:More than one entity can be deleted in a loop. So, save their id in a freedId pool then use 
 * it to assign next Entity's id
 */

package com.oldmansmarch.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.oldmansmarch.AssetsManager;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.UnitTemplate;



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
	public HashMap<Integer,UnitTemplate> unitTemplates;
	public HashMap<Integer,UnitTemplate> enemyUnitTemplates;
	public HashMap<Integer,ProjectileTemplate> projectileTemplates;
	
	public static enum Faction{
		PLAYER,
		ENEMY
	}
	//Containers
	private ConcurrentHashMap<Integer, Entity> entities; //This will hold the entities present on PlayerState
	
	//Id variables
	private Array<Integer> lastFreedId;
	private int nextId;
	private int entCount;
	public Array<Integer> toDelete;

	//AssetsManager
	private AssetsManager assetsManager;

	//Getters and Setters
	public int getEntCount(){
		return this.entCount;
	}
	
	public EntityManager(){
		this.assetsManager=new AssetsManager();
		
		//Create the templates
		this.unitTemplates=parseUnitTemplates("unitTypes.xml");
		this.enemyUnitTemplates=parseUnitTemplates("enemyUnitTypes.xml");
		this.projectileTemplates=parseProjectileTemplates("projectileTypes.xml");
		
		this.entities=new ConcurrentHashMap<Integer,Entity>();
		this.toDelete=new Array<Integer>();
		this.lastFreedId=new Array<Integer>();
		entCount=0;
		nextId=0;
		
	}
	public void draw(SpriteBatch batch){
		for(Entry<Integer, Entity> ent:this.entities.entrySet()){
			if(ent.getValue().getType()!=EntityType.WALL){
				ent.getValue().draw(batch);
			}
			
		}
	}
	
	public HashMap<Integer,UnitTemplate> parseUnitTemplates(String filename){
		HashMap<Integer,UnitTemplate> templates=new HashMap<Integer,UnitTemplate>();
		XmlReader reader=new XmlReader();
		try {
			XmlReader.Element root=reader.parse(Gdx.files.internal(filename));
			for(int i=0;i<root.getChildCount();i++){
				//Read attributes of the UnitType
				XmlReader.Element type=root.getChild(i);
				int id=type.getIntAttribute("id");
				String name=type.getAttribute("name");
				int isCaster=type.getIntAttribute("isCaster");
				int projectileTemplateId=type.getIntAttribute("projectileTemplateId");
				int health=type.getIntAttribute("health");
				int damage=type.getIntAttribute("damage");
				int cost=type.getIntAttribute("cost");
				float speed=Float.parseFloat(type.getAttribute("speed"));
				float width=Float.parseFloat(type.getAttribute("width"));
				float height=Float.parseFloat(type.getAttribute("height"));
				//UnitType unitType=new UnitType(id,name,isCaster,health,damage,cost);
				UnitTemplate unitType=new UnitTemplate(id, name, isCaster, projectileTemplateId,health, damage, speed,width,height,cost);
				
				XmlReader.Element animations=type.getChild(0);
				int animationCount=animations.getChildCount();
				for(int j=0;j<animationCount;j++){
					XmlReader.Element anim=animations.getChild(j);
					unitType.addAnimationTemplate(anim, assetsManager);
				}
				unitType.setPreview();
				templates.put(id,unitType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return templates;
	}
	public HashMap<Integer,ProjectileTemplate> parseProjectileTemplates(String filename){
		HashMap<Integer,ProjectileTemplate> templates=new HashMap<Integer,ProjectileTemplate>();
		XmlReader reader=new XmlReader();
		try {
			XmlReader.Element root=reader.parse(Gdx.files.internal(filename));
			for(int i=0;i<root.getChildCount();i++){
				//Read attributes of the UnitType
				XmlReader.Element type=root.getChild(i);
				int id=type.getIntAttribute("id");
				String name=type.getAttribute("name");
				int damage=type.getIntAttribute("damage");
				float speed=Float.parseFloat(type.getAttribute("speed"));
				float width=Float.parseFloat(type.getAttribute("width"));
				float height=Float.parseFloat(type.getAttribute("height"));
				//UnitType unitType=new UnitType(id,name,isCaster,health,damage,cost);
				ProjectileTemplate projectileType=new ProjectileTemplate(id, name,damage, speed,width,height);
				
				XmlReader.Element animations=type.getChild(0);
				int animationCount=animations.getChildCount();
				for(int j=0;j<animationCount;j++){
					XmlReader.Element anim=animations.getChild(j);
					projectileType.addAnimationTemplate(anim, assetsManager);
				}
				templates.put(id,projectileType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return templates;
	}
	public void createUnit(UnitTemplate template,Commander commander,World world,Vector2 spawnPoint){
		int id;
		id=requestId();
		this.entities.put(id, new Unit(id,commander,this,world,template,this.assetsManager,spawnPoint));
		entCount++;
		
	}
	public void createProjectile(int templateId,Commander commander,World world,Vector2 spawnPoint,Vector2 initialSpeed){
		int id=requestId();
		this.entities.put(id,new Projectile(id,commander,world,this.projectileTemplates.get(templateId),spawnPoint,initialSpeed));
		entCount++;
	}
	public void createWall(World world,Commander commander,Vector2 pos){
		int id;
		id=requestId();
		this.entities.put(id, new Wall(id, commander, world,pos));
	}
	//Unit vs Unit
	public void combat(Unit entA,Unit entB){
		entA.takeDamage(entB.getDamage());
		entB.takeDamage(entA.getDamage());
		
		//Kill the entities if they are 0 hp
		if(entA.getHealth()<=0){
			//if entA is the enemy, add its cost as score
			if(entB.commander.getFaction()==Faction.PLAYER){ //EntB belongs to player
				entB.commander.updateScore(entA.getCost());
			}
			this.toDelete.add(entA.id);
		}
		if(entB.getHealth()<=0){
			//If entB is the enemy
			if(entA.commander.getFaction()==Faction.PLAYER){//EntA belongs to player
				entA.commander.updateScore(entB.getCost());
			}
			this.toDelete.add(entB.id);
		}
	}
	//Projectile vs Unit
	public void impact(Projectile pro,Unit unit){
		unit.takeDamage(pro.getDamage());
		if(unit.getHealth()<=0){
			this.toDelete.add(unit.getId());
		}
		System.out.println("Deleted a pro with id"+pro.getId());
		this.toDelete.add(pro.getId());
	}
	public void impact(Unit unit,Projectile pro){
		unit.takeDamage(pro.getDamage());
		if(unit.getHealth()<=0){
			this.toDelete.add(unit.getId());
		}
		System.out.println("Deleted a pro with id"+pro.getId());
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
			if(ent!=null){
				world.destroyBody(ent.body);
				this.entities.remove(toDelete.get(i));
				lastFreedId.add(toDelete.get(i));
				entCount--;
			}
			
		}
		
	}
	public void dispose(){
		this.assetsManager.dispose();
	}
}
