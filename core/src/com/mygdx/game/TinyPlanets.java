package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Manager.GameStateManager;

public class TinyPlanets extends ApplicationAdapter {

	static final float STEP_TIME = 1f / 60f;
	static final int VELOCITY_ITERATIONS = 6;
	static final int POSITION_ITERATIONS = 2;


	GameStateManager gsm;

	@Override
	public void create () {


		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		gsm = new GameStateManager();
	}


	@Override
	public void render () {

		float dt = Gdx.graphics.getDeltaTime();
		System.out.println(dt);
		if (dt > .025f){
			dt = .025f;
		}


		gsm.handleInput();
		gsm.render(dt);

	}

	@Override
	public void dispose () {
		gsm.dispose();
	}



}
