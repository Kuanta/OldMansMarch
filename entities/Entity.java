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
import com.oldmansmarch.commanders.Commander;

public abstract class Entity {
	public Sprite sprite;
	public Body body;
	
	protected boolean isActive;
	protected int id;
	protected EntityManager.EntityType type;
	protected Commander commander;
	

	public Entity(int id,Commander commander,World world,Vector2 initPos){
		this.id=id;
		this.isActive=true;
		this.commander=commander;
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
	public EntityManager.EntityType getType(){
		return this.type;
	}
	public Commander getCommander(){
		return this.commander;
	}
	
	public void createSprite(float initX, float initY){
		float width=Configuration.baseEntityWidth;
		float height=Configuration.baseEntityHeight;
		this.sprite=new Sprite();
		this.sprite.setSize(width,height);
		this.sprite.setPosition(initX, initY);
	}
	public void createSprite(float width,float height,float initX,float initY){
		sprite=new Sprite();
		sprite.setSize(width,height);
		sprite.setPosition(initX, initY);
	}
	public void createStaticBody(World world,float width,float height,float initX,float initY){
		BodyDef bd=new BodyDef();
		bd.type=BodyType.StaticBody;
		bd.position.set(initX, initY);
		this.body=world.createBody(bd);
		body.setUserData(this);

		//Shape
		PolygonShape poly=new PolygonShape();
		poly.set(new Vector2[] {
				new Vector2(0,0),
				new Vector2(width,0),
				new Vector2(0,height),
				new Vector2(width,height)

		});
		//FixtureDef
		FixtureDef fixDef=new FixtureDef();
		fixDef.shape=poly;
		fixDef.isSensor=true;
		//fixDef.filter.categoryBits=this.filter;
		body.createFixture(fixDef);
		poly.dispose();
	}
	public void createDynamicBody(World world,float width,float height,float initX,float initY){ //Always call this on the overloaded constructor
		BodyDef bd=new BodyDef();
		bd.type=BodyType.DynamicBody;
		bd.position.set(initX, initY);
		this.body=world.createBody(bd);
		this.body.setUserData(this);

		//Shape
		PolygonShape poly=new PolygonShape();
		poly.set(new Vector2[] {
				new Vector2(0,0),
				new Vector2(width,0),
				new Vector2(0,height),
				new Vector2(width,height)

		});
		//FixtureDef
		FixtureDef fixDef=new FixtureDef();
		fixDef.shape=poly;
		fixDef.isSensor=true;
		//fixDef.filter.categoryBits=this.filter;
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
