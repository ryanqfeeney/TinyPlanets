package com.mygdx.game.Manager.Utility.Huds;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;

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



    public void drawPaths() {

        for (Cbody cb : cbs) {

            path = cb.getPath();
            semiA = cb.getSemiA();
            semiB = cb.getSemiB();
            peri = cb.getPeri();
            w = cb.getW();


            path = new ShapeRenderer();
            path.setProjectionMatrix(ps.getCamera().combined);
            path.begin(ShapeRenderer.ShapeType.Line);
            path.translate((float) (((cb.getParentBody().getLoc().getX()) - (semiA) - (Math.cos(w) * (semiA - peri))) - camX) / scale,
                    (float) (((cb.getParentBody().getLoc().getY()) - (semiB) - (Math.sin(w) * (semiA - peri))) - camY) / scale, 0);

            try {
                path.ellipse(0, 0, (float) (2 * semiA / scale), (float) (2 * semiB / scale), (float) Math.toDegrees(w));
            } catch (ArrayIndexOutOfBoundsException e) {
                // e.printStackTrace();
            }

            path.setColor(255f, 0f, 0, 255f);
            path.end();
        }

    }



    @Override
    public void dispose() {

    }


}