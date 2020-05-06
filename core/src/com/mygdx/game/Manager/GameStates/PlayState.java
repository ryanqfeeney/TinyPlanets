package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.*;
import com.mygdx.game.Manager.Utility.Huds.PlayStateHud;
import com.mygdx.game.Manager.Utility.PlayStateInputUtil;

import java.util.ArrayList;

public class PlayState extends GameState {

    TextureAtlas textureAtlas;
    SpriteBatch batch;;
    ArrayList<Cbody> planets;
    ArrayList<Klobject> klobjects;
    PlayStateInputUtil pscu;

    PlayStateHud hud;


    public PlayState(){


        super();

        camera.zoom = 400000f;
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();
        hud = new PlayStateHud(this,batch);
        pscu = new PlayStateInputUtil(this);

        Gdx.input.setInputProcessor(pscu);

        planets = new ArrayList<>();
        klobjects = new ArrayList<>();

        planets.add(new Sun());
        planets.add(new Kerbin(planets.get(0)));
        planets.add(new Mun(planets.get(1)));
        planets.add(new Minmus(planets.get(1)));

        klobjects.add(new Klobject(this, planets.get(1)));


    }

    @Override
    public void render(float dt) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        hud.update(dt);
        for (Cbody cb : planets){
            cb.update(dt);
        }
        for (Klobject klob : klobjects){
            klob.update(dt);
        }


        pscu.lookAtCbody();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        for (Cbody cb : planets) {
            drawPlanet(cb);
        }
        for (Klobject klob : klobjects){
            drawKlob(klob);
        }
        batch.end();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }



    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
        batch.dispose();
        hud.dispose();

    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        sprite.setPosition(((float)(planet.getX()-sprite.getWidth()/2)),
                (float)(planet.getY()-sprite.getHeight()/2));

        planet.getSprite().draw(batch);
    }
    private void drawKlob(Klobject klob) {
        Sprite sprite = klob.getSprite();
        sprite.setPosition((float)(klob.getX()-sprite.getWidth()/2),
                (float)(klob.getY()-sprite.getHeight()/2));

        klob.getSprite().draw(batch);
    }



    @Override
    public void handleInput() {
        pscu.handleInput();
    }

    public ArrayList<Cbody> getPlanets(){
        return planets;
    }

    public ArrayList<Klobject> getKlobjects() {
        return klobjects;
    }

    public PlayStateHud getHud() {
        return hud;
    }
}
