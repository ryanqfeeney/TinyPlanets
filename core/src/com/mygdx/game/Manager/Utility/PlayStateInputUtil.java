package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Manager.Entities.Klobjects.Klobject;
import com.mygdx.game.Manager.Entities.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;



public class PlayStateInputUtil implements InputProcessor {


    int sp = 0;
    int[] warp = new int[] {1,2,3,4,5,10,50,100,1000,10000,100000,250000,750000,1000000,2500000};
    int spMax = warp.length-1;

    float zm = 1.08f;
    float rotation = 0;
    float rotationRate = .50f;
    int look = -1;
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
    public boolean scrolled(float x, float y) {
        if( y == -1 ){
            if (ps.getScale() > 1) {
                ps.setScale(ps.getScale() / zm);
            }
        }
        else if(y == 1){
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
            cb.getTvg().setSize((float)(cb.getRadius()/ps.getScale()),(float)(cb.getRadius()/ps.getScale()));

        }
        return false;
    }


    Cbody p, klob;
    double x, y;
    public void lookAtCbody(){
        if (!camLock){return;}


        if(look < 0){
            klob = ps.getKlobjects().get(Math.abs(look+1));
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
            Klobject klob =  ps.getKlobjects().get(0);
            if(mult == 1 && klob.getThrottle() < klob.getMaxThrottle()) {
                klob.setThrottle(klob.getThrottle()+klob.getdThrottle());
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            Klobject klob =  ps.getKlobjects().get(0);
            if(mult == 1) {
                if(klob.getThrottle()-klob.getdThrottle() < 0){
                    klob.setThrottle(0);
                }
                else{
                    klob.setThrottle(klob.getThrottle()-klob.getdThrottle());
                }
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) ) {
            camLX=0;
            camLY=0;
            camLock = true;
            look=-1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.E) ) {
            if(mult == 1) {
                Klobject klob = ps.getKlobjects().get(0);
                if(Gdx.input.isKeyPressed(Input.Keys.E)){
                    klob.setRR(klob.getRR() + klob.getDRR());
                }
                else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                    klob.setRR(klob.getRR() - klob.getDRR());
                }

            }

        }
        else if (ps.getKlobjects().get(0).getSAS()){
            if(mult == 1) {
                Klobject klob = ps.getKlobjects().get(0);
                if (klob.getRR() > 0 ){
                    klob.setRR(klob.getRR() - klob.getDRR());
                }
                else if ( klob.getRR() < 0){
                    klob.setRR(klob.getRR() + klob.getDRR());
                }
                else if (klob.getRR() == 0){

                }
                if (Math.abs(klob.getRR()) < (2*klob.getDRR())){
                    klob.setRR(0);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (mult == 1) {
                ps.getKlobjects().get(0).setSAS(!ps.getKlobjects().get(0).getSAS());
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            ps.getKlobjects().get(0).setThrottle(0);

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            ps.getKlobjects().get(0).setThrottle( ps.getKlobjects().get(0).getMaxThrottle());

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            ps.getHud().toggleControl();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            ps.getHud().toggleStatus();
        }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            camLock = !camLock;
            camLX = camLY = 0f;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            camLock=true;
            camLX = 0;
            camLY = 0;
            if(look == 0){ // change to zero to include sun in rotation

            }
            else if (look < 0){
                look = 1;
            }
            else{
                look -= 1;
                if (look > 0){setZoom(128*1000f); }
                else{
                    setZoom(5*1024*10215);
                }
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            camLock=true;
            camLX = 0;
            camLY = 0;

            if (look < 0){
                look = 1;
            }
            else if (look < (ps.getPlanets().size())){
                if (look == ps.getPlanets().size()-1 ) {look--;} // comment in this line to shuffle through
                look++;
                look%=(ps.getPlanets().size()); // flips to zero if reaches the end
                if (look == 0) {
                    look++;
                    setZoom(128*1000f);
                }

            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {

            if (sp > 0) {
                //System.out.println("WARP DOWN      <-------");
                sp = sp - 1;
                mult = warp[sp];
                ps.getHud().setMult(mult);
                for (int i = 0; i < ps.getPlanets().size(); i++){
                    ps.getPlanets().get(i).setMultiplier( mult );
                }
                for (Klobject klob : ps.getKlobjects()){
                    klob.setMultiplier(mult);
                }

            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            //ps.getKlobjects().get(0).setSAS(false);
            //System.out.println("WARP UP      <-------");
            if(sp < spMax) {
                sp = sp + 1;
                mult = warp[sp];
                ps.getHud().setMult(mult);
                for (int i = 0; i < ps.getPlanets().size(); i++) {
                    ps.getPlanets().get(i).setMultiplier(mult);
                }
                for (Klobject klob : ps.getKlobjects()) {
                    klob.setMultiplier(mult);
                }
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
            //System.out.println("WARP UP      <-------");
            sp = 0;//Set set to zero sets mult to 1
            mult = warp[sp];
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
                if(   //ps.getPlanets().get(i).getSprite().getBoundingRectangle().contains(input.x,input.y) ||
                        ps.getPlanets().get(i).getTvg().isInBoundingBox(new Vector2(input.x,input.y)) ||
                        checkCircleClick(input.x,input.y,ps.getPlanets().get(i))) {

                    camLX = 0;
                    camLY = 0;
                    camLock = true;
                    p = ps.getPlanets().get(i);
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

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && 
            Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && 
            Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            // Only trigger for first Klobject (player ship)
            ps.getKlobjects().get(0).randomizeOrbit();
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

    public void setZoom(float zoom){
        ps.setScale(zoom);//look = ps.getPlanets().size()-1; comment this back in if you want to go around the rotation
        for (Klobject klob : ps.getKlobjects()){
            klob.getSprite().setScale((float)(1f/ps.getScale()));
            klob.getSprite().setOrigin(klob.getSprite().getWidth() / 2, klob.getSprite().getHeight() / 2);
            klob.scale = ps.getScale();
        }
        for (Cbody cb : ps.getPlanets()){
            cb.getSprite().setSize((float)(cb.getRadius()/ps.getScale()),(float)(cb.getRadius()/ps.getScale()));
            cb.getSprite().setOrigin(cb.getSprite().getWidth() / 2, cb.getSprite().getHeight() / 2);
            cb.scale = ps.getScale();
            cb.getTvg().setSize((float)(cb.getRadius()/ps.getScale()),(float)(cb.getRadius()/ps.getScale()));
        }


    }

}
