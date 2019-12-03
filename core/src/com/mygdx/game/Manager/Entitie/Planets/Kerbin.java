package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Kerbin extends Cbody{
    public Kerbin(Cbody pBody){
        super();
        textureAtlas = new TextureAtlas("sprites.txt");
        name = "kerbin";
        sprite = textureAtlas.createSprite(name);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=600000;
        mass=5.3*Math.pow(10,22);
        semiA = 13_600_000_000.0;
        semiB = 13_600_000_000.0;

        Random rand = new Random();
        loc = new Vector2((float)(semiA) + parentBody.getX(), 0);
        sprite.setSize((float)radius,(float)radius);
    }

    @Override
    public void update(float dt) {

    }
}