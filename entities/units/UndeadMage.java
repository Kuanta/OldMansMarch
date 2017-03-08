package com.oldmansmarch.entities.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oldmansmarch.commanders.Commander;
import com.oldmansmarch.entities.Caster;
import com.oldmansmarch.entities.EntityManager;

/**
 * Created by Kuanta on 8.03.2017.
 */

public class UndeadMage extends Caster {
    public UndeadMage(int id, Commander commander, EntityManager em, World world, Texture sheet, Vector2 initPos) {
        super(id, commander, em, world, sheet, initPos, true,true);
        this.unitType=EntityManager.UnitType.UNDEAD_MAGE;
        this.sprite.setRegion(new TextureRegion(this.sheet,0,4*32,32,32));
        this.currentProjectile= EntityManager.ProjectileType.FIREBALL;
        this.damage=0;
        this.health=1;
    }
}
