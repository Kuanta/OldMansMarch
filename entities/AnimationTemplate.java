package com.oldmansmarch.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.oldmansmarch.AssetsManager;

public class AnimationTemplate {
	public String name;
    public int sheetId;
    public float animDuration;
    TextureRegion[] frames;
    public AnimationTemplate(TextureRegion[] frames){
        this.frames=frames;
        this.animDuration=1f/10f;
    }
    public static AnimationTemplate addAnimationTemplate(Element animationElement ,AssetsManager assetsMan){
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
    	return temp;
    }
}
