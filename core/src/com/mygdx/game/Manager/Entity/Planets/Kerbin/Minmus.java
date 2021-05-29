package com.mygdx.game.Manager.Entity.Planets.Kerbin;

import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Sprites.Sprite;


public class Minmus extends Cbody {
    public Minmus(Cbody pBody, PlayState ps){
        super(ps);
        sprite = new Sprite(ps.assets.manager.get(Assets.minmus));
        name = "minmus";
        parentBody = pBody;
        parentBody.addChild(this);

        rotateRate = -.02;
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