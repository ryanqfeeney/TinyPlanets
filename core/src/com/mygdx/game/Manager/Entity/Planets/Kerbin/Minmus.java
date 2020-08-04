package com.mygdx.game.Manager.Entity.Planets.Kerbin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import math.geom2d.Point2D;


public class Minmus extends Cbody {
    public Minmus(Cbody pBody, PlayState ps){
        super(ps);
        sprite = new Sprite(ps.assets.manager.get(Assets.minmus));
        name = "minmus";
        parentBody = pBody;
        parentBody.addChild(this);


        radius=600_000;
        mass=2.65*Math.pow(10,19);
        semiA = 47_000_000.0;
        semiB = 47_000_000.0;
        soir = 2_247_428.4;
        w = Math.PI / 4.0;
        startAnom = 0;
        cirCol = new int[]{119,130,118};
        fStart = 3000;
        fEnd   = 10000;
        afterCall();
    }


}