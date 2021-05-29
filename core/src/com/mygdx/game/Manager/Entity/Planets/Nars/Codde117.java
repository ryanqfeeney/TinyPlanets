package com.mygdx.game.Manager.Entity.Planets.Nars;


import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Sprites.Sprite;


public class Codde117 extends Cbody {
    public Codde117(Cbody pBody, PlayState ps){
        super(ps);
        name = "codde117";
        sprite = new Sprite(ps.assets.manager.get(Assets.mun));
        parentBody = pBody;
        parentBody.addChild(this);
        radius=420_00;
        rotateRate = -.025;
        mass=3.14*Math.pow(10,20);
        semiA = 3_200_000.0;
        semiB = 3_200_000.0;
        soir = 1_049_598.9;
        w = 135;
        cirCol = new int[]{56,207,33};
        fStart = 3000;
        fEnd   = 10000;
        afterCall();

    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }
}