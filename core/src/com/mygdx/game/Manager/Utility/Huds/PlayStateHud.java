package com.mygdx.game.Manager.Utility.Huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.GameStates.PlayState;

public class PlayStateHud implements Disposable{

    public Stage stage;
    private Viewport viewport;

    int mult = 1;
    //Scene2D Widgets
    private Label multNumberLabel, multLabel, timeLabel, actualTime;

    public PlayStateHud(PlayState ps, SpriteBatch sb) {



        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,sb );


        multLabel = new Label("MULTIPLIER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        multNumberLabel = new Label(String.format("%06d", mult), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualTime = new Label(String.format("%06d", 20), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //define a table used to organize hud's labels
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(multLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(multNumberLabel).expandX();
        table.add(actualTime).expandX();

        stage.addActor(table);

    }
    public void update(float dt){
        multNumberLabel.setText(mult + "X");
        actualTime.setText(System.currentTimeMillis()+"");
    }

    public void setMult(double mult) {
        this.mult = (int)mult;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}