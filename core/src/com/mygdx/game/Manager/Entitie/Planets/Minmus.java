package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;


public class Minmus extends Cbody{
    public Minmus(Cbody pBody){
        super();
        textureAtlas = new TextureAtlas("sprites.txt");
        name = "mun";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=600_000;
        mass=2.65*Math.pow(10,19);
        semiA = 47_000_000.0;
        semiB = 27_000_000.0;
        orbRotation = 45;
        focus = findFocus(semiA, semiB);
        loc = new Vector2(0f,0f);
        moveOnOrbit(0);
        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }


}