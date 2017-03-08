package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

public abstract class Caster extends Unit {
    protected World world;
    protected EntityManager em;
    private RayCastCallback callback;
    protected float spawnCooldown;
    protected float spawnTimer;
    protected boolean onCooldown;
    protected EntityManager.ProjectileType currentProjectile;
    protected float range;
    public Caster(int id, final Commander commander, EntityManager em, World world, Texture sheet, Vector2 initPos, boolean createSprite, boolean createBody) {
        super(id, commander, world, sheet, initPos, createSprite, createBody);
        this.world=world;
        this.em=em;
        this.spawnTimer=0;
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

        //Defaults
        this.spawnCooldown=1f;
        this.range = Configuration.baseEntityWidth*4f;
        this.currentProjectile= EntityManager.ProjectileType.FIREBALL;
    }
    public void shoot(){
        if(!onCooldown){
            Vector2 bodyPos=this.body.getPosition();
            Vector2 from=new Vector2(bodyPos.x+(this.sprite.getWidth()+0.1f)*this.commander.getDirection(),bodyPos.y+this.sprite.getHeight()/2f);
            //em.createUnit(EntityManager.PType.FIREBALL,this.commander,this.world,from);
            em.createProjectile(this.currentProjectile, this.commander, this.world, from, this.body.getLinearVelocity());
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
