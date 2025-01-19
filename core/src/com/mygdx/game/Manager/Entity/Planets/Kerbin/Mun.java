package com.mygdx.game.Manager.Entity.Planets.Kerbin;

import com.mygdx.game.Manager.Utility.Colors;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;


public class Mun extends Cbody {
    public Mun(Cbody pBody,PlayState ps){
        super(ps);
        name = "mun";
        sprite = new Sprite(ps.assets.manager.get(Assets.mun));
        tvg = ps.assets.tvgManager.get("planets/kerbin/mun/mun.tvg", TinyVG.class);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=400_000;
        sVertices = generateVertices(2500,8);
        rotateRate = .016;
        mass=9.76*Math.pow(10,20);
        semiA = 9_000_000.0;
        semiB = 9_000_000.0;
        soir = 2_429_559.1;
        w = 0;
        cirCol = Colors.colorToIntArray(Colors.MUN_CIRCLE);
        fStart = 3000;
        fEnd   = 10000;
        afterCall();




    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }
}