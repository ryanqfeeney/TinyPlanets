package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Manager.GameStates.PlayState;
import math.geom2d.Point2D;


public class Mun extends Cbody {
    public Mun(Cbody pBody,PlayState ps){
        super(ps);
        textureAtlas = ps.getCBTextures();
        name = "mun";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=800_000;
        mass=9.76*Math.pow(10,20);
        semiA = 9_000_000.0;
        semiB = 9_000_000.0;
        soir = 2_429_559.1;
        w = 0;
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

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}