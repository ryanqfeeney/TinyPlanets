package com.mygdx.game.Manager.Entity.Klobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;
import org.apache.commons.math3.analysis.function.Atanh;
import org.apache.commons.math3.analysis.function.Tan;



public class Klobject2 extends Klobject {

    public Klobject2(Cbody cb,PlayState pstate) {
        super( cb,pstate);
        klobSprite.rotate(180);
    }

    public Klobject2(PlayState ps, Cbody cb, Point2D pos, Point2D vel) {
        super(cb,ps, pos, vel);
    }

    //ORIGINAL METHOD FOR MOVE ON ORBIT> IN BAKE(), startAnom HAS TO BE SET TO Eanom FOR THIS TO FUNCTION PROPERLY IN ELLIPTICAL ORBITS


    @Override
    public void bake() {
        super.bake();
        if(ecc < 1){
            startAnom=Eanom;
        }
    }

    @Override
    public void moveOnOrbit(double dt) {
        double velo;

        velo = state.vel.distance(0, 0);

        if (ccw) {
            //this.t += MULTIPLIER * velo * dt;  // <--- add for counter clock wise
            this.s = MULTIPLIER * velo * dt;

        } else {
            //this.t -= MULTIPLIER * velo * dt;  // <--- subtract for clockwise
            this.s = -MULTIPLIER * velo * dt;
        }

        double x , y;
        ///Added section
        if (ecc < 1.0) {
            double h = Math.pow((semiA - semiB), 2) / Math.pow((semiA + semiB), 2);
            double p = Math.PI * (semiA + semiB) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));

            //double tt = ((this.t) / p) * 2 * Math.PI; // <-- something fishy maybe going on here mean and eccentric anomaly
            double q = 0;//tt + startAnom;

            x = (parentBody.getX() + semiA * Math.cos(q) * Math.cos(w) -
            semiB * Math.sin(q) * Math.sin(w) - focus * Math.cos(w));

            y = (parentBody.getY() + semiA * Math.cos(q) * Math.sin(w) +
            semiB * Math.sin(q) * Math.cos(w) - focus * Math.sin(w));

        }

        else {//end of added section
            double dSd0 = (((peri * (1 + ecc) * Math.sqrt(1 + Math.pow(ecc, 2) + (2 * ecc * Math.cos(Tanom)))) / Math.pow(1 + (ecc * Math.cos(Tanom)), 2)));
            double q = this.s * (1 / dSd0) + startAnom;
            startAnom = q;
            double rr = (semiA * (1 - (ecc * ecc))) / (1 + (ecc * Math.cos(q)));
            x = parentBody.getX() + rr * Math.cos(q + w);
            y = parentBody.getY() + rr * Math.sin(q + w);

            //            if (getLoc().distance(parentBody.getLoc()) > parentBody.getSoir()){
            //                parentBody = parentBody.getParentBody();
            //            };
        }//(except for this bracket. it is also added.


        state.pos = new Point2D(x, y);

        velo = calculateVelocity() * sp;

        calcAnomalies();

        if ( ecc < 1) {
            state.vel =
                    new Point2D(-semiA * Math.sin(Eanom) * Math.cos(w) - semiB * Math.cos(Eanom) * Math.sin(w),
                            semiB * Math.cos(Eanom) * Math.cos(w) - semiA * Math.sin(Eanom) * Math.sin(w));

            double normMid = state.vel.distance(0, 0);
            state.vel = state.vel.scale(velo / normMid);

            if (!ccw) {
                state.vel = new Point2D(-state.vel.x(), -state.vel.y());
            }
        }

        else if (ecc > 1){
            double phi = Math.atan2(ecc*Math.sin(Tanom),(1+ecc*Math.cos(Tanom)));
            double velAngle;
            if (ccw)
                velAngle = Tanom + Math.PI/2 - phi + w;
            else{
                velAngle = Tanom - Math.PI/2 - phi + w;
            }
            state.vel = new Point2D(Math.cos(velAngle)*velo, Math.sin(velAngle)*velo);

        }
    }
}