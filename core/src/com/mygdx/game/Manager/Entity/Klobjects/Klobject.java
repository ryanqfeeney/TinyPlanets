package com.mygdx.game.Manager.Entity.Klobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import math.geom2d.Point2D;



public class Klobject {

    class State {
        Point2D pos;
        Point2D vel;
        State(){
            pos = new Point2D(0,0);
            vel = new Point2D(0,0);
        }
    }

    class Derivative {
        Point2D dpos;
        Point2D dvel;
        Derivative(){
            dpos = new Point2D(0,0);
            dvel = new Point2D(0,0);
        }
    }

    Derivative evaluate(State init,
                        float dt,
                        Derivative d)
    {
        State state = new State();
        state.pos = init.pos.plus(d.dpos.scale(dt));
        state.vel = init.vel.plus(d.dvel.scale(dt));

        Derivative output = new Derivative();
        output.dpos = state.vel;
        output.dvel = acceleration( state);
        return output;
    }

    Point2D acceleration(State state )
    {
        Double a;
        Double forcedub;
        Point2D force;
        Point2D loc = state.pos;

        a = Math.atan2(loc.getX()-parentBody.getX(), loc.getY()-parentBody.getY());
        a = Math.toRadians(90)-a;

        forcedub = ((100*100)*(gravConstanat*parentBody.getMass())/Math.pow(loc.distance(parentBody.getLoc()),2));
        force = new Point2D(((-1)*forcedub*Math.cos(a)),((-1)*forcedub*Math.sin(a)));

        return force;
    }

    void integrate( State state,
                    float dt )
    {
        Derivative a,b,c,d;

        a = evaluate( state,  0.0f, new Derivative() );
        b = evaluate( state,  dt*0.5f, a );
        c = evaluate( state,  dt*0.5f, b );
        d = evaluate( state,  dt, c );

        Point2D dxdt = ( a.dpos.plus((b.dpos.plus(c.dpos)).scale(2.0)).plus(d.dpos)).scale(1.0 / 6.0);

        Point2D dvdt = ( a.dvel.plus((b.dvel.plus(c.dvel)).scale(2.0)).plus(d.dvel)).scale(1.0 / 6.0);

        state.pos = state.pos.plus(dxdt.scale(dt));
        state.vel = state.vel.plus(dvdt.scale(dt));

    }

////////////////////////////////////////////////////
    //***************************8
////////////////////////////////////////////////////

    String name;
    TextureAtlas textureAtlas;
    Sprite sprite;
    Cbody parentBody;
    State state;
    public boolean acceleration;
    public boolean ccw;

    double MULTIPLIER;
    static double gravConstanat = 6.67*Math.pow(10,-11);

    double semiA, semiB, ecc, w, Eanom, Manom, startEanom;
    Point2D eccVecc;

    protected double t;
    protected double mass;
    protected double rotateRate;
    protected double orbRotation;
    protected double focus;

    public Klobject(Cbody cb){

        mass = 1;
        MULTIPLIER = 1;
        rotateRate = 0;
        parentBody = cb;
        acceleration = false;

        cb.addKlob(this);

        state = new State();
        state.pos = new Point2D((parentBody.getX()),
                        (parentBody.getY() +900_000));

        state.vel = new Point2D(-100*2526.733,0);

        textureAtlas = new TextureAtlas("spaceship.txt");
        name = "spaceship";
        sprite = textureAtlas.createSprite(name);
        sprite.setScale(800f,800f);
        //sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        bake();
    }



    public void update(float dt){

        if ( MULTIPLIER == 1 && !acceleration){
            oneXupdate(dt);
            try{
                bake();
            }
            catch (Exception e){
                System.out.println("BAKE FAILED");
                System.out.println(e);
            }
        }
        else{
            moveOnOrbit(dt);
        }
        rotate(dt);

    }




    private void oneXupdate(float dt){
        int timesLooped = 1000;
        for (int i = 0; i < timesLooped; i++) {
            integrate(state, dt/timesLooped);
        }


    }

    public void bake(){
        t=0;
        double velSq = Math.pow(state.vel.distance(0,0),2);
        double r = state.pos.distance(parentBody.getLoc());
        double gm = 100*100*gravConstanat*parentBody.getMass();

        Point2D posr = new Point2D(state.pos.getX()-parentBody.getX(), state.pos.getY()-parentBody.getY());


        Point2D calc1 = posr.scale((velSq/gm));
        Point2D calc2 = state.vel.scale((dotProd(posr,state.vel)/gm));
        Point2D calc3 = posr.scale(1/r);

        eccVecc = (calc1.minus(calc2)).minus(calc3);
        w = Math.atan2(eccVecc.y(),eccVecc.x());

        double cz = posr.x()*state.vel.y() - posr.y()*state.vel.x();
        if (cz > 0){
            ccw = true;
        }
        else{
            ccw = false;
        }

        ecc = eccVecc.distance(0,0);


        semiA = (gm*r)/(2*gm - r*velSq);
        semiB = semiA*(Math.sqrt((1-Math.pow(ecc,2))));
        focus = findFocus(semiA,semiB);

        startEanom = calcEanomAndManom();

    }


