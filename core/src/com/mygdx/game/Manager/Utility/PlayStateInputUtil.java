package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;


public class PlayStateInputUtil implements InputProcessor {


    float mv = 200000000;
    int sp = 0;
    double zm = 1.05;
    double rotation = 0;
    float rotationRate = .5f;
    int look = 1;
    float camLX = 0;
    float camLY = 0;
    boolean camLock = true;
    double mult = Math.pow(2,sp);

    PlayState ps;
    OrthographicCamera camera;
    OrthographicCamera bCamera;

    public PlayStateInputUtil(PlayState ps){
        this.ps = ps;
        this.camera = ps.getCamera();
        this.bCamera = ps.getBCamera();

    }

    @Override
    public boolean scrolled(int amount) {
        if(amount == -1){
            camera.zoom /= zm;
        }
        else if(amount == 1){
            camera.zoom *= zm;
            if (camera.zoom == 0){
                camera.zoom = .01f;
            }
        }
        return false;
    }
    Cbody p, klob;
    double x, y;
    public void lookAtCbody(){
        if (!camLock){return;}


        if(look == -1){
            klob = ps.getKlobjects().get(0);
            x = klob.getX();
            y = klob.getY();
        }
        else{
            p = ps.getPlanets().get(look);
            x = p.getX();
            y = p.getY();

        }

        ps.setCamX((x+camLX));
        ps.setCamY((y+camLY));

        camera.update();
    }
    public void lookAtCbody(Cbody cb){
        if (!camLock){return;}
        p = cb;
        x = p.getX();
        y = p.getY();
        ps.setCamX((x+camLX));
        ps.setCamY((y+camLY));
        camera.update();
    }
    Point2D vel, velN;
    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ) {
            vel = ps.getKlobjects().get(0).getVel();
            velN = vel.scale(1.002);
            ps.getKlobjects().get(0).setVel(velN);


        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            vel = ps.getKlobjects().get(0).getVel();
            velN = vel.scale(1/1.002);
            ps.getKlobjects().get(0).setVel(velN);

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) ) {
            camLX=0;
            camLY=0;
            look=-1;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q) ) {
            camera.zoom /= zm;
            //mv /= mvScale;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) ) {
            camera.zoom *= zm;
           // mv *=mvScale;

            if(camera.zoom == 0){
                camera.zoom = 1;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-mv, 0, 0);
            camLX-=150000;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(mv, 0, 0);
            camLX+=150000;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -mv, 0);
            camLY-=150000;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, mv, 0);
            camLY+=150000;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {


            camera.rotate(-rotationRate, 0, 0, 1);
            bCamera.rotate(-rotationRate, 0, 0, 1);
            rotation -= rotationRate;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(rotationRate, 0, 0, 1);
            bCamera.rotate(rotationRate, 0, 0, 1);
            rotation += rotationRate;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            camLock = !camLock;
            camLX = camLY = 0f;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {

            camLX = 0;
            camLY = 0;
            if(look == 1){ // change to zero to include sun in rotation
                look = ps.getPlanets().size()-1;
            }
            else if (look < 0){
                look = 1;
            }
            else{
                look -= 1;
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            camLX = 0;
            camLY = 0;

            if (look < 0){
                look = 1;
            }
            else if (look < (ps.getPlanets().size()-1)){ look=(look + 1)%(ps.getPlanets().size()); } // change to <= to flip back to planet 0
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            if (sp > 0) {
                //System.out.println("WARP DOWN      <-------");
                sp = sp - 1;
                mult = Math.pow(2,sp);
                ps.getHud().setMult(mult);
                for (int i = 0; i < ps.getPlanets().size(); i++){
                    ps.getPlanets().get(i).setMultiplier( Math.pow(2,sp) );
                }
                for (Klobject klob : ps.getKlobjects()){
                    klob.setMultiplier(mult);
                }

            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            //System.out.println("WARP UP      <-------");
            sp = sp + 1;
            mult = Math.pow(2,sp);
            ps.getHud().setMult(mult);
            for (int i = 0; i < ps.getPlanets().size(); i++){
                ps.getPlanets().get(i).setMultiplier( mult );
            }
            for (Klobject klob : ps.getKlobjects()){
                klob.setMultiplier(mult);
            }

        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){

            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 input = new Vector3(x, y, 0);
            camera.unproject(input);

            for (int i = 0; i < ps.getPlanets().size(); i++){
                if(ps.getPlanets().get(i).getSprite().getBoundingRectangle().contains(input.x,input.y)) {
                    camLX = 0;
                    camLY = 0;
                    p = ps.getPlanets().get(i);
                    //camera.position.slerp(new Vector3((float)p.getX(),(float)p.getY(),0f),.2f);
                    ps.setCamX(p.getX());
                    ps.setCamY(p.getY());
                    look = i;
                    break;
                }
            }
            for (int i = 0; i < ps.getKlobjects().size(); i++){
                if(ps.getKlobjects().get(i).getKlobSprite().getBoundingRectangle().contains(input.x,input.y)) {
                    camLX = 0;
                    camLY = 0;
                    p = ps.getKlobjects().get(i);
                    //camera.position.slerp(new Vector3((float)p.getX(),(float)p.getY(),0f),.2f);
                    ps.setCamX(p.getX());
                    ps.setCamY(p.getY());
                    look = -(i+1);
                    break;
                }
            }
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            int x1 = Gdx.input.getDeltaX();
            int y1 = Gdx.input.getDeltaY();
            double rote = Math.toRadians(rotation);
            Point2D delta = new Point2D( (x1 * camera.zoom), (y1 * camera.zoom));
            delta = delta.rotate(-rote);

            if (camLock){
                camLX -= delta.getX();
                camLY += delta.getY();
            }
            else{
                ps.setCamX( ps.getCamX() - delta.getX()); ;
                ps.setCamY( ps.getCamY() + delta.getY());
            }
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

}
