package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.Manager.Utility.Assets;

public class GameState {

    OrthographicCamera camera;
    ExtendViewport viewport;
    int width, height;
    public Assets assets;
    
    public GameState(Assets assets){
        this.assets = assets;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
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

    public int getScreenHeight() {
        return height;
    }

    public int getScreenWidth() {
        return width;
    }
}
