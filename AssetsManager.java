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
    public HashMap<Sheets,Texture> textures;
    public AssetsManager(){
        textures=new HashMap<Sheets,Texture>();
        textures.put(Sheets.HUMANS,new Texture(Gdx.files.internal("soldiers.png")));
        textures.put(Sheets.UNDEADS,new Texture(Gdx.files.internal("undeads.png")));
        textures.put(Sheets.HEAVY_UNDEAD,new Texture(Gdx.files.internal("undeads.png")));
        textures.put(Sheets.SPELLS,new Texture(Gdx.files.internal("fireball.png")));
    }
    public void dispose(){
        for(Sheets sheet:textures.keySet()){
            textures.get(sheet).dispose();
        }
    }
}
