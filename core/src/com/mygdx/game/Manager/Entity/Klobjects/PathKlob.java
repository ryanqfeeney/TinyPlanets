package com.mygdx.game.Manager.Entity.Klobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import math.geom2d.Point2D;
import org.apache.commons.math3.analysis.function.Atanh;
import org.apache.commons.math3.analysis.function.Sinh;
import com.mygdx.game.Manager.Utility.Colors;

import java.util.ArrayList;

public class PathKlob extends Klobject{
    public Cbody parentPath;
    public double dt;
    public PathKlob(Cbody pp){
        super();
        parentPath = pp;
        name = parentPath.getName()+"_PathKlob";
        parentBody = parentPath.getParentBody();
        MULTIPLIER = 1;
        w = 0;

        path = new ShapeRenderer();
        onPathShape = new ShapeRenderer();

        cirCol = Colors.colorToIntArray(Colors.PATHKLOB_CIRCLE);
        this.ps = parentPath.getPS();

        path.setProjectionMatrix(ps.getCamera().combined);

        state = new State();
        fStart = pp.fStart;
        fEnd   = pp.fEnd;

        bake();
    }

    public void copyProps(Cbody cb){
        semiA = cb.getSemiA();
        semiB = cb.getSemiB();
        w = cb.getW();
        scale = cb.getPS().getScale();
        ecc = cb.getEcc();
        ccw = cb.getCCW();
        mass = cb.getMass();

    }

    Point2D rrr;
    public void setAnoms(double q) {

        rrr = new Point2D(state.pos.x() - parentBody.getX(),
                state.pos.y() - parentBody.getY());

        Tanom = q;


        if (ecc < 1) {
            Eanom = Math.atan2((Math.sqrt(1 - Math.pow(ecc, 2)) * Math.sin(Tanom)),
                    (ecc + Math.cos(Tanom)));
        }
        else{
            Eanom = new Atanh().value(Math.sqrt((ecc-1)/(ecc+1))*Math.tan(Tanom/2))*2;

        }


        if (ecc < 1) {
            Manom = Eanom - (ecc * Math.sin(Eanom));

        }
        else{
            Manom = ecc * (new Sinh().value(Eanom)) - Eanom;
        }
        if (ecc > 1){

            if (ccw) {
                tmax =  (Math.acos(1 / ((-ecc))) );
            }
            else{
                tmax = -(Math.acos(1 / ((-ecc))));
            }

        }

    }

    @Override
    public void drawPath() {
        double fadeStart = fStart;
        double fadeEnd =   fEnd;
        double fade = ps.getScale()-fadeStart;
        if (fade < 0){
            return;
        }

        fade = (fade / fadeEnd);
        if (fade > 1){
            fade = 1;
        }


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        path.setProjectionMatrix(ps.getCamera().combined);
        path.begin(ShapeRenderer.ShapeType.Line);
        escapePath = false;
        try {
            int vertsSize = 2500;
            float[] verts = getVerts(vertsSize,0); //May not return the same size array if a point exits the soir
            Color pathColor = Colors.PATHKLOB_PATH;
            path.setColor(pathColor.r, pathColor.g, pathColor.b, (float) fade);
            path.polyline(verts);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
        }
        path.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if(escapePath){
            if (pathKlob != null ) {
                pathKlob.drawPath();
            }
        }
    }


    public void drawVector() {

    }

    @Override
    public void updateChildren(double ddx, double ddy) {

    }

    public void setTanom(double in){
        Tanom = in;
    }
}
