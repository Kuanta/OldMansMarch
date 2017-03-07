package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.commanders.Commander;


public class Projectile extends Entity {

    public Projectile(int id, Commander commander, World world, Texture sheet, Vector2 initPos) {
        super(id, commander, world, sheet, initPos);
    }
}
