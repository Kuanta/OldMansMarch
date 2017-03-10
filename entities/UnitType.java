package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Kuanta on 10.03.2017.
 */

public class UnitType {
    private int id;
    public String name;
    private float health;
    private float damage;
    private float cost;
    private AnimationTemplate[] animTemplates;
    private class AnimationTemplate{
        public String name;
        public int sheetId;
        TextureRegion[] frames;
        public AnimationTemplate(int frameCount){
            this.frames=new TextureRegion[frameCount];
        }
    }
}
