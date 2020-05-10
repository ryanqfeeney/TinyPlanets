package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject2;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;


public class PlayStateInputUtil implements InputProcessor {


    float mv = 200000000;
    double mvScale = 1.1;
    int sp = 0;
    double zm = 1.05;
    double rotation = 0;
    int look = 1;
    float camLX = 0;
    float camLY = 0;
    boolean camLock = true;
    double mult = Math.pow(2,sp);

    PlayState ps;
    OrthographicCamera camera;

    public PlayStateInputUtil(PlayState ps){
        this.ps = ps;
        this.camera = ps.getCamera();

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

    public void lookAtCbody(){
        if (!camLock){return;}
        Cbody p;
        Klobject klob;
        double x, y;

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

        camera.position.x = (float)(x+camLX);
        camera.position.y = (float)(y+camLY);
        camera.update();
    }
    public void lookAtCbody(Cbody cb){
        if (!camLock){return;}
        Cbody p = cb;
        double x = p.getX();
        double y = p.getY();
        camera.position.x = (float)(x+camLX);
        camera.position.y = (float)(y+camLY);
        camera.update();
    }

    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ) {
            Point2D vel = ps.getKlobjects().get(0).getVel();
            Point2D velN = vel.scale(1.0002);
            ps.getKlobjects().get(0).setVel(velN);
            System.out.println("ye");


        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            Point2D vel = ps.getKlobjects().get(0).getVel();
            Point2D velN = vel.scale(1/1.0002);
            ps.getKlobjects().get(0).setVel(velN);
            System.out.println("ye");


        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) ) {
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
            camera.rotate(-.5f, 0, 0, 1);
            rotation -= .5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(.5f, 0, 0, 1);
            rotation += .5;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            camLock = !camLock;
            camLX = camLY = 0f;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {

            if (look > 0){
                look -= 1;
            }
            else{
                look = 1;
            }

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            if(look < 0){
                look = 1;
            }
            else if (look < ps.getPlanets().size()-1) look += 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            if (sp > 0) {
                System.out.println("WARP DOWN      <-------");
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
            System.out.println("WARP UP      <-------");
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
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            camera.unproject(input);

            for (int i = 0; i < ps.getPlanets().size(); i++){
                if(ps.getPlanets().get(i).getSprite().getBoundingRectangle().contains(input.x,input.y)) {

                    Cbody p = ps.getPlanets().get(i);
                    camera.position.slerp(new Vector3((float)p.getX(),(float)p.getY(),0f),.2f);
                    look = i;
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
                camLX-=delta.getX();
                camLY+=delta.getY();
            }
            else{
                camera.position.x -= delta.getX();
                camera.position.y += delta.getY();
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
