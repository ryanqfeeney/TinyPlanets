package com.mygdx.game.Manager;

import com.mygdx.game.Manager.GameStates.GameState;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;

import java.util.ArrayList;

public class GameStateManager {

    int currentState;
    ArrayList<GameState> states;
    Assets assets;

    public GameStateManager(Assets assets){
        currentState = 0;
        this.assets = assets;
        System.out.println("GSM Made!");
        states = new ArrayList<GameState>();
        init();
    }

    public void init(){
        states.add(new PlayState(assets));
    }
    public void render(float dt){
        states.get(currentState).render(dt);
    }

    public void dispose(){
        states.get(currentState).dispose();
    }

    public void handleInput(){
        states.get(currentState).handleInput();
    }
}
