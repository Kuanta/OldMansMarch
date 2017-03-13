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
    }
    public void dispose(){
        for(Integer key:textures.keySet()){
            textures.get(key).dispose();
        }
    }
}
