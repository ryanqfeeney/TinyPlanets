package com.mygdx.game.Manager.Entities.Planets.Nars;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entities.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;
import com.mygdx.game.Manager.Utility.Colors;

public class Nars extends Cbody {
    public Nars(Cbody pBody, PlayState ps) {
        super(ps);
        name = "nars";
        sprite = new Sprite(ps.assets.manager.get(Assets.nars));
        tvg = ps.assets.tvgManager.get("planets/nars/nars.tvg", TinyVG.class);
        parentBody = pBody;
        parentBody.addChild(this);
        radius = 800_000;
        sVertices = generateVertices(2500,8);
        rotateRate = -0.011;
        mass = 4.51 * Math.pow(10, 21);
        semiA = 20_726_155_264.0;
        semiB = semiA * Math.sqrt(1-Math.pow(.051,2));
        w = 0;
        startAnom = Math.toRadians(Cbody.testROT);
        soir = 47_921_949.0;
        cirCol = Colors.colorToIntArray(Colors.NARS_CIRCLE);
        fStart = 15000;
        fEnd =   200000;
        afterCall();
    }

}
