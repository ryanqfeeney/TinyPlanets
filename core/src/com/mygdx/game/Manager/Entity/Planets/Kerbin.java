package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;
import org.apache.commons.math3.distribution.TDistribution;


public class Kerbin extends Cbody{
    public Kerbin(Cbody pBody, PlayState ps){
        super(ps);
        textureAtlas = ps.getCBTextures();
        name = "kerbin";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=600_000;
        mass=5.3*Math.pow(10,22);
        semiA = 13_600_000_000.0;
        semiB = 13_600_000_000.0;
        w = 0;
        soir = 84_159_286.0;
        focus = findFocus(semiA, semiB);
        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

        Double rote = Math.toRadians(0);

        state.pos = new Point2D(parentBody.getX() + Math.cos(rote)*semiA,
                parentBody.getY() + Math.sin(rote)*semiA);

        double vel = calculateVelocity()*sp; // positive goes ccw similar to angles // K

        state.vel = new Point2D(-Math.sin(rote)*vel,
                Math.cos(rote)*vel);

        bake();
    }


}