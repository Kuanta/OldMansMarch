package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

/**
 * Created by Kuanta on 6.03.2017.
 */

public class Fireball extends Entity {

    public Fireball(int id, Commander commander, World world, Texture sheet, Vector2 initPos) {
        super(id, commander, world, sheet, initPos);
        createBody(world,initPos.x,initPos.y);
        //this.sprite.setRegion(new TextureRegion(this.sheet,0,0,32,32));
        this.sprite.setSize(Configuration.baseEntityWidth/2f,Configuration.baseEntityHeight/2f);
        this.body.setLinearVelocity(this.body.getLinearVelocity().x*2f,this.body.getLinearVelocity().y);
        //
        System.out.println("Created a fireball with an id:"+id);
    }
}
