package com.mygdx.game.Manager.Entity.Klobjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import math.geom2d.Point2D;


public class Klobject extends Cbody {
    float drr = .5f;
    public Klobject(Cbody cb, PlayState pstate) {
        super(cb,pstate);

        name = "spaceship";
        mass = 1;
        MULTIPLIER = 1;
        rotateRate = 0;//Math.random()*20 - 10 ;
        rotation = -90;
        parentBody = cb;
        //acceleration = false;

        parentBody.addKlob(this);

        state = new State();

        Double rote = Math.toRadians(3.0/2*Math.PI);
        double dist = 900_000;
        double vel = 2500.0 * sp; // positive goes ccw similar to angles // K

        scale = ps.getScale();

        state.pos = new Point2D(parentBody.getX() + Math.cos(rote) * dist,
                parentBody.getY() + Math.sin(rote) * dist);

        state.vel = new Point2D(-Math.sin(rote) * vel,
                Math.cos(rote) * vel);


        onPathShape = new ShapeRenderer();
        fStart = 20;
        fEnd   = 70;

        sprite = new Sprite(ps.assets.manager.get(Assets.spaceship));
        sprite.setScale(1f/ps.getScale());
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRotation((float)rotation);
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
        //sprite.setRotation((float)rotation);
        bake();

    }

    public Klobject(Cbody cb, PlayState ps, Point2D pos, Point2D vel) {
        this(cb,ps);
        state.pos = pos;
        state.vel = vel;
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
        System.out.println(fade);
        if (fade > 1){
            fade = 1;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        path.setProjectionMatrix(ps.getCamera().combined);
        path.begin(ShapeRenderer.ShapeType.Line);
        try {
            int vertsSize = 2500;
            float[] verts = getVerts(vertsSize); //May not return the same size array if a point exits the soir
            if (verts.length != 2*vertsSize){    //If the length of getVerts return is less than the input paramater, the orbit is cut short. Either by collision or soir escape.
                path.setColor(255f, 0f, 0f, (float)fade);
            }
            else{
                path.setColor(0f, 255f, 0f, (float)fade);
            }
            path.polyline(verts);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
        }
        path.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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

        checkSoir();
        rotate(dt);
        ///System.out.println(state.pos.minus(new Point2D(ps.getCamX(),ps.getCamY()))  + " "+ sprite.getX() + " " +sprite.getY());

    }

    //check if klobject has crossed into or out of a sphere of influence
    public void checkSoir(){
        if (getLoc().distance(parentBody.getLoc()) > parentBody.getSoir()) {
            state.vel = state.vel.plus(parentBody.getVel());
            parentBody = parentBody.getParentBody();
            bake();
        }
        else {
            for (Cbody child : parentBody.getChildren()) {
                if (child.equals(this)) { }
                else if (getLoc().distance(child.getLoc()) < child.getSoir()) {
                    state.vel = state.vel.minus(child.getVel());
                    parentBody = child;
                    bake();
                    break;
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
        super.moveOnOrbit(dt);
        //setStateVel();
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

    //public Sprite getKlobSprite() {
        //return klobSprite;
    //}


    public void setMultiplier(double mult) {
        MULTIPLIER = mult;
    }



    public Point2D getLoc() {
        return state.pos;
    }

    public ShapeRenderer getOnPathShape(){
        return onPathShape;
    }

    public double getDRR() {return drr;}

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
        state.vel = in;
    }

    public void setParentBody(Cbody parentBody) {
        this.parentBody = parentBody;
    }


}
