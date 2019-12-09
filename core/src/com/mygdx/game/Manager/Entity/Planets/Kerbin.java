package com.mygdx.game.Manager.Entity.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Kerbin extends Cbody{
    public Kerbin(Cbody pBody){
        super();
        textureAtlas = new TextureAtlas("sprites.txt");
        name = "kerbin";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=600_000;
        mass=5.3*Math.pow(10,22);
        semiA = 13_600_000_000.0;
        semiB = 13_600_000_000.0;
        orbRotation = 0;
        soir = 84_159_286.0;
        focus = findFocus(semiA, semiB);
        loc = new Vector2(0f,0f);
        moveOnOrbit(0);
        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }


}