package com.mygdx.game.Manager.Entity.Planets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject2;
import com.mygdx.game.Manager.Entity.Klobjects.PathKlob;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.FeeneyShapeRenderer;
import math.geom2d.Point2D;
import org.apache.commons.math3.analysis.function.Atanh;
import org.apache.commons.math3.analysis.function.Sinh;


import java.io.IOException;
import java.util.ArrayList;


public class Cbody{


    protected int sp = 1;
    public static double testROT = 67.6+150;

    protected String name;
    protected Sprite sprite;
    protected Cbody parentBody;
    protected State state;
    protected double MULTIPLIER;
    protected static double gravConstant = 6.67*Math.pow(10,-11);

    protected ArrayList<Cbody> children;
    protected ArrayList<Klobject> klobs;

    protected boolean ccw;

    protected double semiA, semiB, peri, apoap, ecc, w, Eanom, Manom, Tanom, startAnom, tmax;
    protected Point2D eccVecc;

    protected double s;

    protected double radius;
    protected double mass;
    protected double rotateRate;
    protected double rotation;

    protected double focus;
    protected double soir;

    protected PlayState ps;
    protected FeeneyShapeRenderer path;
    protected FeeneyShapeRenderer onPathShape;
    protected PathKlob pathKlob;
    protected boolean escapePath;
    public float scale, circleSize, fStart, fEnd, fPathMax, fCirleMax;
    protected int[] cirCol;




    public Cbody(PlayState ps){
        children = new  ArrayList<>();
        klobs = new  ArrayList<>();
        circleSize = 9f;
        MULTIPLIER = 1;
        rotateRate = 0;
        w = 0;
        soir = 0; //sphere of influence radiance
        path = new FeeneyShapeRenderer();
        onPathShape = new FeeneyShapeRenderer();
        cirCol = new int[]{255,0,0};
        path.setProjectionMatrix(ps.getCamera().combined);
        state = new State();
        fPathMax = .8f;
        fCirleMax = .75f;
        this.ps = ps;
    }

    public void afterCall() {
        scale = ps.getScale();
        focus = findFocus(semiA, semiB);
        sprite.setSize((float)radius / (float)ps.getScale(),(float)radius/(float)ps.getScale());
        sprite.setSize((float)radius / (float)ps.getScale(),(float)radius/(float)ps.getScale());
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

        fixStartAnom();
        rr = (semiA*(1-(ecc*ecc))) / (1 + (ecc * Math.cos(startAnom)));


        x = parentBody.getX() + rr*Math.cos(startAnom + w);
        y = parentBody.getY() + rr*Math.sin(startAnom + w);

//        dx = x - getLoc().getX();
//        dy = y - getLoc().getY();


        state.pos = new Point2D(x,y);
//        state.pos = new Point2D(parentBody.getX() + Math.cos(startAnom)*semiB,
//                parentBody.getY() + Math.sin(startAnom)*semiB);

        double vel = calculateVelocity()*sp; // positive goes ccw similar to angles // K

        state.vel = new Point2D(-Math.sin(startAnom+w)*vel,
                Math.cos(startAnom+w)*vel);


        bake();

    }

    public Cbody(Cbody parentBody,PlayState ps){
        this(ps);
        this.parentBody = parentBody;
    }

    public Cbody(){

    }

    public void update(float dt) {
        moveOnOrbit(dt);
        rotate(dt);
        fixStartAnom();

    }

    public void fixStartAnom(){
        while ( startAnom < (-Math.PI)){
            startAnom += (2*Math.PI);
        }
        while ( startAnom > (Math.PI)){
            startAnom -= (2*Math.PI);
        }
    }


    public void rotate(float dt){
        if (rotateRate!=0) {
            sprite.rotate((float) ((rotateRate * dt * MULTIPLIER) % 360));
        }
        rotation = (Math.toRadians(sprite.getRotation())+(Math.PI/2))%(2*Math.PI);
    }

    public void move(float dt){

    }


