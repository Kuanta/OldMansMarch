package com.oldmansmarch.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.Entity;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.Projectile;
import com.oldmansmarch.entities.Unit;

public class Fireball extends Projectile{
	
	private float speed=2f;
	public Fireball(int id,Commander commander, World world ,Texture sheet, Vector2 initPos, Vector2 initialSpeed) {
		super(id, commander,world,sheet, initPos, initialSpeed);
		this.sprite.setSize(Configuration.baseEntityWidth/2f, Configuration.baseEntityWidth/2f);
		this.sprite.setRegion(new TextureRegion(this.sheet,0,0,32,32));
		this.createDynamicBody(world, this.sprite.getWidth(), this.sprite.getHeight(), initPos.x, initPos.y);
		this.body.setLinearVelocity(initialSpeed.x*speed, initialSpeed.y);
		this.damage=1;
	}
	@Override
	public void doStuff(Entity ent) {
		// TODO Auto-generated method stub
		super.doStuff(ent);
		if(ent.getType()==EntityManager.EntityType.UNIT){
			((Unit) ent).takeDamage(this.damage);
		}
	}
	
}
