package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.commanders.Commander;

public class Wall extends Entity{
	
	public Wall(int id, Commander commander, World world,Vector2 initPos) {
		super(id, commander,world, initPos);
		this.type=EntityManager.EntityType.WALL;
		// TODO Auto-generated constructor stub
		this.createSprite(initPos.x, initPos.y);
		//Create the wall
		BodyDef bd=new BodyDef();
		bd.type=BodyType.StaticBody;
		bd.position.set(initPos.x,0);
		this.body=world.createBody(bd);
		body.setUserData(this);
		PolygonShape edge=new PolygonShape();
		edge.set(new Vector2[] {
				new Vector2(0,0),
				new Vector2(0.1f,0f),
				new Vector2(0,Configuration.gameWorldHeight),
				new Vector2(0.1f,Configuration.gameWorldHeight)
		});
		FixtureDef fixDef=new FixtureDef();
		fixDef.shape=edge;
		fixDef.isSensor=true;
		fixDef.filter.categoryBits=EntityManager.WALL;
		body.createFixture(fixDef);
		edge.dispose();
	}
	
}
