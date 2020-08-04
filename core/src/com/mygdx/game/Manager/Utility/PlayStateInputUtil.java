package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;
import math.geom2s.Point2S;

import java.awt.*;


public class PlayStateInputUtil implements InputProcessor {


    int sp = 0;
    float zm = 1.05f;
    float rotation = 0;
    float rotationRate = .5f;
    int look = 1;
    float camLX = 0;
    float camLY = 0;
    boolean camLock = true;
    double mult = Math.pow(2,sp);
    double dvScale = 5.0;

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
            if (ps.getScale() > 1) {
                ps.setScale(ps.getScale() / zm);
            }
        }
        else if(amount == 1){
            ps.setScale(ps.getScale()*zm);
        }
        for (Klobject klob : ps.getKlobjects()){
            klob.getSprite().setScale((float)(1f/ps.getScale()));
            klob.getSprite().setOrigin(klob.getSprite().getWidth() / 2, klob.getSprite().getHeight() / 2);
            klob.scale = ps.getScale();
        }
        for (Cbody cb : ps.getPlanets()){
            cb.getSprite().setSize((float)(cb.getRadius()/ps.getScale()),(float)(cb.getRadius()/ps.getScale()));
            cb.getSprite().setOrigin(cb.getSprite().getWidth() / 2, cb.getSprite().getHeight() / 2);
            cb.scale = ps.getScale();
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
        //camera.update();
    }
    public void lookAtCbody(Cbody cb){
        if (!camLock){return;}
        p = cb;
        x = p.getX();
        y = p.getY();
        ps.setCamX((x+camLX));
        ps.setCamY((y+camLY));
        //camera.update();
    }
    Point2D vel, velN;
    double rote;
    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ) {
            if(mult == 1) {
                vel = ps.getKlobjects().get(0).getVel();
                rote = ps.getKlobjects().get(0).getRotation();
                velN = vel.plus(new Point2D(Math.cos(rote), Math.sin(rote)).scale(dvScale));
                ps.getKlobjects().get(0).setVel(velN);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            if(mult == 1) {
                vel = ps.getKlobjects().get(0).getVel();
                rote = ps.getKlobjects().get(0).getRotation();
                velN = vel.plus(new Point2D(-Math.cos(rote), -Math.sin(rote)).scale(dvScale));
                ps.getKlobjects().get(0).setVel(velN);
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) ) {
            camLX=0;
            camLY=0;
            look=-1;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.E) ) {
            if(mult == 1) {
                Klobject klob = ps.getKlobjects().get(0);
                float rr = 0;//SAS THING GARBAGE TEMP CODE
                if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                    klob.setRR(klob.getRR() + klob.getDRR());
                    rr+=klob.getSasDrr();//SAS THING GARBAGE TEMP CODE
                }
                else if(Gdx.input.isKeyPressed(Input.Keys.E)){
                    klob.setRR(klob.getRR() + klob.getDRR());
                    rr-=klob.getSasDrr();//SAS THING GARBAGE TEMP CODE
                }
                klob.setRR(rr);//SAS THING GARBAGE TEMP CODE
            }

        }
        else{//SAS THING GARBAGE TEMP CODE
            Klobject klob = ps.getKlobjects().get(0);
            klob.setRR(0);
        }


//        if (Gdx.input.isKeyPressed(Input.Keys.A) ) {
//            camera.zoom *= zm;
//           // mv *=mvScale;
//
//            if(camera.zoom == 0){
//                camera.zoom = 1;
//            }
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            camera.translate(-mv, 0, 0);
//            camLX-=mv;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            camera.translate(mv, 0, 0);
//            camLX+=mv;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            camera.translate(0, -mv, 0);
//            camLY-=mv;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            camera.translate(0, mv, 0);
//            camLY+=mv;
//        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {


            camera.rotate(-rotationRate, 0, 0, 1);
            bCamera.rotate(-rotationRate, 0, 0, 1);
            rotation -= rotationRate;
            ps.setCamRote(rotation);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.rotate(rotationRate, 0, 0, 1);
            bCamera.rotate(rotationRate, 0, 0, 1);
            rotation += rotationRate;
            ps.setCamRote(rotation); //Check for setCamRote. It has a negative there
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
                sp = sp - 1;//Set set to zero sets mult to 1
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
            sp = sp + 1;//Set set to zero sets mult to 1
            mult = Math.pow(2,sp);
            ps.getHud().setMult(mult);
            for (int i = 0; i < ps.getPlanets().size(); i++){
                ps.getPlanets().get(i).setMultiplier( mult );
            }
            for (Klobject klob : ps.getKlobjects()){
                klob.setMultiplier(mult);
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
            //System.out.println("WARP UP      <-------");
            sp = 0;//Set set to zero sets mult to 1
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
                if(ps.getPlanets().get(i).getSprite().getBoundingRectangle().contains(input.x,input.y)||
                        checkCircleClick(input.x,input.y,ps.getPlanets().get(i))) {
                    camLX = 0;
                    camLY = 0;
                    camLock = true;
                    p = ps.getPlanets().get(i);
                    //camera.position.slerp(new Vector3((float)p.getX(),(float)p.getY(),0f),.2f);
                    ps.setCamX(p.getX());
                    ps.setCamY(p.getY());
                    look = i;
                    break;
                }
            }
            for (int i = 0; i < ps.getKlobjects().size(); i++){
                if(ps.getKlobjects().get(i).getSprite().getBoundingRectangle().contains(input.x,input.y) ||
                    checkCircleClick(input.x,input.y,ps.getKlobjects().get(i))) {
                    camLX = 0;
                    camLY = 0;
                    camLock=true;
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
            int dx = Gdx.input.getDeltaX();
            int dy = Gdx.input.getDeltaY();


            double rote = Math.toRadians(rotation);
            Point2D delta = new Point2D( (dx * camera.zoom), (dy * camera.zoom));
            delta = delta.rotate(-rote);

            if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {

                Point2D click1 = new Point2D(Gdx.input.getX(),Gdx.input.getY()).
                        minus(new Point2D((float)ps.getScreenWidth()/2,(float)ps.getScreenHeight()/2));

                Point2D click2 = click1.plus(new Point2D(dx,dy));
                double ang = Math.atan2(click1.getY(),click1.getX())-Math.atan2(click2.getY(),click2.getX()) ;

                float r = (float) Math.toDegrees(ang);
                camera.rotate(r);
                bCamera.rotate(r);
                rotation -= r;
                ps.setCamRote(rotation);

            }
            else{
                if (camLock) {
                    camLX -= ps.getScale() * delta.getX();
                    camLY += ps.getScale() * delta.getY();
                } else {
                    ps.setCamX(ps.getCamX() - ps.getScale() * delta.getX());

                    ps.setCamY(ps.getCamY() + ps.getScale() * delta.getY());
                }

            }
        }

    }
    public boolean checkCircleClick(float x, float y, Cbody klob){
        Point2D click = new Point2D(x,y);
        Point2D circle = new Point2D((klob.getX()-ps.getCamX())/ps.getScale(),(klob.getY()-ps.getCamY())/ps.getScale());
        if (click.distance(circle) < klob.getCirlceSize()){
            return true;
        }
        return false;
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
