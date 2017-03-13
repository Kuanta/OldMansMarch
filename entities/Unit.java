package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oldmansmarch.AssetsManager;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.AnimationTemplate;

public class Unit extends Entity{
	
	private float health;
	private float damage;
	private float speed;
	private float cost;
	private Array<Animation> animations;
	private TextureRegion preview; //Used in the ui
	private EntityManager em;
	
	//Caster variables
	private boolean isCaster;
	private boolean onCooldown;
	private float spawnTimer;
	private float spawnCooldown;
	private int projectileId;
	private float range;
	private RayCastCallback callback;
	
	//Getters
	public float getDamage(){
		return this.damage;
	}
	public float getHealth(){
		return this.health;
	}
	public float getSpeed(){
		return this.speed;
	}
	public boolean isCaster(){
		return this.isCaster;
	}
	public float getCost(){
		return this.cost;
	}
	
	public Unit(int id, final Commander commander, EntityManager em,World world,UnitTemplate template,AssetsManager assetsMan,Vector2 spawnPoint) {
		super(id, commander,world, spawnPoint);
		
		this.em=em;
		this.type=EntityManager.EntityType.UNIT;
		this.elapsedTime=0;
		this.health=template.getHealth();
		this.damage=template.getDamage();
		this.speed=template.getSpeed();
		this.cost=template.getCost();
		this.isCaster=template.isCaster();
		this.projectileId=template.getProjectileTemplateId();
		this.animations=new Array<Animation>();
		
		//Animation
		Array<AnimationTemplate> animTemplates=template.getAnimationTemplates();
		for(int i=0;i<animTemplates.size;i++){
			AnimationTemplate animTemplate=animTemplates.get(i);
			this.animations.add(new Animation(animTemplate.animDuration,animTemplate.frames));
		}
		
		//Sprite and Body
		this.createSprite(template.getWidth()*Configuration.baseEntityWidth,template.getHeight()*Configuration.baseEntityHeight,spawnPoint.x, spawnPoint.y);
		
		this.createDynamicBody(world, sprite.getWidth(), sprite.getHeight(), spawnPoint.x, spawnPoint.y);
		this.body.setLinearVelocity(((float)this.speed)*Configuration.baseEntitySpeed*commander.getDirection(), 0);
		
		//Caster specific codes
		this.range=Configuration.baseEntityWidth*4f;
		this.spawnCooldown=1f;
		this.onCooldown=false;
		callback=new RayCastCallback(){

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				// TODO Auto-generated method stub
				if(((Entity)fixture.getBody().getUserData()).getType()==EntityManager.EntityType.UNIT ){
					if(((Unit)fixture.getBody().getUserData()).getCommander().getFaction()!=commander.getFaction()){
						cast();
						return 0;
					}else{
						return -1;
					}
				}else{
					return -1;
				}
			}
			
		};
		
		
	}
	public void cast(){
		if(!onCooldown){
			Vector2 bodyPos=this.body.getWorldCenter();
			Vector2 from=new Vector2(bodyPos.x,bodyPos.y+this.sprite.getHeight()/4f);
			this.em.createProjectile(projectileId, commander, world, from, this.body.getLinearVelocity());
			this.onCooldown=true;
		}
	}
	public void takeDamage(float damage){
		this.health-=damage;
	}
	public void update(float deltaTime){
		super.update(deltaTime);
		this.sprite.setRegion((TextureRegion) animations.get(0).getKeyFrame(elapsedTime,true));
		if(isCaster){
			if(onCooldown){
				
			}else{
				Vector2 bodyPos=this.body.getPosition();
				Vector2 from=new Vector2(bodyPos.x+this.sprite.getWidth(),bodyPos.y+this.sprite.getHeight()/2f);
				Vector2 to=new Vector2(from.x+this.range*this.commander.getDirection(),from.y);
				this.world.rayCast(this.callback,from,to);
			}
		}
	}
	

}
