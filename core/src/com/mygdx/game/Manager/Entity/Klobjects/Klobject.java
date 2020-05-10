package com.mygdx.game.Manager.Entity.Klobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;
import org.apache.commons.math3.analysis.function.Atanh;



public class Klobject {

    int sp = 1;

    String name;
    TextureAtlas textureAtlas;
    Sprite klobSprite;
    ShapeRenderer path;
    Cbody parentBody;
    State state;
    PlayState ps;


    public boolean acceleration;
    public boolean ccw;

    double MULTIPLIER;
    static double gravConstanat = 6.67 * Math.pow(10, -11);

    double semiA, semiB, peri, ecc, w, Eanom, Manom, Tanom, startAnom;
    Point2D eccVecc;

    protected double t, s;
    protected double mass;
    protected double rotateRate;
    protected double focus;

    public Klobject(PlayState pstate, Cbody cb) {

        ps = pstate;
        mass = 1;
        MULTIPLIER = 1;
        rotateRate = 0;
        parentBody = cb;
        acceleration = false;

        cb.addKlob(this);

        state = new State();

        //////////////////////////////
        //***
        //////////////////////////////

        Double rote = Math.toRadians(135);
        double dist = 900_000;
        double vel = 2290.0*sp; // positive goes ccw similar to angles // K


        state.pos = new Point2D(parentBody.getX() + Math.cos(rote)*dist,
                                parentBody.getY() + Math.sin(rote)*dist);

        state.vel = new Point2D(-Math.sin(rote)*vel,
                                Math.cos(rote)*vel);


        textureAtlas = new TextureAtlas("spaceship.txt");
        name = "spaceship";
        klobSprite = textureAtlas.createSprite(name);
        path = new ShapeRenderer();
        klobSprite.setScale(25f, 25f);
        klobSprite.setOrigin(klobSprite.getWidth() / 2, klobSprite.getHeight() / 2);
        bake();
    }

    public Klobject (PlayState ps,Cbody cb, Point2D pos, Point2D vel){
        this(ps,cb);
        state.pos = pos;
        state.vel = vel;
    }


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


    private void oneXupdate(float dt) {
        int timesLooped = 1000;
        for (int i = 0; i < timesLooped; i++) {
            integrate(state, dt / timesLooped);
        }
        if (getLoc().distance(parentBody.getLoc()) > parentBody.getSoir()){
            parentBody = parentBody.getParentBody();
        };


    }

