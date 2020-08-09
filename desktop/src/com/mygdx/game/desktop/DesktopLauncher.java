package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.mygdx.game.TinyPlanets;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.useHDPI =true;
		cfg.title = "TinyPlanets";
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.fullscreen = true;
		new LwjglApplication(new TinyPlanets(), cfg);
	}


}