    protected double velo, x, y, dSd0, q, rr, dx, dy, normMid, velAngle, phi;
    public void moveOnOrbit(double dt){
        velo = state.vel.distance(0, 0);

        if (ccw) {
            this.s = MULTIPLIER * velo * dt;

        } else {
            this.s = -MULTIPLIER * velo * dt;
        }

        // IF ITS FALLING DIRECTLY TO PARENT IT CRASHES > GOTTA FIX

        dSd0 = (((peri * (1+ecc) * Math.sqrt(1+Math.pow(ecc,2) + (2*ecc*Math.cos(Tanom)))) / Math.pow(1+(ecc*Math.cos(Tanom)),2)));
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

        //if(getName().equals("spaceship")){System.out.println("" + state.pos.getX() + " | " + Tanom  );}
        state.pos = new Point2D(x,y);
        if (Double.isNaN(state.pos.getX())) {
            System.out.println("HERE");
        }
        if (Double.isNaN(state.vel.getX())) {
            System.out.println("HERE2");
        }

        updateChildren(dx,dy);

        calcAnomalies();

        setStateVel();
    }

    public float[] getVerts(int size, double redo){


        if (ecc + .00001 > 1 && getPeri() - .00001 < 0 ){
            float sx = (float) (getX() -ps.getCamX())/scale;
            float sy = (float) (getY() -ps.getCamY())/scale;
            float px = (float) (parentBody.getX() -ps.getCamX())/scale;
            float py = (float) (parentBody.getY() -ps.getCamY())/scale;
            return new float[]{sx,sy,px,py};
        }

        float [] verts = new float[2*size];
        double rr;
        double q;
        double dq;

        q = startAnom;

        if (redo != 0 ){  //redo will only be non zero when getVerts() is recursively called. getVerts() will be recursively called when it initially hits a planet. The redo is to get higher accuracy with high eccentricy orbits
            dq = redo / size;
        }
        else if (ecc < 1){
            dq = 2*Math.PI/size;
            if(!ccw)
            {
                dq=-dq;
            }
        }
        else {
            dq = distance(tmax,startAnom)/size;
        }

        verts[0] = (float) (getX() -ps.getCamX())/scale;
        verts[1] = (float) (getY() -ps.getCamY())/scale;

        for (int i = 1; i < size; i++){

            rr = (semiA*(1-(ecc*ecc))) / (1 + (ecc * Math.cos(q)));
            double x = (parentBody.getX() + rr*Math.cos(q + w));
            double y = (parentBody.getY() + rr*Math.sin(q + w));

            if (i == size - 1 && ecc < 1){
                verts[2*i]   = (float) (getX() -ps.getCamX())/scale;
                verts[2*i+1] = (float) (getY() -ps.getCamY())/scale;

            }
            else{
                verts[2*i]   = (float) (x-ps.getCamX())/scale;
                verts[2*i+1] = (float) (y-ps.getCamY())/scale;

                Point2D di = new Point2D(x,y);
                double dist = di.distance(parentBody.getLoc());

                if(dist < parentBody.getRadius()/2 && redo == 0) {
                    if(getLoc().distance(parentBody.getLoc()) > parentBody.getRadius()/2){
                        return getVerts(size, q-startAnom); // Boom
                    }

                }
            }


            if (rr > parentBody.getSoir()) {
                float[] copyVerts = new float[i*2];
                System.arraycopy(verts,0,copyVerts,0,i*2);

                if (pathKlob != null ) {
                    if (pathKlob.pathKlob == null) {
                        pathKlob.pathKlob = new PathKlob(pathKlob);
                    }
                    escapePath = true;
                    pathKlob.setParentBody(this.getParentBody());
                    pathKlob.setLoc(new Point2D(x, y));
                    pathKlob.copyProps(this);


                    pathKlob.setAnoms(q);
                    pathKlob.setStateVel();
                    pathKlob.bake();


                    if (ecc > 1){
                        pathKlob.dt = (Math.pow((Math.pow(-semiA,3)/gm),(1.0/2))*pathKlob.getManom());
                        pathKlob.dt -= (Math.pow((Math.pow(-semiA,3)/gm),(1.0/2))*getManom());
                    }
                    else{
                        pathKlob.dt = (Math.pow((Math.pow(semiA,3)/gm),(1.0/2))*pathKlob.getManom());
                        pathKlob.dt -= (Math.pow((Math.pow(semiA,3)/gm),(1.0/2))*getManom());

                    }

                    double pbdt, pManom, pPeriod;
                    Point2D pVel;

                    pbdt = pathKlob.dt;

                    pPeriod = Math.PI*2*Math.pow((Math.pow(parentBody.semiA,3)/parentBody.gm),(1.0/2));

                    pbdt = Math.abs(pbdt);

                    pManom = ((2*Math.PI*pbdt)/ (pPeriod) + parentBody.getManom() )  ;


                    //CONVERT MANOM TO E AND T
                    double pTanom = parentBody.convertMtoT(pManom);
                    double pEanom = parentBody.convertTtoE(pTanom);


                    pVel = pathKlob.parentBody.projStateVel(pEanom, pTanom); //SHOULD BE E and T. BUT THEY ARE THE SAME IN CIRCLEs

                    double prr = (pathKlob.parentBody.semiA*(1-(pathKlob.parentBody.ecc*pathKlob.parentBody.ecc))) / (1 + (pathKlob.parentBody.ecc * Math.cos(pManom)));//Use Tanom here
                    pathKlob.setParentBody(this.getParentBody().getParentBody());

                    x = (pathKlob.parentBody.getX() + prr*Math.cos(pManom + parentBody.w) + rr*Math.cos(q + w));
                    y = (pathKlob.parentBody.getY() + prr*Math.sin(pManom + parentBody.w) + rr*Math.sin(q + w));


                    pathKlob.setLoc(new Point2D(x, y));
                    pathKlob.setVel(pathKlob.state.vel.plus(pVel));
                    pathKlob.bake();


                }

                if (i < 3){
                    copyVerts = new float[]{verts[0], verts[1], verts[0], verts[1]+1};
                }
                return copyVerts;
            }
            q+=dq;

        }
        if (redo != 0) {
            float[] retVerts = new float[verts.length-2];;
            System.arraycopy(verts, 0, retVerts, 0, verts.length - 2);
            if(retVerts.length < 4){
                return new float[]{verts[0], verts[1], verts[0], verts[1]+1};
            }
            return retVerts;
        }
        return verts;
    }

