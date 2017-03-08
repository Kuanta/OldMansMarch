package com.oldmansmarch.entities.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.EntityManager;
import com.oldmansmarch.entities.Unit;

/**
 * Created by Kuanta on 8.03.2017.
 */

public class Mounted extends Unit {
    public Mounted(int id, Commander commander, World world, Texture sheet, Vector2 initPos) {
        super(id, commander, world, sheet, initPos, true,true);
        this.unitType= EntityManager.UnitType.MOUNTED;
        this.sprite.setRegion(new TextureRegion(this.sheet,0,0,48,48));
        this.health=2;
        this.damage=2;
    }
}
