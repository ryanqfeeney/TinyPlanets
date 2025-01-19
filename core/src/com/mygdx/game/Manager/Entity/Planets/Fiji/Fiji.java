package com.mygdx.game.Manager.Entity.Planets.Fiji;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;
import com.mygdx.game.Manager.Utility.Colors;

public class Fiji extends Cbody {
    public Fiji(Cbody pBody, PlayState ps){
        super(ps);
        name = "fiji";
        sprite = new Sprite(ps.assets.manager.get(Assets.fiji));
        tvg = ps.assets.tvgManager.get("planets/fiji/fiji.tvg", TinyVG.class);
        parentBody = pBody;
        parentBody.addChild(this);
        radius =900_000;
        sVertices = generateVertices(2500,8);
        rotateRate = 0.0110;
        mass=1.22*Math.pow(10,23);
        semiA = 9_832_684_544.0;
        semiB = semiA * Math.sqrt(1-Math.pow(.01,2));
        w = 0;
        startAnom = Math.toRadians(-97);
        soir = 85_109_365.0;
        cirCol = Colors.colorToIntArray(Colors.FIJI_CIRCLE);
        fStart = 200000;
        fEnd   = 2000000;
        afterCall();
    }
}
