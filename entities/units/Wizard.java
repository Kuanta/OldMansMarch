package com.oldmansmarch.entities.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.AssetsManager;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.Entity;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.EntityManager.EntityType;
import com.oldmansmarch.entities.Unit;

public class Wizard extends Unit {
    private World world;
    private EntityManager em;
    private RayCastCallback callback;
    protected float spawnCooldown=2f;
    protected float spawnTimer;
    protected boolean onCooldown;
    private float range=Configuration.baseEntityWidth*4f;

    public Wizard(int id, final Commander commander, EntityManager em,World world, Texture sheet, Vector2 initPos,boolean createSprite,boolean createBody) {
        super(id, commander, world, sheet, initPos, createSprite,createBody);
        this.world=world;
        this.em=em;
        this.unitType=EntityManager.UnitType.WIZARD;
        this.sprite.setRegion(new TextureRegion(this.sheet, (int)AssetsManager.wizardRect.x,(int)AssetsManager.wizardRect.y,48,48));
        callback=new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            	if(((Entity) fixture.getBody().getUserData()).getType()==EntityManager.EntityType.UNIT){
            		//If the raycasted entity is a Unit
            		if(((Unit) fixture.getBody().getUserData()).getCommander().getFaction()!=commander.getFaction()){
            			//If the Entity belongs to enemy
            			shoot();
            			return 0;
	            	}else{
	            		return -1;
	            	}
            	}else{
            		return -1;
            	}
            	
                
            }
        };
        this.onCooldown=false;
        this.spawnTimer=0;
        this.damage=0;
    }
    public void shoot(){
        if(!onCooldown){
        	Vector2 bodyPos=this.body.getPosition();
            Vector2 from=new Vector2(bodyPos.x+this.sprite.getWidth()+0.1f,bodyPos.y+this.sprite.getHeight()/2f);
           //em.createUnit(EntityManager.PType.FIREBALL,this.commander,this.world,from);
           em.createProjectile(EntityManager.ProjectileType.FIREBALL, this.commander, this.world, from, this.body.getLinearVelocity());
            onCooldown=true;
        }
    }
    public void update(float delta){
        if(onCooldown){
            if(spawnTimer<spawnCooldown){
                spawnTimer+=delta;
            }else{
                spawnTimer=0;
                onCooldown=false;
            }
        }else{
        	Vector2 bodyPos=this.body.getPosition();
            Vector2 from=new Vector2(bodyPos.x+this.sprite.getWidth(),bodyPos.y+this.sprite.getHeight()/2f);
            Vector2 to=new Vector2(from.x+this.range*this.commander.getDirection(),from.y);
            this.world.rayCast(this.callback,from,to);
        }
    }
}
