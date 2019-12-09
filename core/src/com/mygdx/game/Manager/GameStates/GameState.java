package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameState {

    OrthographicCamera camera;
    ExtendViewport viewport;
    
    public GameState(){
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        camera.position.set(0,0, 0);
        viewport = new ExtendViewport(width,height,camera);
        camera.update();
        camera.zoom = 8f;
    }


    public void render(float dt){

    }

    public void dispose(){

    }

    public void handleInput(){

    }

    public OrthographicCamera getCamera(){
        return camera;
    }
}
