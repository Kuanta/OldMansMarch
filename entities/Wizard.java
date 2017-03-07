package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

import static com.oldmansmarch.entities.EntityManager.EntityType.WIZARD;


public class Wizard extends Entity {
    private World world;
    private EntityManager em;
    private RayCastCallback callback;
    protected float spawnCooldown=2f;
    protected float spawnTimer;
    protected boolean onCooldown;
    private float range=Configuration.baseEntityWidth*4f;

    public Wizard(int id, Commander commander, EntityManager em,World world, Texture sheet, Vector2 initPos) {
        super(id, commander, world, sheet, initPos);
        this.world=world;
        this.em=em;
        this.type=EntityManager.EntityType.WIZARD;
        createBody(world,initPos.x,initPos.y);
        callback=new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            	System.out.println("Saw something");
                shoot();
                return 0;
            }
        };
        this.onCooldown=false;
        this.spawnTimer=0;
    }
    public void shoot(){
        if(!onCooldown){
        	Vector2 bodyPos=this.body.getPosition();
            Vector2 from=new Vector2(bodyPos.x+this.sprite.getWidth()+0.1f,bodyPos.y+this.sprite.getHeight()/2f);
           em.createEntity(EntityManager.EntityType.FIREBALL,this.commander,this.world,from);
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
