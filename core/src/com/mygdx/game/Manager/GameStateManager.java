package com.mygdx.game.Manager;

import com.mygdx.game.Manager.GameStates.GameState;
import com.mygdx.game.Manager.GameStates.PlayState;

import java.util.ArrayList;

public class GameStateManager {

    int currentState;
    ArrayList<GameState> states;

    public GameStateManager(){
        currentState = 0;
        System.out.println("GSM Made!");
        states = new ArrayList<GameState>();
        init();
    }

    public void init(){
        states.add(new PlayState());
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
