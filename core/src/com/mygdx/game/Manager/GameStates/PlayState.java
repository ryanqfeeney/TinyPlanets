package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.*;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Huds.PlayStateHud;
import com.mygdx.game.Manager.Utility.PlayStateInputUtil;


import java.util.ArrayList;

public class PlayState extends GameState {

    double camX, camY, camRote;
    float scale = 12000f;


    SpriteBatch batch;
    Sprite backgroundSprite;

    PlayStateHud hud;
    Viewport bViewport;
    OrthographicCamera bCamera;

    ArrayList<Cbody> planets;
    ArrayList<Klobject> klobjects;
    PlayStateInputUtil pscu;


    public PlayState(Assets assets){


        super(assets);

        camera.zoom = 1f;
        batch = new SpriteBatch();

        int c = (int)(Math.sqrt(Math.pow(width,2)+Math.pow(height,2))+1);

        //move stars to sprite
        backgroundSprite = new Sprite(assets.manager.get(Assets.background));
        backgroundSprite.setScale(1);
        backgroundSprite.setSize(c,c);
        backgroundSprite.setOrigin(backgroundSprite.getWidth()/2, backgroundSprite.getHeight()/2);
        backgroundSprite.setPosition(-backgroundSprite.getWidth()/2, -backgroundSprite.getHeight()/2);


        bCamera = new OrthographicCamera(width,height);
        bCamera.position.set(0f,0f,0f);
        bCamera.update();
        bViewport = new ExtendViewport(width,height,bCamera);

        pscu = new PlayStateInputUtil(this);
        Gdx.input.setInputProcessor(pscu);

        //Plant Init
        planets = new ArrayList<>();
        klobjects = new ArrayList<>();

        planets.add(new Sun(this));                   //011
        planets.add(new Kerbin(planets.get(0),this)); //1
        planets.add(new Mun(planets.get(1),this));    //2
        planets.add(new Minmus(planets.get(1),this)); //3
        planets.add(new Nars(planets.get(0),this));   //4

       // klobjects.add(new Klobject( planets.get(4),this, Cbody.testROT,  1000000.0, -1140 ));
        klobjects.add(new Klobject( planets.get(4),this, 3*Math.PI/2,  5000000.0, 350 ));
        hud = new PlayStateHud(this,batch);

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


        for (int i = planets.size()-1; i >= 0; i--){
            planets.get(i).drawPath();
        }

        for (Klobject klob : klobjects){
            klob.drawPath();
        }

        for (int i = planets.size()-1; i >= 0; i--){
            planets.get(i).drawCircle();
        }

        for (Klobject klob : klobjects){
            klob.drawCircle();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Cbody cb : planets) {
            drawPlanet(cb);
        }
        for (Klobject cb : klobjects){
            drawKlob(cb);
        }
        batch.end();

        hud.draw(batch);
    }



    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        hud.dispose();
        for (Klobject klob : klobjects){
            klob.dispose();
        }

        for (Cbody cb : planets){
            cb.dispose();
        }

    }

    private void drawPlanet(Cbody planet) {
        Sprite sprite = planet.getSprite();
        float x = (float)((planet.getX()-camX)/scale-sprite.getWidth()/2);
        float y = (float)((planet.getY()-camY)/scale-sprite.getHeight()/2);
        sprite.setPosition(x, y);
        planet.getSprite().draw(batch);
    }
    private void drawKlob(Klobject klob) {
        Sprite sprite = klob.getSprite();
        float x = (float)((klob.getX()-camX)/scale-sprite.getWidth()/2);
        float y = (float)((klob.getY()-camY)/scale-sprite.getHeight()/2);
        sprite.setPosition(x,y);
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

    public OrthographicCamera getBCamera(){
        return bCamera;
    }

    public double getCamX(){return camX;}
    public double getCamY(){return camY;}
    public double getCamRotation(){return camRote;}
    public void setCamX(double x){camX = x;}
    public void setCamY(double y){camY = y;}
    public void setCamRote(double rr){camRote = -rr;}
    public float getScale(){return scale;}
    public void setScale(float sc){scale =sc;}


}
