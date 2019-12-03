package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entitie.Planets.Cbody;
import com.mygdx.game.Manager.Entitie.Planets.Kerbin;
import com.mygdx.game.Manager.Entitie.Planets.Mun;
import com.mygdx.game.Manager.Entitie.Planets.Sun;

import java.util.ArrayList;

public class PlayState extends GameState {

    TextureAtlas textureAtlas;
    SpriteBatch batch;

    ArrayList<Cbody> planets;

    public PlayState(){


        super();
        camera.zoom = 500000f;
        textureAtlas = new TextureAtlas("sprites.txt");
        batch = new SpriteBatch();

        planets = new ArrayList<Cbody>();
        planets.add(new Sun());
        planets.add(new Kerbin(planets.get(0)));
        planets.add(new Mun(planets.get(1)));
        cameraTest();

    }

    public void cameraTest(){
        Cbody p = planets.get(1);

        float x = p.getX();
        float y = p.getY();

        System.out.println(x);
        System.out.println(y);
        System.out.println();

        camera.translate((float)(1.36*Math.pow(10,10)),0f,0f);


    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);


        for (int i = 0; i < planets.size(); i++){
            planets.get(i).update(dt);

        }

        batch.begin();

        for (int i = 0; i < planets.size(); i++){
            drawPlanet(planets.get(i));

        }


        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        sprite.setPosition(planet.getX()-sprite.getWidth()/2, planet.getY()-sprite.getHeight()/2);
        sprite.draw(batch);
    }

    int mv = 20000000;
    int sp = 0;
    double zm = 1.05;

    @Override
    public void handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom /= zm;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom *= zm;
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            if (sp > 0) {
                sp = sp - 1;
                for (int i = 0; i < planets.size(); i++){
                    planets.get(i).setMultiplier( Math.pow(2,sp) );
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            sp = sp + 1;
            System.out.println(sp);
            for (int i = 0; i < planets.size(); i++){
                planets.get(i).setMultiplier( Math.pow(2,sp) );
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
