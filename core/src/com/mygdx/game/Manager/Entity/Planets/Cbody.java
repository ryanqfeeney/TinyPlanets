package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject2;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;
import org.apache.commons.math3.analysis.function.Atanh;


import java.util.ArrayList;

public class Cbody{

    protected int sp = 1;

    protected String name;
    protected TextureAtlas textureAtlas;
    protected Sprite sprite;
    protected Cbody parentBody;
    protected State state;
    protected double MULTIPLIER;
    protected double gravConstant = 6.67*Math.pow(10,-11);

    protected ArrayList<Cbody> children;
    protected ArrayList<Klobject> klobs;
    protected ArrayList<Klobject2> klobs2;

    protected boolean ccw;

    protected double semiA, semiB, peri, ecc, w, Eanom, Manom, Tanom, startAnom;
    protected Point2D eccVecc;

    protected double s;

    protected double radius;
    protected double mass;
    protected double rotateRate;

    protected double focus;
    protected double soir;

    protected PlayState ps;
    protected ShapeRenderer path;


    public Cbody(PlayState ps){
        children = new  ArrayList<>();
        klobs = new  ArrayList<>();
        klobs2 = new  ArrayList<>();
        MULTIPLIER = 1;
        rotateRate = .0166;
        w = 0;
        soir = 0; //sphere of influence radiance
        path = new ShapeRenderer();
        state = new State();
        this.ps = ps;
    }

    public Cbody(Cbody parentBody,PlayState ps){
        this(ps);
        this.parentBody = parentBody;
    }

    public void update(float dt) {
        moveOnOrbit(dt);
        rotate(dt);
    }


    public void rotate(float dt){
        sprite.rotate((float)((rotateRate*dt*MULTIPLIER)%360));
    }


