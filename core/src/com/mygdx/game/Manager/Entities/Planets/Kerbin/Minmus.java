package com.mygdx.game.Manager.Entities.Planets.Kerbin;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Manager.Entities.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Colors;
import dev.lyze.gdxtinyvg.TinyVG;



public class Minmus extends Cbody {
    public Minmus(Cbody pBody, PlayState ps){
        super(ps);
        sprite = new Sprite(ps.assets.manager.get(Assets.minmus));
        tvg = ps.assets.tvgManager.get("planets/kerbin/minmus/minmus.tvg", TinyVG.class);
        name = "minmus";
        parentBody = pBody;
        parentBody.addChild(this);
        rotateRate = -.02;
        radius=600_000;
        sVertices = generateVertices(2500,8);
        mass=2.65*Math.pow(10,19);
        semiA = 47_000_000.0;
        semiB = 47_000_000.0;
        soir = 2_247_428.4;
        w = Math.PI / 4.0;
        startAnom = 0;
        cirCol = Colors.colorToIntArray(Colors.MINMUS_CIRCLE);
        fStart = 3000;
        fEnd   = 10000;
        afterCall();
    }


}