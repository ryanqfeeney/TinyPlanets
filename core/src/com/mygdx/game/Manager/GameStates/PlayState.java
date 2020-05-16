package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.*;
import com.mygdx.game.Manager.Utility.Huds.PlayStateHud;
import com.mygdx.game.Manager.Utility.PlayStateInputUtil;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayState extends GameState {

    double camX, camY;

    TextureAtlas cBTextureAtlas;
    TextureAtlas klobjectAtlas;

    SpriteBatch batch;
    ArrayList<Cbody> planets;
    ArrayList<Klobject> klobjects;
    PlayStateInputUtil pscu;


    PlayStateHud hud;
    Pixmap background;
    Sprite backgroundSprite;

    Viewport bViewport;
    OrthographicCamera bCamera;




    public PlayState(){


        super();

        camera.zoom = 3000f;
        cBTextureAtlas = new TextureAtlas("sprites.txt");
        klobjectAtlas = new TextureAtlas("spaceship.txt");
        batch = new SpriteBatch();
        hud = new PlayStateHud(this,batch);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        int c = (int)(Math.sqrt(Math.pow(width,2)+Math.pow(height,2))+1);

        background = new Pixmap(c, c, Pixmap.Format.RGBA8888);

        //create stars background
        for ( int rc = 0; rc < c; rc++ ) {
            for ( int cc = 0; cc < c; cc++ ) {
                // Set the pixel colour of the image n.b. x = cc, y = rc
                int i = new Random().nextInt(10000);
                if (i <= 1){
                    background.setColor(Color.WHITE);
                    //background.drawPixel(cc,rc,Color.WHITE.toIntBits());
                    background.fillCircle(cc,rc,1);
                }
                //else {   //background.drawPixel(cc,rc,Color.BLACK.toIntBits());    }
            }
        }

        //move stars to sprite
        backgroundSprite = new Sprite(new Texture(background));
        backgroundSprite.setScale(1);
        backgroundSprite.setSize(c,c);
        backgroundSprite.setOrigin(backgroundSprite.getWidth()/2, backgroundSprite.getHeight()/2);
        backgroundSprite.setPosition(-backgroundSprite.getWidth()/2, -backgroundSprite.getHeight()/2);

        background.dispose();

        bCamera = new OrthographicCamera(width,height);
        bCamera.position.set(0f,0f,0f);
        bCamera.update();
        bViewport = new ExtendViewport(width,height,bCamera);

        pscu = new PlayStateInputUtil(this);
        Gdx.input.setInputProcessor(pscu);

        //Plant Init
        planets = new ArrayList<>();
        klobjects = new ArrayList<>();

        planets.add(new Sun(this));
        planets.add(new Kerbin(planets.get(0),this));
        planets.add(new Mun(planets.get(1),this));
        planets.add(new Minmus(planets.get(1),this));

        klobjects.add(new Klobject( planets.get(2),this, 0,  500000.0, 250));
//        klobjects.add(new Klobject(this, planets.get(2), 0, 1000000.0, 250.0));
//        klobjects.add(new Klobject(this, planets.get(1), 0,  900000.0, 2500.0));
//        klobjects.add(new Klobject(this, planets.get(1), 0, 1000000.0, 2500.0));
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        bCamera.update();

        hud.update(dt);
        for (Cbody cb : planets){
            cb.update(dt);
        }
        for (Klobject klob : klobjects){
            klob.update(dt);
        }

        pscu.lookAtCbody();


        batch.setProjectionMatrix(bViewport.getCamera().combined);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        batch.setProjectionMatrix(camera.combined);

        for (Klobject klob : klobjects){
            klob.drawPath();
        }

        for (Cbody klob : planets){
            klob.drawPath();
        }

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
        //textureAtlas.dispose();
        batch.dispose();
        hud.dispose();

    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        float x = (float)((planet.getX()-sprite.getWidth()/2)-camX);
        float y = (float)((planet.getY()-sprite.getHeight()/2)-camY);
        sprite.setPosition(x, y);
        planet.getSprite().draw(batch);
    }
    private void drawKlob(Klobject klob) {
        Sprite sprite = klob.getKlobSprite();
        float x = (float)((klob.getX()-sprite.getWidth()/2)-camX);
        float y = (float)((klob.getY()-sprite.getHeight()/2)-camY);
        sprite.setPosition(x,y);
        klob.getKlobSprite().draw(batch);
    }




    @Override
    public void handleInput() {
        pscu.handleInput();
    }

    public TextureAtlas getCBTextures() {
        return cBTextureAtlas;
    }
    public TextureAtlas getKlobjectAtlas(){
        return klobjectAtlas;
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

    public OrthographicCamera getBCamera(){
        return bCamera;
    }

    public double getCamX(){return camX;}
    public double getCamY(){return camY;}
    public void setCamX(double x){camX = x;}
    public void setCamY(double y){camY = y;}


}
