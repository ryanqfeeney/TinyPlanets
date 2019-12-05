package com.mygdx.game.Manager.Entitie.Planets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Sun extends Cbody{
    public Sun(){
        super();
        textureAtlas = new TextureAtlas("sprites.txt");
        name = "sun";
        sprite = textureAtlas.createSprite(name);
        parentBody = this;
        radius=5_061_600_000.0;
        mass = 1.8 * Math.pow(10,28);
        semiA = semiB = 0;
        loc = new Vector2(0f,0f);

        sprite.setSize((float)radius,(float)radius);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    @Override
    public void update(float dt) {

    }
}