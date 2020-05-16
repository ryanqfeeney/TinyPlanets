package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;


public class Minmus extends Cbody {
    public Minmus(Cbody pBody, PlayState ps){
        super(ps);
        textureAtlas = ps.getCBTextures();
        sprite = textureAtlas.createSprite("mun");
        name = "minmus";
        parentBody = pBody;
        parentBody.addChild(this);
        radius=600_000;
        mass=2.65*Math.pow(10,19);
        semiA = 47_000_000.0;
        semiB = 47_000_000.0;
        soir = 2_247_428.4;
        w = 45;
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