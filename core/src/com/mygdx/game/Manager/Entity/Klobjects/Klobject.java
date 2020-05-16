package com.mygdx.game.Manager.Entity.Klobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;



public class Klobject extends Cbody {

    Sprite klobSprite;

    public Klobject(Cbody cb, PlayState pstate) {

        super(cb,pstate);


        mass = 1;
        MULTIPLIER = 1;
        rotateRate = Math.random()*20 - 10 ;
        parentBody = cb;
        //acceleration = false;

        parentBody.addKlob(this);

        state = new State();

        Double rote = Math.toRadians(0);
        double dist = 900_000;
        double vel = 2500.0 * sp; // positive goes ccw similar to angles // K


        state.pos = new Point2D(parentBody.getX() + Math.cos(rote) * dist,
                parentBody.getY() + Math.sin(rote) * dist);

        state.vel = new Point2D(-Math.sin(rote) * vel,
                Math.cos(rote) * vel);



        name = "spaceship";
        textureAtlas = ps.getKlobjectAtlas();
        klobSprite =  textureAtlas.createSprite(name);
        klobSprite.setScale(300f);
        klobSprite.setOrigin(klobSprite.getWidth() / 2, klobSprite.getHeight() / 2);
        bake();

    }

    public Klobject(Cbody cb, PlayState pstate, double ang, double d, double v) {

        this(cb,pstate);

        Double rote = Math.toRadians(ang);
        double dist = d;
        double vel = v * sp; // positive goes ccw similar to angles // K


        state.pos = new Point2D(parentBody.getX() + Math.cos(rote) * dist,
                parentBody.getY() + Math.sin(rote) * dist);

        state.vel = new Point2D(-Math.sin(rote) * vel,
                Math.cos(rote) * vel);

        bake();

    }

    public Klobject(Cbody cb, PlayState ps, Point2D pos, Point2D vel) {
        this(cb,ps);
        state.pos = pos;
        state.vel = vel;
    }




    @Override
    public void update(float dt) {

        if (MULTIPLIER == 1) {
            try {
                oneXupdate(dt);
                bake();
            } catch (Exception e) {
                System.out.println("BAKE FAILED");
                System.out.println(e);
            }
        } else {
            moveOnOrbit(dt);
        }


        rotate(dt);

    }

    //check if klobject has crossed into another sphere of influence
    public void checkSoir(){
        if (getLoc().distance(parentBody.getLoc()) > parentBody.getSoir()) {
            parentBody = parentBody.getParentBody();
            bake();
        }
        else {
            for (Cbody child : parentBody.getChildren()) {
                if (child.equals(this)) { }
                else if (getLoc().distance(child.getLoc()) < child.getSoir()) {
                    parentBody = child;
                    bake();
                }
            }
        }
    }

    int timesLooped = 100 ;
    private void oneXupdate(float dt) {
        for (int i = 0; i < timesLooped; i++) {
            integrate(state, dt / timesLooped);
        }
    }



    double velo, x, y, dSd0, q, rr, normMid, phi, velAngle;

    @Override
    public void moveOnOrbit(double dt) {

        velo = state.vel.distance(0, 0);

        if (ccw) {
            this.s = MULTIPLIER * velo * dt;  // <--- add for counter clock wise

        } else {
            this.s = -MULTIPLIER * velo * dt;
        }

        dSd0 = (((peri * (1 + ecc) * Math.sqrt(1 + Math.pow(ecc, 2) + (2 * ecc * Math.cos(Tanom)))) / Math.pow(1 + (ecc * Math.cos(Tanom)), 2)));
        q = this.s * (1 / dSd0) + startAnom;
        startAnom = q;
        rr = (semiA * (1 - (ecc * ecc))) / (1 + (ecc * Math.cos(q)));
        x = parentBody.getX() + rr * Math.cos(q + w);
        y = parentBody.getY() + rr * Math.sin(q + w);


        state.pos = new Point2D(x, y);

        velo = calculateVelocity() * sp;

        calcAnomalies();

        if (ecc < 1) {
            state.vel =
                    new Point2D(-semiA * Math.sin(Eanom) * Math.cos(w) - semiB * Math.cos(Eanom) * Math.sin(w),
                            semiB * Math.cos(Eanom) * Math.cos(w) - semiA * Math.sin(Eanom) * Math.sin(w));

            normMid = state.vel.distance(0, 0);
            state.vel = state.vel.scale(velo / normMid);


            if (!ccw) {
                state.vel = new Point2D(-state.vel.x(), -state.vel.y());
            }

        } else if (ecc > 1) {;
            phi = Math.atan2(ecc * Math.sin(Tanom), (1 + ecc * Math.cos(Tanom)));
            if (ccw)
                velAngle = Tanom + Math.PI / 2 - phi + w;
            else {
                velAngle = Tanom - Math.PI / 2 - phi + w;
            }
            state.vel = new Point2D(Math.cos(velAngle) * velo, Math.sin(velAngle) * velo);

        }
    }





    public void rotate(float dt) {
        klobSprite.rotate((float) ((rotateRate * dt * MULTIPLIER) % 360));
    }


    public double findFocus(double a, double b) {
        return Math.sqrt(a * a - b * b);
    }

    public double getX() {
        return state.pos.getX();
    }

    public double getY() {
        return state.pos.getY();
    }

    public void setRotateRate(double rr) {
        rotateRate = rr;
    }

    public String getName() {
        return name;
    }

    public Sprite getKlobSprite() {
        return klobSprite;
    }

    public ShapeRenderer getPathSprite() {
        return path;
    }

    public void setMultiplier(double mult) {
        MULTIPLIER = mult;
    }

    public Point2D getLoc() {
        return state.pos;
    }

    public void setLoc(Point2D newLoc) {
        state.pos = newLoc;
    }

    public double dotProd(Point2D a, Point2D b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    public Cbody getParentBody() {
        return parentBody;
    }

    public Point2D getVel() {
        return state.vel;
    }

    public void setVel(Point2D in) {
        state.vel = in;
    }

    public void setParentBody(Cbody parentBody) {
        this.parentBody = parentBody;
    }
}
