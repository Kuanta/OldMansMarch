package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.commanders.Commander;

public abstract class Projectile extends Entity{
	
	protected EntityManager.ProjectileType projectileType;
	protected float damage;
	protected Texture sheet;
	
	//Getters
	public float getDamage(){
		return this.damage;
	}
	public Projectile(int id, Commander commander,World world, Texture sheet,Vector2 initPos,Vector2 initalSpeed) { //Initial speed is the speed of the caster
		super(id,commander, world, initPos);
		this.type=EntityManager.EntityType.PROJECTILE;
		this.sheet=sheet;
		this.createSprite(initPos.x, initPos.y);
		
	}
	public void doStuff(Entity ent){
		
	}
	
}
