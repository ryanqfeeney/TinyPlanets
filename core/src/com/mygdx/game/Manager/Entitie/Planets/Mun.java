package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Mun extends Cbody{
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

        Random rand = new Random();
        loc = new Vector2((float)(semiA) + parentBody.getX(), 0);
        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

    }
}