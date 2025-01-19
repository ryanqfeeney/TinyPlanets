package com.mygdx.game.Manager.Entity.Planets.Nars;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;
import com.mygdx.game.Manager.Utility.Colors;


public class Hacobo extends Cbody {
    public Hacobo(Cbody pBody, PlayState ps){
        super(ps);
        name = "Hacobo";
        sprite = new Sprite(ps.assets.manager.get(Assets.mun));
        tvg = ps.assets.tvgManager.get("planets/nars/hacobo/hacobo.tvg", TinyVG.class);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=420_00;
        sVertices = generateVertices(2500,8);
        rotateRate = -.025;
        mass=3.14*Math.pow(10,20);
        semiA = 3_200_000.0;
        semiB = 3_200_000.0;
        soir = 1_049_598.9;
        w = 135;
        cirCol = Colors.colorToIntArray(Colors.HACOBO_CIRCLE);
        fStart = 3000;
        fEnd   = 10000;
        afterCall();

    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }
}