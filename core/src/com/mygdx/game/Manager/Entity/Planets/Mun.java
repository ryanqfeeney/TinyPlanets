package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import math.geom2d.Point2D;


public class Mun extends Cbody {
    public Mun(Cbody pBody){
        super();
        textureAtlas = new TextureAtlas("sprites.txt");
        name = "mun";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=800_000;
        mass=9.76*Math.pow(10,20);
        semiA = 9_000_000.0;
        semiB = 9_000_000.0;
        soir = 2_429_559.1;
        orbRotation = 0;
        focus = findFocus(semiA, semiB);
        loc = new Point2D(0,0);
        moveOnOrbit(0);
        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);


    }

}