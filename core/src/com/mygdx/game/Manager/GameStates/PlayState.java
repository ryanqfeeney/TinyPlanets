package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entitie.Planets.*;
import com.mygdx.game.Manager.Utility.PlayStateCameraUtil;

import java.util.ArrayList;

public class PlayState extends GameState {

    TextureAtlas textureAtlas;
    SpriteBatch batch;
    ArrayList<Cbody> planets;
    PlayStateCameraUtil pscu;

    public PlayState(){


        super();
        camera.zoom = 400000f;
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();
        pscu = new PlayStateCameraUtil(this);

        planets = new ArrayList<>();

        planets.add(new Sun());
        planets.add(new Kerbin(planets.get(0)));
        planets.add(new Mun(planets.get(1)));
        planets.add(new Minmus(planets.get(1)));


    }


    @Override
    public void render(float dt) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < planets.size(); i++){
            planets.get(i).update(dt);
        }


        pscu.lookAtCbody();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < planets.size(); i++) {
            drawPlanet(planets.get(i));
        }
        batch.end();

        camera.update();

    }



    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
        batch.dispose();

    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        sprite.setPosition(planet.getX()-sprite.getWidth()/2,
                           planet.getY()-sprite.getHeight()/2);

        planet.getSprite().draw(batch);
    }


    @Override
    public void handleInput() {
        pscu.handleInput();
    }

    public ArrayList<Cbody> getPlanets(){
        return planets;
    }
}
