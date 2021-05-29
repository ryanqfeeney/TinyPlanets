package com.mygdx.game.Manager.Entity.Planets.Kerbin;

import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Sprites.Sprite;

public class Kerbin extends Cbody {
    public Kerbin(Cbody pBody, PlayState ps){
        super(ps);
        name = "kerbin";
        sprite = new Sprite(ps.assets.manager.get(Assets.kerbin));
        parentBody = pBody;
        parentBody.addChild(this);
        radius=1_000_000;
        rotateRate = 0.0166;
        mass=5.3*Math.pow(10,22);
        semiA = 13_600_000_000.0;
        semiB = 13_600_000_000.0;
        w = 0;
        startAnom = Math.toRadians(150);
        soir = 84_159_286.0;
        cirCol = new int[]{0,30,255};
        fStart = 200000;
        fEnd   = 2000000;
        afterCall();
    }

}