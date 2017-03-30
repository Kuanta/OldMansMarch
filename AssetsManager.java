package com.oldmansmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;


public class AssetsManager {

    public HashMap<Integer,Texture> textures;
    public AssetsManager(){
        textures=new HashMap<Integer,Texture>();
        textures.put(0,new Texture(Gdx.files.internal("soldiers.png")));
        textures.put(1,new Texture(Gdx.files.internal("undeads.png")));
        textures.put(2,new Texture(Gdx.files.internal("fireball.png")));
        textures.put(3,new Texture(Gdx.files.internal("infantryRight.png")));
        textures.put(4,new Texture(Gdx.files.internal("heavyInfantryRight.png")));
        textures.put(5,new Texture(Gdx.files.internal("mageRight.png")));
        textures.put(6,new Texture(Gdx.files.internal("mountedRight.png")));
        textures.put(7,new Texture(Gdx.files.internal("kingRight.png")));
    }
    public void dispose(){
        for(Integer key:textures.keySet()){
            textures.get(key).dispose();
        }
    }
}
