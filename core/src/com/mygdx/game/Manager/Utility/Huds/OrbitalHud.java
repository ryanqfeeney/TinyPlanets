package com.mygdx.game.Manager.Utility.Huds;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Manager.Entities.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Colors;

import java.util.ArrayList;

public class OrbitalHud implements Disposable  {

    private PlayState ps;
    private ShapeRenderer path;
    double x,y,semiA,semiB,peri,camX,camY,w;
    float scale;
    ArrayList<Cbody> cbs;




    public OrbitalHud(PlayState ps) {
        this.ps = ps;
        cbs = new ArrayList<>();
        cbs.addAll(ps.getPlanets());
        cbs.addAll(ps.getKlobjects());
    }

    public void update(float dt){

        scale =ps.getScale();
        camX = ps.getCamX();
        camY = ps.getCamY();
    }




    @Override
    public void dispose() {

    }


}