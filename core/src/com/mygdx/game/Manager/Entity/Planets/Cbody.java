package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import math.geom2d.Point2D;


import java.util.ArrayList;

public class Cbody{

    String name;
    TextureAtlas textureAtlas;
    Sprite sprite;
    Cbody parentBody;
    Point2D loc;
    double MULTIPLIER;
    static double gravConstanat = 6.67*Math.pow(10,-11);

    ArrayList<Cbody> children;
    ArrayList<Klobject> klobs;

    protected double t;

    protected double radius;
    protected double mass;
    protected double rotateRate;

    protected double semiA;
    protected double semiB;
    protected double orbRotation;
    protected double focus;
    protected double soir;

    public Cbody(){
        loc = new Point2D(1,1);
        children = new  ArrayList<>();
        klobs = new  ArrayList<>();
        MULTIPLIER = 1;
        rotateRate = .0166;
        orbRotation = 0;
        soir = 0;

    }


    public void update(float dt){
        double distance = dt*calculateVelocity();
        moveOnOrbit(distance);
        rotate(dt);
    }

    public void rotate(float dt){
        sprite.rotate((float)((rotateRate*dt*MULTIPLIER)%360));
    }

    public void moveOnOrbit(double t){
        this.t += t;

        double h = Math.pow((semiA - semiB),2)/Math.pow((semiA + semiB),2);
        double p = Math.PI * (semiA+semiB) *( 1 + (3*h)/(10 + Math.sqrt(4-3*h)));
        p = p / (2 * Math.PI);
        double q = this.t/p;

        double x = (parentBody.getX() + semiA*Math.cos(q)*Math.cos(orbRotation) -
                        semiB*Math.sin(q)*Math.sin(orbRotation) + focus*Math.cos(orbRotation));

        double y =  (parentBody.getY() + semiA*Math.cos(q)*Math.sin(orbRotation) +
                        semiB*Math.sin(q)*Math.cos(orbRotation) + focus*Math.sin(orbRotation));

        double dx = x - loc.getX();
        double dy = y - loc.getY();

        loc = new Point2D(x,y);

        for (Cbody child: children){
            child.setLoc(new Point2D(child.getX()+dx,child.getY()+dy));
        }
        for (Klobject child: klobs){
            child.setLoc(new Point2D(child.getX()+dx,child.getY()+dy));
        }
    }

    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }


    public double calculateVelocity() {

        double dist = loc.distance(parentBody.loc);
        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY "+getName());
            System.exit(0);
        }
        double vel = MULTIPLIER*Math.sqrt(gravConstanat*parentBody.mass*((2/dist)-(1/semiA)));
        if (Double.isNaN(vel)){
            vel = MULTIPLIER*Math.sqrt(gravConstanat*parentBody.mass*(Math.abs((2/dist)-(2/dist))));
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

    public double getX(){
        return loc.getX();
    }
    public double getY(){
        return loc.getY();
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
    public Point2D getLoc(){
        return loc;
    }
    public void setLoc(Point2D newLoc) { loc = newLoc;}

    public double getMass(){
        return mass;
    }
}


