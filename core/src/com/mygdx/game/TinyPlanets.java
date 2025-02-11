package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Manager.GameStateManager;
import com.mygdx.game.Manager.Utility.Assets;

public class TinyPlanets extends ApplicationAdapter {

	Assets assets;
	GameStateManager gsm;

	@Override
	public void create () {


		assets = new Assets();
		assets.load( );
		gsm = new GameStateManager(assets);

	}


	@Override
	public void render () {

		float dt = Gdx.graphics.getDeltaTime();
		if (dt > .025f){
			dt = .025f;
		}

		gsm.handleInput();
		gsm.render(dt);

	}

	@Override
	public void dispose () {
		gsm.dispose();
		assets.dispose();
	}



}
