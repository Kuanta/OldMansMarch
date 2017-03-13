package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

public class Projectile extends Entity{
	
	private float damage;
	private Texture sheet;
	private float speed;
	private Array<Animation> animations;
	
	//Getters
	public float getDamage(){
		return this.damage;
	}
	public float getSpeed(){
		return this.speed;
	}
	public Projectile(int id, Commander commander,World world, ProjectileTemplate template,Vector2 initPos,Vector2 initalSpeed) { //Initial speed is the speed of the caster
		super(id,commander, world, initPos);
		this.type=EntityManager.EntityType.PROJECTILE;
		
		this.speed=template.getSpeed();
		this.damage=template.getDamage();
		
		//Animation codes
		this.animations=new Array<Animation>();
		Array<AnimationTemplate> animTemplates=template.getAnimationTemplates();
		for(int i=0;i<animTemplates.size;i++){
			AnimationTemplate animTemplate=animTemplates.get(i);
			this.animations.add(new Animation(animTemplate.animDuration,animTemplate.frames));
		}
		
		//Sprite and Body
		this.createSprite(template.getWidth()*Configuration.baseEntityWidth,template.getHeight()*Configuration.baseEntityHeight,initPos.x,initPos.y);
		this.createDynamicBody(world, sprite.getWidth(), sprite.getHeight(), initPos.x,initPos.y);
		this.body.setLinearVelocity(((float)this.speed)*Configuration.baseEntitySpeed*commander.getDirection(), 0);
		this.sprite.setRegion((TextureRegion) animations.get(0).getKeyFrame(elapsedTime,true));
		
	}
	public void update(float deltaTime){
		super.update(deltaTime);
		this.sprite.setRegion((TextureRegion) animations.get(0).getKeyFrame(elapsedTime,true));
	}
}
