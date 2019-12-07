package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.javafx.geom.Vec2d;
import jdk.nashorn.internal.runtime.ECMAException;

import java.awt.*;
import java.util.ArrayList;

public class Cbody{

    String name;
    TextureAtlas textureAtlas;
    Sprite sprite;
    Cbody parentBody;
    Vector2 loc;
    double MULTIPLIER;
    static double gravConstanat = 6.67*Math.pow(10,-11);

    ArrayList<Cbody> children;

    protected double t;

    protected double radius;
    protected double mass;
    protected double rotateRate;

    protected double test;
    protected double semiA;
    protected double semiB;
    protected double orbRotation;
    protected double focus;

    public Cbody(){
        loc = new Vector2(1f,1f);
        children = new  ArrayList<Cbody>();
        MULTIPLIER = 1;
        rotateRate = .0166;
        test = 0;
        orbRotation = 0;
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
        double q = this.t/(2*Math.PI*semiA);

        float x = (float)(parentBody.getX() + semiA*Math.cos(this.t/p)*Math.cos(orbRotation) -
                        semiB*Math.sin(this.t/p)*Math.sin(orbRotation) + focus*Math.cos(orbRotation));

        float y = (float)(parentBody.getY() + semiA*Math.cos(this.t/p)*Math.sin(orbRotation) +
                        semiB*Math.sin(this.t/p)*Math.cos(orbRotation) + focus*Math.sin(orbRotation));

        float dx = x - loc.x;
        float dy = y - loc.y;

        loc.set(x,y);
        for (Cbody child: children){
            child.loc.set(child.getX()+dx,child.getY()+dy);
        }
    }

    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }


    public double calculateVelocity() {

        double dist = loc.dst(parentBody.loc);
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


    public float getX(){
        return loc.x;
    }
    public float getY(){
        return loc.y;
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
    public Vector2 getLoc(){
        return loc;
    }
}


