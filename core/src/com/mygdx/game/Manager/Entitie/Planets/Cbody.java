package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.sun.javafx.geom.Vec2d;

import java.awt.*;
import java.util.ArrayList;

public class Cbody{


    String name;
    TextureAtlas textureAtlas;
    Sprite sprite;
    Cbody parentBody;
    Vector2 loc;
    double MULTIPLIER;
    double privateMult;

    ArrayList<Cbody> children;

    protected double radius;
    protected double mass;

    protected double t;

    protected double semiA;
    protected double semiB;

    public Cbody(){
        loc = new Vector2(1f,1f);
        children = new  ArrayList<Cbody>();
        privateMult = 1*Math.pow(10,-12);
        MULTIPLIER = 1;
    }


    public void moveOnOrbit(double t){
        this.t += t;
        double focus = findFocus(semiA, semiB);

        double solve = 9_203_545.0 * 1_000_000.0;

        float x = (float)(parentBody.getX() + focus + semiA*Math.cos(this.t));
        float y = (float)(parentBody.getY() + semiB*Math.sin(this.t));

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

    public double calculateVelocity(){
        double dist = loc.dst(parentBody.loc);

        if (Double.isNaN(dist)){
            System.out.println("BREAK BECAUSE NAN ON CALC VELOCITY");
            System.exit(0);
        }

        return privateMult*MULTIPLIER*Math.sqrt(parentBody.mass*((2/dist)-(1/semiA)));  /////Should put catch for divide by 0 // catch for sqrt of -#


    }

    public void update(float dt){
        double distance = dt*calculateVelocity();
        moveOnOrbit(distance);
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
    public double getMULTIPLIER(){
        return MULTIPLIER;
    }
    public void addChild(Cbody child){
        children.add(child);
    }

    public Vector2 getLoc(){
        return loc;
    }
}


