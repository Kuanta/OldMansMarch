package com.oldmansmarch;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;


public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	//GameManager bigGuy;
	AssetsManager assetsMan;


	@Override
	public void create () {

		assetsMan=new AssetsManager();
		//bigGuy=new GameManager();
		XmlReader reader=new XmlReader();
		try {
			XmlReader.Element root=reader.parse(Gdx.files.internal("unitTypes.xml"));
			for(int i=0;i<root.getChildCount();i++){
				XmlReader.Element type=root.getChild(i);
				String name=type.getAttribute("name");
				int health=type.getIntAttribute("health");
				int damage=type.getIntAttribute("damage");
				XmlReader.Element animations=type.getChild(0);
				int animationCount=animations.getChildCount();
				Animation[] anims=new Animation[animationCount];
				for(int j=0;j<animationCount;j++){
					XmlReader.Element anim=animations.getChild(j);
					Texture sheet=assetsMan.textures.get(anim.getIntAttribute("sheetId"));
					int frameCount=anim.getChildCount();
					TextureRegion[] regions=new TextureRegion[frameCount];
					for(int k=0;k<frameCount;k++){
						XmlReader.Element frame=anim.getChild(k);
						int row=frame.getIntAttribute("row");
						int column=frame.getIntAttribute("column");
						int width=frame.getIntAttribute("width");
						int height=frame.getIntAttribute("height");
						regions[k]=new TextureRegion(sheet,column*width,row*height,width,height);
					}
					anims[j]=new Animation(1f/20f,regions);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//bigGuy.render();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		//bigGuy.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}

	@Override
	public void dispose () {
		//bigGuy.dispose();
	}
}
