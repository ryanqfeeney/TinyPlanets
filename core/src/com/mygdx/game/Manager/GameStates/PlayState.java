package com.mygdx.game.Manager.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.*;
import com.mygdx.game.Manager.Entity.Planets.Kerbin.Kerbin;
import com.mygdx.game.Manager.Entity.Planets.Kerbin.Minmus;
import com.mygdx.game.Manager.Entity.Planets.Kerbin.Mun;
import com.mygdx.game.Manager.Entity.Planets.Nars.Codde117;
import com.mygdx.game.Manager.Entity.Planets.Nars.Nars;
import com.mygdx.game.Manager.Entity.Planets.Fiji.Fiji;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Huds.PlayStateHud;
import com.mygdx.game.Manager.Utility.PlayStateInputUtil;
import com.mygdx.game.Manager.Utility.Sprites.Sprite;


import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.Manager.Entity.Planets.Cbody.calculateEscapteVel;

public class PlayState extends GameState {

    double camX, camY, camRote;
    float scale = 128 * 1000f;


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

        planets.add(new Sun(this));                         //0

        planets.add(new Fiji(planets.get(0),this));         //1
        planets.add(new Kerbin(planets.get(0),this));       //2
        planets.add(new Mun(planets.get(2),this));          //3
        planets.add(new Minmus(planets.get(2),this));       //4
        planets.add(new Nars(planets.get(0),this));         //5
        planets.add(new Codde117(planets.get(5),this));     //6

        int numOfKlobs = 1;
        klobjects.add(returnOrbitingKlob(planets.get((int)(Math.random() * planets.size())), true,true));
        for (int i = 1; i < numOfKlobs; i++){
            int q = (int) (Math.random() * planets.size()) ;
            Klobject k = returnOrbitingKlob(planets.get(q), false,true);
            klobjects.add(k);
        }



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
            //klob.drawPath();
        }
        klobjects.get(0).drawPath();

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

    public Klobject returnOrbitingKlob(Cbody parent, boolean pathBool, boolean circleBool){

        Klobject k = new Klobject();
        Random random = new Random();

        do{
            double ang = (Math.random() * 360);
            double dist = parent.getName().equals("sun") ?
                    Math.random()*30_000_000_000.0 + getPlanets().get(0).getRadius() :
                    Math.random()*(parent.getSoir()-parent.getRadius()) + parent.getRadius();

            double eVel = calculateEscapteVel(parent, dist);
            double vel = (random.nextBoolean() ? 1 : -1)*(.8*eVel * Math.random());
            double rr  = (random.nextBoolean() ? 1 : -1)*(Math.random() * 20);



            k = new Klobject( parent,this, ang, dist, vel, rr, pathBool,circleBool);


        } while (k.getPeri() < parent.getRadius() || k.getApoap() > parent.getSoir() || k.getApoap() < 0 );
        return  k;
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
