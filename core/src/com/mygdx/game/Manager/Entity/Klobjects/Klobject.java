package com.mygdx.game.Manager.Entity.Klobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Manager.Entity.Planets.Cbody;

public class Klobject {

    String name;
    TextureAtlas textureAtlas;
    Sprite sprite;
    Cbody parentBody;
    Vector2 loc;
    Vector2 vel;
    double MULTIPLIER;
    static double gravConstanat = 6.67*Math.pow(10,-11);

    protected double mass;
    protected double rotateRate;

//    protected double semiA;
//    protected double semiB;
//    protected double orbRotation;
//    protected double focus;
//    protected double soir;

    public Klobject(Cbody cb){

        mass = 1;
        MULTIPLIER = 1;
        rotateRate = 0;
        parentBody = cb;
        cb.addKlob(this);
        loc = new Vector2((float)(parentBody.getX()),
                        (float)(parentBody.getY() + 680_000f ));

        vel = new Vector2((float)-2296,0f);

        textureAtlas = new TextureAtlas("spaceship.txt");
        name = "spaceship";
        sprite = textureAtlas.createSprite(name);
        sprite.setScale(800f,800f);
        //sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }



    public void update(float dt){


        double a = Math.atan2(loc.x-parentBody.getX(), loc.y-parentBody.getY());
        a = Math.toRadians(90)-a;

        double forcedub = (gravConstanat*parentBody.getMass()/Math.pow(loc.dst(parentBody.getLoc()),2));
        Vector2 force = new Vector2((float)((-1)*forcedub*Math.cos(a)),(float)((-1)*forcedub*Math.sin(a)));

        vel.x += force.x*dt*MULTIPLIER;
        vel.y += force.y*dt*MULTIPLIER;

        float dx = (float) (dt*vel.x*MULTIPLIER);
        float dy = (float) (dt*vel.y*MULTIPLIER);

        loc.set(loc.x+dx, loc.y+dy);

    }

    public void rotate(float dt){
        sprite.rotate((float)((rotateRate*dt*MULTIPLIER)%360));
    }

//    public void moveOnOrbit(double t){
//        this.t += t;
//
//        double h = Math.pow((semiA - semiB),2)/Math.pow((semiA + semiB),2);
//        double p = Math.PI * (semiA+semiB) *( 1 + (3*h)/(10 + Math.sqrt(4-3*h)));
//        p = p / (2 * Math.PI);
//        double q = this.t/p;
//
//        float x = (float)(parentBody.getX() + semiA*Math.cos(q)*Math.cos(orbRotation) -
//                semiB*Math.sin(q)*Math.sin(orbRotation) + focus*Math.cos(orbRotation));
//
//        float y = (float)(parentBody.getY() + semiA*Math.cos(q)*Math.sin(orbRotation) +
//                semiB*Math.sin(q)*Math.cos(orbRotation) + focus*Math.sin(orbRotation));
//
//        float dx = x - loc.x;
//        float dy = y - loc.y;
//
//        loc.set(x,y);
//
//    }
//
//    public double findFocus(double a, double b){
//        return Math.sqrt(a*a - b*b);
//    }
//
//
//    public double calculateVelocity() {
//
//        double dist = loc.dst(parentBody.getLoc());
//        if (Double.isNaN(dist)){
//            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY "+getName());
//            System.exit(0);
//        }
//        double vel = MULTIPLIER*Math.sqrt(gravConstanat*parentBody.getMass()*((2/dist)-(1/semiA)));
//        if (Double.isNaN(vel)){
//            vel = MULTIPLIER*Math.sqrt(gravConstanat*parentBody.getMass()*(Math.abs((2/dist)-(2/dist))));
//            if(Double.isNaN(vel)) {
//                System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY 2 "+getName());
//                System.exit(0);
//            }
//        }
//        if(vel==0){
//            vel = 1;
//        }
//        return vel;
//
//    }

    public float getX(){
        return loc.x;
    }
    public float getY(){
        return loc.y;
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
    public Vector2 getLoc(){
        return loc;
    }
}