    public void moveOnOrbit(float dt){
        double velo;

        velo = state.vel.distance(0,0);


        if (ccw){
            this.t += MULTIPLIER*velo*dt;  // <--- add for counter clock wise subtract for clockwise
        }
        else{
            this.t -= MULTIPLIER*velo*dt;  // <--- add for counter clock wise subtract for clockwise
        }


        double h = Math.pow((semiA - semiB),2)/Math.pow((semiA + semiB),2);
        double p = (semiA+semiB) * ( 1 + (3*h)/(10 + Math.sqrt(4-3*h)));

        p = p / (Math.PI);

        double q = ((this.t)/p) + startEanom;



        double x = (parentBody.getX() + semiA*Math.cos(q)*Math.cos(w) -
                semiB*Math.sin(q)*Math.sin(w) - focus*Math.cos(w));

        double y = (parentBody.getY() + semiA*Math.cos(q)*Math.sin(w) +
                semiB*Math.sin(q)*Math.cos(w) - focus*Math.sin(w));

        double dx = x - state.pos.getX();
        double dy = y - state.pos.getY();


        state.pos = new Point2D(x,y);

        velo = calculateVelocity()*100;

        calcEanomAndManom();

        System.out.println(Eanom+"REE");

        state.vel =
                new Point2D(-semiA*Math.sin(Eanom)*Math.cos(w)-semiB*Math.cos(Eanom)*Math.sin(w),
                             semiB*Math.cos(Eanom)*Math.cos(w)-semiA*Math.sin(Eanom)*Math.sin(w));

        double normMid = state.vel.distance(0,0);
        state.vel = state.vel.scale(velo/normMid);


        if (!ccw){
            state.vel = new Point2D(-state.vel.x(),-state.vel.y());
        }
    }



    public double calculateVelocity() {

        double dist = state.pos.distance(parentBody.getLoc());
        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY "+getName());
            System.exit(0);
        }
        double vel = Math.sqrt(gravConstanat*parentBody.getMass()*((2/dist)-(1/semiA)));
        if (Double.isNaN(vel)){
            vel = Math.sqrt(gravConstanat*parentBody.getMass()*(Math.abs((2/dist)-(2/dist))));
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

    public double calcEanomAndManom(){
        double EanomC = (state.pos.getX()-(parentBody.getX()-focus*Math.cos(w)))/semiA;  //<--- adjust for rotation
        double EanomS = (state.pos.getY()-(parentBody.getY()-focus*Math.sin(w)))/semiB;  //<--- adjust for rotation


        if (EanomC > 1.0){
            EanomC = 1.0;
        }
        else if (EanomC < -1.0){
            EanomC = -1.0;
        }

        if (EanomS > 1.0){
            EanomS = 1.0;
        }
        else if (EanomS < -1.0){
            EanomS = -1.0;
        }

        EanomC = Math.acos(EanomC);
        EanomS = Math.asin(EanomS);



        if (EanomC <= (Math.PI/2) && EanomS >= 0) {
            Eanom = EanomS;
        }
        else if (EanomC >= (Math.PI/2) && EanomS >= 0){
            Eanom = EanomC;

        }
        else if (EanomC >= (Math.PI/2) && EanomS <= 0){
            Eanom = Math.PI - EanomS;
        }
        else if (EanomC <= (Math.PI/2) && EanomS <= 0){
            Eanom = EanomS;
        }
        else {
            System.out.println("BROKEN");
        }


        Eanom = Eanom - w;
        Manom = Eanom - (ecc * Math.sin(Eanom));
        return Eanom;

    }


    public void rotate(float dt){
        sprite.rotate((float)((rotateRate*dt*MULTIPLIER)%360));
    }



    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }

    public double getX(){
        return state.pos.getX();
    }
    public double getY(){
        return state.pos.getY();
    }
    public void setRotateRate(double rr){rotateRate = rr;}
    public String getName(){
        return name;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public void setMultiplier(double mult){
        MULTIPLIER = mult;
    }
    public Point2D getLoc(){
        return state.pos;
    }
    public void setLoc(Point2D newLoc){
         state.pos = newLoc;
    }

    public double dotProd(Point2D a, Point2D b){
        return a.x()*b.x() + a.y()*b.y();
    }
}
