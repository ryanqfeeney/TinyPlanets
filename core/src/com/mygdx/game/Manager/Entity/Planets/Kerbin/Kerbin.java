package com.mygdx.game.Manager.Entity.Planets.Kerbin;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;
import com.mygdx.game.Manager.Utility.Colors;

public class Kerbin extends Cbody {
    public Kerbin(Cbody pBody, PlayState ps){
        super(ps);
        name = "kerbin";
        sprite = new Sprite(ps.assets.manager.get(Assets.kerbin));
        tvg = ps.assets.tvgManager.get("planets/kerbin/kerbin.tvg", TinyVG.class);
        parentBody = pBody;
        parentBody.addChild(this);
        radius=1_000_000;
        sVertices = generateVertices(2500,8);
        rotateRate = 0.0166;
        mass=5.3*Math.pow(10,22);
        semiA = 13_600_000_000.0;
        semiB = 13_600_000_000.0;
        w = 0;
        startAnom = Math.toRadians(150);
        soir = 84_159_286.0;
        cirCol = Colors.colorToIntArray(Colors.KERBIN_CIRCLE);
        fStart = 200000;
        fEnd   = 2000000;
        rotation = 0;
        afterCall();
    }

}