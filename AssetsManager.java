package com.oldmansmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

/**
 * Created by Kuanta on 8.03.2017.
 */

public class AssetsManager {
    //Image Rectangles (Place in the spriteSheet)
    public static Rectangle infantryRect=new Rectangle(0,4*48,48,48);
    public static Rectangle zombieRect=new Rectangle(0,0,32,32);
    public static Rectangle wizardRect=new Rectangle(6*48,4*48,48,48);
    public static Rectangle mountedRect=new Rectangle(0,0,48,48);
    public static Rectangle heavyUndead=new Rectangle(0,0,32,32);
    public static enum Sheets{
        HUMANS,UNDEADS,SPELLS,HEAVY_UNDEAD
    }
    public HashMap<Integer,Texture> textures;
    public AssetsManager(){
        textures=new HashMap<Integer,Texture>();
        textures.put(0,new Texture(Gdx.files.internal("soldiers.png")));
        textures.put(1,new Texture(Gdx.files.internal("undeads.png")));
        textures.put(2,new Texture(Gdx.files.internal("fireball.png")));
    }
    public void dispose(){
        for(Integer key:textures.keySet()){
            textures.get(key).dispose();
        }
    }
}
