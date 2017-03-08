package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

public class Unit extends Entity{
	protected EntityManager.UnitType unitType;
	protected Texture sheet;
	
	//Unit specific variables
	protected int health;
	protected int damage;
	protected float speedProp;
	
	//Getters
	public int getDamage(){
		return this.damage;
	}
	public int getHealth(){
		return this.health;
	}
	public EntityManager.UnitType getUnitType(){
		return this.unitType;
	}
	public Unit(int id, Commander commander, World world, Texture sheet, Vector2 initPos,boolean createSprite,boolean createBody) {
		super(id, commander,world, initPos);
		this.type=EntityManager.EntityType.UNIT;
		this.sheet=sheet;
		if(createSprite){
			this.createSprite(initPos.x, initPos.y);
			this.sprite.setRegion(new TextureRegion(this.sheet,0,0,32,64));
		}
		if(createBody){
			this.createDynamicBody(world, sprite.getWidth(), sprite.getHeight(), initPos.x, initPos.y);
			this.body.setLinearVelocity(Configuration.baseEntitySpeed*this.commander.getDirection(), 0);
		}
		
	}
	public void takeDamage(float damage){
		this.health-=damage;
	}
	
	

}
