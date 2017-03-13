package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.oldmansmarch.AssetsManager;


public class UnitTemplate {
    private int id;
    public String name;
    private boolean isCaster;
    private int projectileTemplateId;
    private float health;
    private float damage;
    private float speed;
    private float cost;
    private float width;
    private float height;
    private Array<AnimationTemplate> animTemplates;
    private TextureRegion preview;
    
    //Getters
    public boolean isCaster(){
    	return this.isCaster;
    }
    public float getHealth(){
    	return this.health;
    }
    public float getDamage(){
    	return this.damage;
    }
    public float getSpeed(){
    	return this.speed;
    }
    public float getCost(){
    	return this.cost;
    }
    public int getProjectileTemplateId(){
    	return this.projectileTemplateId;
    }
    public float getWidth(){
    	return this.width;
    }
    public float getHeight(){
    	return this.height;
    }
    public TextureRegion getPreview(){
		return this.preview;
	}
    public Array<AnimationTemplate> getAnimationTemplates(){
    	return this.animTemplates;
    }
    
    //Setters
    public void setPreview(){
    	this.preview=this.animTemplates.get(0).frames[0];
    }
    public UnitTemplate(int id,String name,int isCaster,int projectileTemplateId,float health,float damage,float speed,float width,float height,float cost){
    	this.id=id;
    	this.name=name;
    	this.health=health;
    	this.damage=damage;
    	this.cost=cost;
    	this.speed=speed;
    	this.width=width;
    	this.height=height;
    	this.animTemplates=new Array<AnimationTemplate>();
    	if(isCaster==1){
    		this.isCaster=true;
    	}else{
    		this.isCaster=false;
    	}
    	this.projectileTemplateId=projectileTemplateId;
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
