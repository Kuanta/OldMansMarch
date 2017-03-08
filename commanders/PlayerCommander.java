package com.oldmansmarch.commanders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.Configuration;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.EntityManager.EntityType;

public class PlayerCommander extends Commander{
	
	public PlayerCommander(World world,float posX,EntityManager em){
		super(world,posX,em);
		currentType=EntityManager.UnitType.TEST; //Set as default
		this.health=Configuration.playerHealth;
		this.faction=EntityManager.Faction.PLAYER;
		createWall(world,this.em);
		this.gold=10;
		this.health=100;
		this.direction=1;
		
	}
	public void update(float delta){
		super.update(delta);
		if(this.gold<=15){
			this.gold+=Configuration.goldPerSec*delta;
		}
	}
}