    public void bake() {
        t = 0;
        s = 0;

        double velSq = Math.pow(state.vel.distance(0, 0), 2);
        double r = state.pos.distance(parentBody.getLoc());
        double gm = sp * sp * gravConstanat * parentBody.getMass();

        Point2D posr = new Point2D(state.pos.getX() - parentBody.getX(), state.pos.getY() - parentBody.getY());


        Point2D calc1 = posr.scale((velSq / gm));
        Point2D calc2 = state.vel.scale((dotProd(posr, state.vel) / gm));
        Point2D calc3 = posr.scale(1 / r);

        eccVecc = (calc1.minus(calc2)).minus(calc3);
        w = Math.atan2(eccVecc.y(), eccVecc.x());

        double cz = posr.x() * state.vel.y() - posr.y() * state.vel.x();
        if (cz > 0) {
            ccw = true;
        } else {
            ccw = false;
        }
        ecc = eccVecc.distance(0, 0);
        if (ecc == 1){ ecc = 1 + Double.MIN_VALUE; }


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


    public void moveOnOrbit(float dt) {
        double velo;

        velo = state.vel.distance(0, 0);

        if (ccw) {
            this.t += MULTIPLIER * velo * dt;  // <--- add for counter clock wise
            this.s = MULTIPLIER * velo * dt;

        } else {
            this.t -= MULTIPLIER * velo * dt;  // <--- subtract for clockwise
            this.s = -MULTIPLIER * velo * dt;
        }

        double x , y;

        double dSd0 = (((peri * (1+ecc) * Math.sqrt(1+Math.pow(ecc,2) +(2*ecc*Math.cos(Tanom)))) / Math.pow(1+(ecc*Math.cos(Tanom)),2)));
        double q = this.s * (1/dSd0) + startAnom;
        startAnom = q;
        double rr = (semiA*(1-(ecc*ecc))) / (1 + (ecc * Math.cos(q)));
        x = parentBody.getX() + rr*Math.cos(q + w);
        y = parentBody.getY() + rr*Math.sin(q + w);

//            if (getLoc().distance(parentBody.getLoc()) > parentBody.getSoir()){
//                parentBody = parentBody.getParentBody();
//            };


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

    public double calculateVelocity() {

        double dist = state.pos.distance(parentBody.getLoc());
        if (Double.isNaN(dist)) {
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY " + getName());
            System.exit(0);
        }
        double vel;
        if(ecc < 1.0){
            vel = Math.sqrt(gravConstanat * parentBody.getMass() * ((2 / dist) - (1 / semiA)));
        }
        else{
            vel = Math.sqrt(gravConstanat * parentBody.getMass() * ((2 / dist) - (1 / semiA))); // garbage **************
        }
        if (Double.isNaN(vel)) {
            vel = Math.sqrt(gravConstanat * parentBody.getMass() * (Math.abs((2 / dist) - (2 / dist))));
            if (Double.isNaN(vel)) {
                System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY 2 " + getName());
                System.exit(0);
            }
        }
        if (vel == 0) {
            vel = 1;
        }

        return vel;

    }

    public void calcAnomalies() {


        Point2D r = new Point2D(state.pos.x() - parentBody.getX(),
                state.pos.y() - parentBody.getY());

        Tanom = Math.acos(dotProd(eccVecc, r) / (ecc * r.distance(0, 0)));

        if(!ccw){
            if (dotProd(eccVecc.rotate(-Math.PI/2),r) < 0){
                Tanom = 2 * Math.PI - Tanom;
            }
        }
        else {
            if (dotProd(eccVecc.rotate(Math.PI/2),r) < 0){
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

    public void updatePathSprite(){


        path = new ShapeRenderer();
        path.setProjectionMatrix(ps.getCamera().combined);
        path.begin(ShapeRenderer.ShapeType.Line);
        path.translate( (float) (getParentBody().getLoc().getX()) - (float)(semiA) - (float) (Math.cos(w)*(semiA-peri)),
                        (float) (getParentBody().getLoc().getY()) - (float)(semiB) - (float) (Math.sin(w)*(semiA-peri)),0);

        try{
            path.ellipse(0,0,(float)(2*semiA),(float)(2*semiB),(float)Math.toDegrees(getW()));
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        path.setColor(255f,0f,0,255f);
        path.end();

    }


    public void rotate(float dt) {
        klobSprite.rotate((float) ((rotateRate * dt * MULTIPLIER) % 360));
    }

    public double getW() {        return w;    }

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

    public Point2D getVel() { return state.vel;}

    public void setVel(Point2D in) {state.vel = in;}

    public void setParentBody(Cbody parentBody) { this.parentBody = parentBody;  }

    class State {
        Point2D pos;
        Point2D vel;

        State() {
            pos = new Point2D(0, 0);
            vel = new Point2D(0, 0);
        }
    }

    class Derivative {
        Point2D dpos;
        Point2D dvel;

        Derivative() {
            dpos = new Point2D(0, 0);
            dvel = new Point2D(0, 0);
        }
    }

    Derivative evaluate(State init,
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

    Point2D acceleration(State state) {
        Double a;
        Double forcedub;
        Point2D force;
        Point2D loc = state.pos;

        a = Math.atan2(loc.getX() - parentBody.getX(), loc.getY() - parentBody.getY());
        a = Math.toRadians(90) - a;

        forcedub = ((sp * sp) * (gravConstanat * parentBody.getMass()) / Math.pow(loc.distance(parentBody.getLoc()), 2));
        force = new Point2D(((-1) * forcedub * Math.cos(a)), ((-1) * forcedub * Math.sin(a)));

        return force;
    }

    void integrate(State state,
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
