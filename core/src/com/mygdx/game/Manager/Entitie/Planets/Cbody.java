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
    double gravConstanat;

    ArrayList<Cbody> children;

    protected double t;

    protected double radius;
    protected double mass;
    protected double rotateRate;




    protected double semiA;
    protected double semiB;

    public Cbody(){
        loc = new Vector2(1f,1f);
        children = new  ArrayList<Cbody>();

        gravConstanat = 6.67*Math.pow(10,-11);
        MULTIPLIER = 1;

        rotateRate = 1;
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
        double focus = findFocus(semiA, semiB);

        double h = Math.pow((semiA - semiB),2)/Math.pow((semiA + semiB),2);

        double p = Math.PI * (semiA+semiB) *( 1 + (3*h)/(10 + Math.sqrt(4-3*h)));

        p = p / (2 * Math.PI);

        double q = this.t/(2*Math.PI*semiA);

        float x = (float)(parentBody.getX() + focus + semiA*Math.cos(this.t/p));
        float y = (float)(parentBody.getY() + semiB*Math.sin(this.t/p));

        float dx = x - loc.x;
        float dy = y - loc.y;

        loc.set(x,y);
        //loc.lerp(new Vector2(x,y),1*dt);
        for (Cbody child: children){
            //child.loc.lerp(new Vector2(child.getX()+dx,child.getY()+dy), 1*dt);
            child.loc.set(child.getX()+dx,child.getY()+dy);
        }
    }

    public double findFocus(double a, double b){
        return Math.sqrt(a*a - b*b);
    }

    public double calculateVelocity(){
        double dist = loc.dst(parentBody.loc);
        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY");
            System.exit(0);
        }
        /////Should put catch for divide by 0 // catch for sqrt of -#
        return MULTIPLIER*Math.sqrt(gravConstanat*parentBody.mass*((2/dist)-(1/semiA)));
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


