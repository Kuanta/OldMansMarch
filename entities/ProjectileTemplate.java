package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.oldmansmarch.AssetsManager;

public class ProjectileTemplate {
	
	private int id;
    public String name;

    private float damage;
    private float speed;
    private float width;
    private float height;
    private Array<AnimationTemplate> animTemplates;
    
  //Getters
    public float getDamage(){
    	return this.damage;
    }
    public float getSpeed(){
    	return this.speed;
    }
    public float getWidth(){
    	return this.width;
    }
    public float getHeight(){
    	return this.height;
    }
    public Array<AnimationTemplate> getAnimationTemplates(){
    	return this.animTemplates;
    }
    public ProjectileTemplate(int id,String name,float damage,float speed,float width,float height){
    	this.id=id;
    	this.name=name;
    	this.damage=damage;
    	this.speed=speed;
    	this.width=width;
    	this.height=height;
    	this.animTemplates=new Array<AnimationTemplate>();
    }
	public void addAnimationTemplate(Element animationElement ,AssetsManager assetsMan){
    	int frameCount=animationElement.getChildCount();
    	TextureRegion[] frames=new TextureRegion[frameCount];
    	for(int i=0;i<frameCount;i++){
    		Element frame=animationElement.getChild(i);
    		int row=frame.getIntAttribute("row");
			int column=frame.getIntAttribute("column");
			int width=frame.getIntAttribute("width");
			int height=frame.getIntAttribute("height");
			frames[i]=new TextureRegion(assetsMan.textures.get(animationElement.getIntAttribute("sheetId")),column*width,row*height,width,height);
    	}
    	AnimationTemplate temp=new AnimationTemplate(frames);
    	this.animTemplates.add(temp);
    }
}
