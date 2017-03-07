package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.oldmansmarch.GameManager;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.EntityManager.Faction;

public class Zombie extends Entity{

	public Zombie(int id, Commander commander, World world, Texture texture, Vector2 spawnPoint) {
		// TODO Auto-generated constructor stub
		super(id,commander,world,texture,spawnPoint);
		this.type=EntityManager.EntityType.ZOMBIE;
		this.sprite.setRegion(new TextureRegion(this.sheet,0,0,32,32));
		createBody(world,spawnPoint.x,spawnPoint.y);
	}
}