package com.mygdx.game.Manager.Entity.Planets;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;
import math.geom2d.Point2D;

public class Sun extends Cbody{
    public Sun(PlayState ps){
        super(ps);
        name = "sun";
        sprite = new Sprite(ps.assets.manager.get(Assets.sun));
        tvg = ps.assets.tvgManager.get("planets/sun.tvg", TinyVG.class);
        tvg.centerOrigin();
        parentBody = this;
        radius= 506160000.0;//5_061_600_000.0;
        sVertices = generateVertices(2500,8);
        mass = 1.8 * Math.pow(10,28);
        semiA = semiB = 0;
        state.pos = new Point2D(0,0);
        soir = Double.MAX_VALUE;
        sprite.setSize((float)radius/ ps.getScale(),(float)radius/ps.getScale());
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        cirCol = new int[]{219,216,42};
        rotateRate = 1;

    }

    @Override
    public void update(float dt) {
        rotate(dt);
    }

    @Override
    public void drawPath() {

    }

    public Sprite getSprite(){
        return sprite;
    }
}