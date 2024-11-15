package com.mygdx.game.Manager.Entity.Planets.Nars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Manager.Entity.Planets.Cbody;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import dev.lyze.gdxtinyvg.TinyVG;

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
        cirCol = new int[]{173, 74, 11};
        fStart = 15000;
        fEnd =   200000;
        afterCall();
    }

}
