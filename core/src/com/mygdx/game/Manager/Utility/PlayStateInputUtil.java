package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;

public class PlayStateInputUtil implements InputProcessor {


    float mv = 200000000;
    double mvScale = 1.1;
    int sp = 0;
    double zm = 1.05;
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
        Cbody p = ps.getPlanets().get(look);
        double x = p.getX();
        double y = p.getY();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.M) ) {
            ps.getKlobjects().get(0).acceleration = !ps.getKlobjects().get(0).acceleration;
            //mv /= mvScale;

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
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(.5f, 0, 0, 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            camLock = !camLock;
            camLX = camLY = 0f;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (look > 0) look -= 1;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            if (look < ps.getPlanets().size()-1) look += 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            if (sp > 0) {
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

            if (camLock){
                camLX-=(x1*camera.zoom);
                camLY+=(y1*camera.zoom);
            }
            else{
                camera.position.x -= (x1*camera.zoom);
                camera.position.y += (y1*camera.zoom);
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
