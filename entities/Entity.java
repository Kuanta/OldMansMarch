package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.commanders.Commander;

public abstract class Entity {
	public Sprite sprite;
	public Body body;
	public Texture sheet;
	
	protected boolean isActive;
	protected int id;
	protected short filter=0x001;
	protected Commander commander;
	protected EntityManager.EntityType type;
	protected float speed=Configuration.baseEntitySpeed;
	
	
	//Entity specific variables
	protected int health;
	protected int damage;
	protected float speedProp;

	public Entity(int id,Commander commander,World world,Texture sheet,Vector2 initPos){
		System.out.println("Created an entity with an id of:"+id);
		this.id=id;
		this.sheet=sheet;
		this.isActive=true;
		this.commander=commander; //The Entity belongs to player as default 
		if(commander.getFaction()==EntityManager.Faction.PLAYER){
			this.filter=EntityManager.PLAYER_UNIT;
		}else{
			this.filter=EntityManager.ENEMY_UNIT;
		}
		
		//Defaults
		health=1;
		damage=1;
		speedProp=1;
		createSprite(world,sheet,initPos.x,initPos.y);
	}
	
	//Getters and Setters
	public void setActive(boolean active){
		this.isActive=active;
	}
	public boolean isActive(){
		return this.isActive;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return this.id;
	}
	public Commander getCommander(){
		return this.commander;
	}
	public int getDamage(){
		return this.damage;
	}
	public void createSprite(World world,Texture sheet, float initX, float initY){
		
		float pixPerUnit=GameManager.pixPerUnit;
		float width=Configuration.baseEntityWidth;
		float height=Configuration.baseEntityHeight;
		TextureRegion region=new TextureRegion(sheet,0,0,32,64);
		sprite=new Sprite(region);
		sprite.setSize(width,height);
		

	}
	public void createBody(World world,float initX,float initY){ //Always call this on the overloaded constructor
		BodyDef bd=new BodyDef();
		bd.type=BodyType.DynamicBody;
		bd.position.set(initX, initY);
		body=world.createBody(bd);
		body.setUserData(this);
		if(this.commander.getFaction()==EntityManager.Faction.PLAYER){
			body.setLinearVelocity(this.commander.getSpeed()*this.speedProp,0);
		}else{
			body.setLinearVelocity(this.commander.getSpeed()*this.speedProp*-1,0);
		}


		//Shape
		PolygonShape poly=new PolygonShape();
		poly.set(new Vector2[] {
				new Vector2(0,0),
				new Vector2(sprite.getWidth(),0),
				new Vector2(0,sprite.getHeight()),
				new Vector2(sprite.getWidth(),sprite.getHeight())

		});
		//FixtureDef
		FixtureDef fixDef=new FixtureDef();
		fixDef.shape=poly;
		fixDef.isSensor=true;
		fixDef.filter.categoryBits=this.filter;
		body.createFixture(fixDef);
		poly.dispose();
	}
	//Other
	public void draw(SpriteBatch batch){
		batch.draw(sprite, body.getPosition().x, body.getPosition().y, body.getLocalCenter().x,
				body.getLocalCenter().y, sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), body.getAngle());
	}
	public void update(float delta){

	}
}
