package com.oldmansmarch;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	GameManager bigGuy;
	@Override
	public void create () {
		
		//bigGuy=new GameManager();
		XmlReader reader=new XmlReader();
		try {
			Element root=reader.parse(Gdx.files.internal("types.xml"));
			int childCount;
			childCount=root.getChildCount();
			for(int i=0;i<childCount;i++){
				Element child=root.getChild(i);
				int health=child.getIntAttribute("health");
				String name=child.getAttribute("name");
				System.out.println(name+" has "+health+" health");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