    public double distance(double a, double b) {
        double d = Math.abs(a - b) % (2*Math.PI);
        double r = d > (Math.PI) ? (2*Math.PI) - d : d;

        //calculate sign
        int sign = (a - b >= 0 && a - b <= (Math.PI)) || (a - b <=-(Math.PI) && a- b>= -(2*Math.PI)) ? 1 : -1;
        r *= sign;

        while (r < 0 && ccw){
            r += (Math.PI*2);
        }
        while (r > 0 && !ccw){
            r -= (Math.PI*2);
        }
        if(!ccw){


        }

        return r;
    }


    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }


    public double calculateVelocity() {


        double dist = getLoc().distance(parentBody.getLoc());

        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY " + getName());
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


    public double calculateVelocity(Point2D inP) {

        double dist = inP.distance(parentBody.getLoc());

        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY "+getName());
            //System.exit(0);
        }
        double vel = Math.sqrt(gravConstant*parentBody.mass*((2/dist)-(1/semiA)));
        if (Double.isNaN(vel)){
            vel = Math.sqrt(gravConstant*parentBody.mass*(Math.abs((2/dist)-(2/dist))));
            if(Double.isNaN(vel)) {
                System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY 2 "+getName());
                //System.exit(0);
            }
        }
        if(vel==0){
            vel = 1;
        }
        return vel;

    }

    public static double calculateEscapteVel(Cbody parentBody, double dist) {
        return Math.sqrt((2*gravConstant*parentBody.mass)/(dist));
    }
    public void setStateVel(){


        velo = calculateVelocity() * sp;

        if (ecc < 1) {
            state.vel =
                    new Point2D(-semiA * Math.sin(Eanom) * Math.cos(w) - semiB * Math.cos(Eanom) * Math.sin(w),
                            semiB * Math.cos(Eanom) * Math.cos(w) - semiA * Math.sin(Eanom) * Math.sin(w));
            normMid = state.vel.distance(0, 0);
            state.vel = state.vel.scale(velo / normMid);


            if (!ccw) {
                state.vel = new Point2D(-state.vel.x(), -state.vel.y());
            }


        } else if (ecc > 1) {
            phi = Math.atan2(ecc * Math.sin(Tanom), (1 + ecc * Math.cos(Tanom)));
            if (ccw)
                velAngle = Tanom + Math.PI / 2 - phi + w;
            else {
                velAngle = Tanom - Math.PI / 2 - phi + w;
            }

            state.vel = new Point2D(Math.cos(velAngle) * velo, Math.sin(velAngle) * velo);

        }



    }

    public Point2D projStateVel(double eIn, double tIn){


        velo = calculateVelocity() * sp;

        if (ecc < 1) {
            state.vel =
                    new Point2D(-semiA * Math.sin(eIn) * Math.cos(w) - semiB * Math.cos(eIn) * Math.sin(w),
                            semiB * Math.cos(eIn) * Math.cos(w) - semiA * Math.sin(eIn) * Math.sin(w));

            normMid = state.vel.distance(0, 0);
            state.vel = state.vel.scale(velo / normMid);


            if (!ccw) {
                state.vel = new Point2D(-state.vel.x(), -state.vel.y());
            }

        } else if (ecc > 1) {;
            phi = Math.atan2(ecc * Math.sin(tIn), (1 + ecc * Math.cos(tIn)));
            if (ccw)
                velAngle = Tanom + Math.PI / 2 - phi + w;
            else {
                velAngle = Tanom - Math.PI / 2 - phi + w;
            }
            state.vel = new Point2D(Math.cos(velAngle) * velo, Math.sin(velAngle) * velo);

        }
        return state.vel;

    }


    double velSq, r, gm, cz;
    public void bake() {

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
        ccw = cz > 0;


        //System.out.println("here"  + semiA);
        semiA = (gm * r) / (2 * gm - r * velSq);
        semiB = semiA * (Math.sqrt((1 - Math.pow(ecc, 2))));
        peri = semiA * ( 1 - ecc);
        apoap = semiA * ( 1 + ecc);
        focus = findFocus(semiA, semiB);


        //if(getName().toLowerCase().equals("nars")){System.out.println(Tanom + " " +Eanom +" " + Manom + " prebake ANOMALIES IN MOVEONORBIT BEFOR STATEVEL AFTER CALC ANOM()") ;}
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
    }

    public double convertMtoT(double M){


        double T = M + ((2*ecc-.25*Math.pow(ecc,3))*Math.sin(M)) +((5.0/4)*Math.pow(ecc,2)*(Math.sin(2*M)))+((13.0/12.0)*Math.pow(ecc,3)*(Math.sin(3*M)));

        return T;
    }

    public double convertTtoE(double T){

        double E;
        if (ecc < 1) {
            E = Math.atan2((Math.sqrt(1 - Math.pow(ecc, 2)) * Math.sin(T)),
                    (ecc + Math.cos(T)));
        }
        else{
            E = new Atanh().value(Math.sqrt((ecc-1)/(ecc+1))*Math.tan(T/2))*2;

        }

        return E;
    }


    Point2D rrr;
    public void calcAnomalies() {

        rrr = new Point2D(state.pos.x() - parentBody.getX(),
                state.pos.y() - parentBody.getY());

        Tanom = dotProd(eccVecc, rrr) / (ecc * rrr.distance(0, 0));
        if (Tanom > 1) { Tanom = 1;}
        else if (Tanom < -1) {Tanom = -1;}

        Tanom = Math.acos(Tanom);



        if(Double.isNaN(getTanom()) && getName().equals("spaceship")){
            System.out.println( dotProd(eccVecc, rrr) / (ecc*rrr.distance(0,0)));

        }

        if (ecc == 0) {
            Tanom = Math.atan2(rrr.getY(),rrr.getX());
        }

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
    float resetX, resetY, nextX, nextY;

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

        fade*=fPathMax;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        path.setProjectionMatrix(ps.getCamera().combined);
        path.begin(FeeneyShapeRenderer.ShapeType.Line);
        path.setColor(255f/255, 255f/255, 255f/255, (float)fade );

        try {
            int vertsSize = 2500;
            float[] verts = getVerts(vertsSize, 0); //May not return the same size array if a point exits the soir
            path.polyline(verts);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
        }

        path.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    public void drawCircle(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        onPathShape.setProjectionMatrix(ps.getCamera().combined);
        onPathShape.begin(FeeneyShapeRenderer.ShapeType.Filled);
        try{
            onPathShape.circle((float)((getX()-ps.getCamX())/scale),(float)((getY()-ps.getCamY())/scale),circleSize);
        }
        catch (Exception e){
            System.out.println(e);
        }
        onPathShape.setColor(cirCol[0]/256f, cirCol[1]/256f, cirCol[2]/256f, fCirleMax);
        onPathShape.end();
    }

    public void dispose(){
        path.dispose();
    }


    public double getX(){
        return getLoc().getX();
    }
    public double getY(){
        return getLoc().getY();
    }
    public double getRotation() {return rotation;}

    public double getRR(){return rotateRate;}

    public void setRR(double rr){ rotateRate = rr;}

    public FeeneyShapeRenderer getPath() {
        return path;
    }

    public void setPath(FeeneyShapeRenderer path) {
        this.path = path;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiA() {
        return semiA;
    }

    public void setSemiA(double semiA) {
        this.semiA = semiA;
    }

    public double getSemiB() {
        return semiB;
    }

    public void setSemiB(double semiB) {
        this.semiB = semiB;
    }

    public double getPeri() {
        return peri;
    }

    public double getApoap() {
        return apoap;
    }

    public String getName(){
        return name;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public void setMultiplier(double mult){
        MULTIPLIER = mult;
        if (pathKlob != null){
            pathKlob.setMultiplier(mult);
        }
    }
    public void addChild(Cbody child){
        children.add(child);
    }
    public void addKlob(Klobject klob){ klobs.add(klob);}
    public Point2D getLoc(){
        return state.pos;
    }
    public Point2D getVel(){
        return state.vel;
    }
    public void setLoc(Point2D newLoc) { state.pos = newLoc;}

    public double getMass(){
        return mass;
    }

    public double dotProd(Point2D a, Point2D b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    public double crossProd(Point2D a, Point2D b){
        double cp =  a.x()*b.y() - a.y()*b.x();
        System.out.println(cp);
        return cp;
    }

    boolean isMovingTowards(Point2D testPoint) {
        Point2D toPoint = testPoint.minus(state.pos); //a vector going from your obect to the point
        return dotProd(toPoint, state.vel) > 0;
    }

    public double getSoir() {
        return soir;
    }

    public double getW(){
        return w;
    }

    public float getCirlceSize() {
        return circleSize;
    }
    public void setCirlceSize(float in){
        circleSize = in;
    }

    public PlayState getPS(){
        return ps;
    }

    public double getEcc(){
        return ecc;
    }

    public boolean getCCW(){
        return ccw;
    }
    public double getTanom(){
        return Tanom;
    }
    public double getEanom(){
        return Eanom;
    }
    public double getManom(){
        return Manom;
    }




    public ArrayList<Cbody> getChildren(){
        return children;
    }

    public ArrayList<Klobject> getKlobs(){
        return klobs;
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

