package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entitie.Planets.*;

import java.util.ArrayList;

public class PlayState extends GameState {

    TextureAtlas textureAtlas;
    SpriteBatch batch;
    ArrayList<Cbody> planets;
    boolean camLock;

    public PlayState(){


        super();
        camLock = true;
        camera.zoom = 500000f;
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();

        planets = new ArrayList<>();

        planets.add(new Sun());
        planets.add(new Kerbin(planets.get(0)));
        planets.add(new Mun(planets.get(1)));
        planets.add(new Minmus(planets.get(1)));


    }
    int look = 0;
    @Override
    public void render(float dt) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < planets.size(); i++){
            planets.get(i).update(dt);
        }

        if (camLock){
            lookAtCbody(planets.get(look));
        }


        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (int i = 0; i < planets.size(); i++) {
            drawPlanet(planets.get(i));
        }
        batch.end();
        camera.update();

    }


    public void lookAtCbody(Cbody cb){
        Cbody p = cb;
        float x = p.getX();
        float y = p.getY();
        camera.position.x = x;
        camera.position.y = y;
        camera.update();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        sprite.setPosition(planet.getX()-sprite.getWidth()/2,
                           planet.getY()-sprite.getHeight()/2);

        planet.getSprite().draw(batch);
    }

    int mv = 20000000;
    double mvScale = 1.2;
    int sp = 0;
    double zm = 1.05;

    @Override
    public void handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom /= zm;
            mv /=mvScale;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom *= zm;
            mv *=mvScale;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-mv, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(mv, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -mv, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, mv, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.rotate(-.5f, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(.5f, 0, 0, 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            camLock = !camLock;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (look > 0) look -= 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            if (look < planets.size()-1) look += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            if (sp > 0) {
                sp = sp - 1;
                for (int i = 0; i < planets.size(); i++){
                    planets.get(i).setMultiplier( Math.pow(3,sp) );
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            sp = sp + 1;
            for (int i = 0; i < planets.size(); i++){
                planets.get(i).setMultiplier( Math.pow(3,sp) );
            }
        }

//		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);
//
//		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
//		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
//
//		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);

    }
}
