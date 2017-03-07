package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.states.PlayState;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class TestEntity extends Entity{
	

	public TestEntity(int id,Commander commander,World world,Texture sheet,Vector2 spawnPoint){
		super(id,commander,world,sheet,spawnPoint);
		this.type=EntityManager.EntityType.INFANTRY;
		createBody(world,spawnPoint.x,spawnPoint.y);
	}

}