    double velo, x, y, dSd0, q, rr, dx, dy, vdx, vdy;
    public void moveOnOrbit(double dt){

        velo = state.vel.distance(0, 0);

        if (ccw) {
            this.s = MULTIPLIER * velo * dt;

        } else {
            this.s = -MULTIPLIER * velo * dt;
        }


        dSd0 = (((peri * (1+ecc) * Math.sqrt(1+Math.pow(ecc,2) +(2*ecc*Math.cos(Tanom)))) / Math.pow(1+(ecc*Math.cos(Tanom)),2)));
        q = this.s * (1/dSd0) + startAnom;

        if (ecc == 0){
            q = (this.s / semiA) + startAnom;
        }

        startAnom = q;

        rr = (semiA*(1-(ecc*ecc))) / (1 + (ecc * Math.cos(q)));


        x = parentBody.getX() + rr*Math.cos(q + w);
        y = parentBody.getY() + rr*Math.sin(q + w);

        dx = x - getLoc().getX();
        dy = y - getLoc().getY();


        state.pos = new Point2D(x,y);

        updateChildren(dx,dy);





    }

    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }


    public double calculateVelocity() {

        double dist = getLoc().distance(parentBody.getLoc());
        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY "+getName());
            System.exit(0);
        }
        double vel = Math.sqrt(gravConstant*parentBody.mass*((2/dist)-(1/semiA)));
        if (Double.isNaN(vel)){
            vel = Math.sqrt(gravConstant*parentBody.mass*(Math.abs((2/dist)-(2/dist))));
            if(Double.isNaN(vel)) {
                System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY 2 "+getName());
                System.exit(0);
            }
        }
        if(vel==0){
            vel = 1;
        }
        return vel;

    }
    double velSq, r, gm, cz;
    public void bake() {
        s = 0;
        startAnom = 0;

        velSq = Math.pow(state.vel.distance(0, 0), 2);
        r = state.pos.distance(parentBody.getLoc());
        gm = sp * sp * gravConstant * parentBody.getMass();

        Point2D posr = new Point2D(state.pos.getX() - parentBody.getX(), state.pos.getY() - parentBody.getY());

        Point2D calc1 = posr.scale((velSq / gm));
        Point2D calc2 = state.vel.scale((dotProd(posr, state.vel) / gm));
        Point2D calc3 = posr.scale(1 / r);

        eccVecc = (calc1.minus(calc2)).minus(calc3);
        ecc = eccVecc.distance(0.0,0.0);

        w = Math.atan2(eccVecc.y(), eccVecc.x());

        cz = posr.x() * state.vel.y() - posr.y() * state.vel.x();
        if (cz > 0) {
            ccw = true;
        } else {
            ccw = false;
        }



        semiA = (gm * r) / (2 * gm - r * velSq);
        semiB = semiA * (Math.sqrt((1 - Math.pow(ecc, 2))));
        peri = semiA * ( 1 - ecc);
        focus = findFocus(semiA, semiB);



        calcAnomalies();



        startAnom = Tanom;

        if (ecc > 1){
            semiB = -semiA*Math.sqrt(ecc*ecc - 1);
        }


    }

    public void updateChildren(double ddx,double ddy){
        for (Cbody child: children){
            child.setLoc(new Point2D(child.getX()+ddx,child.getY()+ddy));
            child.updateChildren(ddx,ddy);
        }
        for (Klobject child: klobs){
            child.setLoc(new Point2D(child.getX()+ddx,child.getY()+ddy));
            child.updateChildren(ddx,ddy);
        }
        for (Klobject2 child: klobs2){
            child.setLoc(new Point2D(child.getX()+ddx,child.getY()+ddy));
            child.updateChildren(ddx,ddy);
        }
    }


    Point2D rrr;
    public void calcAnomalies() {


        rrr = new Point2D(state.pos.x() - parentBody.getX(),
                state.pos.y() - parentBody.getY());

//        if(startAnom == 0) {
//            Tanom = Math.acos(dotProd(eccVecc, r) / (ecc * r.distance(0, 0)));
//            if (ecc == 0) {
//                Tanom = Math.atan2(getX() - parentBody.getX(), getY() - parentBody.getY());
//            }
//        }

        Tanom = Math.acos(dotProd(eccVecc, rrr) / (ecc * rrr.distance(0, 0)));
        if (ecc == 0) {
            Tanom = Math.atan2(rrr.getY(),rrr.getX());
        }
//        else {
//            Tanom = startAnom;
//        }

        if(!ccw){
            if (dotProd(eccVecc.rotate(-Math.PI/2),rrr) < 0){
                Tanom = 2 * Math.PI - Tanom;
            }
        }
        else {
            if (dotProd(eccVecc.rotate(Math.PI/2),rrr) < 0){
                Tanom = 2 * Math.PI - Tanom;
            }
        }

        if (!ccw) {
            Tanom = -Tanom;
        }

        if (ecc < 1) {
            Eanom = Math.atan2((Math.sqrt(1 - Math.pow(ecc, 2)) * Math.sin(Tanom)),
                    (ecc + Math.cos(Tanom)));
        }
        else{
            Eanom = new Atanh().value(Math.sqrt((ecc-1)/(ecc+1))*Math.tan(Tanom/2))*2;

        }

        Manom = Eanom - (ecc * Math.sin(Eanom));

    }
    public void drawPath() {

        path = new ShapeRenderer();
        path.setProjectionMatrix(ps.getCamera().combined);
        path.begin(ShapeRenderer.ShapeType.Line);
        path.translate((float) (((getParentBody().getLoc().getX()) - (semiA) - (Math.cos(w) * (semiA - peri))) - ps.getCamX()),
                (float) (((getParentBody().getLoc().getY()) - (semiB) - (Math.sin(w) * (semiA - peri))) - ps.getCamY()), 0);

        try {
            path.ellipse(0, 0, (float) (2 * semiA), (float) (2 * semiB), (float) Math.toDegrees(getW()));
        } catch (ArrayIndexOutOfBoundsException e) {
           // e.printStackTrace();
        }

        path.setColor(255f, 0f, 0, 255f);
        path.end();

    }


    public double getX(){
        return getLoc().getX();
    }
    public double getY(){
        return getLoc().getY();
    }

    public double getRadius() {
        return radius;
    }

    public String getName(){
        return name;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public void setMultiplier(double mult){
        MULTIPLIER = mult;
    }
    public void addChild(Cbody child){
        children.add(child);
    }
    public void addKlob(Klobject klob){ klobs.add(klob);}
    public void addKlob(Klobject2 klob){ klobs2.add(klob);}
    public Point2D getLoc(){
        return state.pos;
    }
    public void setLoc(Point2D newLoc) { state.pos = newLoc;}

    public double getMass(){
        return mass;
    }

    public double dotProd(Point2D a, Point2D b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    public double getSoir() {
        return soir;
    }

    public double getW(){
        return w;
    }

    public ArrayList<Cbody> getChildren(){
        return children;
    }

    public Cbody getParentBody() {
        return parentBody;
    }

    public void setParentBody(Cbody parentBody) {
        this.parentBody = parentBody;
    }

    public class State {
        public Point2D pos;
        public Point2D vel;

        public State() {
            pos = new Point2D(0, 0);
            vel = new Point2D(0, 0);
        }
    }

    public class Derivative {
        public Point2D dpos;
        public Point2D dvel;

        public Derivative() {
            dpos = new Point2D(0, 0);
            dvel = new Point2D(0, 0);
        }
    }

    public Derivative evaluate(State init,
                                 float dt,
                                 Derivative d) {
        State state = new State();
        state.pos = init.pos.plus(d.dpos.scale(dt));
        state.vel = init.vel.plus(d.dvel.scale(dt));

        Derivative output = new Derivative();
        output.dpos = state.vel;
        output.dvel = acceleration(state);
        return output;
    }

    public Point2D acceleration(State state) {
        Double a;
        Double forcedub;
        Point2D force;
        Point2D loc = state.pos;

        a = Math.atan2(loc.getX() - parentBody.getX(), loc.getY() - parentBody.getY());
        a = Math.toRadians(90) - a;

        forcedub = ((sp * sp) * (gravConstant * parentBody.getMass()) / Math.pow(loc.distance(parentBody.getLoc()), 2));
        force = new Point2D(((-1) * forcedub * Math.cos(a)), ((-1) * forcedub * Math.sin(a)));

        return force;
    }

    public void integrate(State state,
                   float dt) {
        Derivative a, b, c, d;

        a = evaluate(state, 0.0f, new Derivative());
        b = evaluate(state, dt * 0.5f, a);
        c = evaluate(state, dt * 0.5f, b);
        d = evaluate(state, dt, c);

        Point2D dxdt = (a.dpos.plus((b.dpos.plus(c.dpos)).scale(2.0)).plus(d.dpos)).scale(1.0 / 6.0);

        Point2D dvdt = (a.dvel.plus((b.dvel.plus(c.dvel)).scale(2.0)).plus(d.dvel)).scale(1.0 / 6.0);

        state.pos = state.pos.plus(dxdt.scale(dt));
        state.vel = state.vel.plus(dvdt.scale(dt));

    }
}

//        double h = Math.pow((semiA - semiB),2)/Math.pow((semiA + semiB),2);
//        double p = Math.PI * (semiA+semiB) *( 1 + (3*h)/(10 + Math.sqrt(4-3*h)));
//        p = p / (2 * Math.PI);
//        double q = this.t/p;
//
//        double x = (parentBody.getX() + semiA*Math.cos(q)*Math.cos(orbRotation) -
//                        semiB*Math.sin(q)*Math.sin(orbRotation) + focus*Math.cos(orbRotation));
//
//        double y =  (parentBody.getY() + semiA*Math.cos(q)*Math.sin(orbRotation) +
//                        semiB*Math.sin(q)*Math.cos(orbRotation) + focus*Math.sin(orbRotation));

