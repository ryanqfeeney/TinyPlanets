package com.mygdx.game.Manager.Utility.Huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import math.geom2d.Point2D;

import java.util.Date;


public class PlayStateHud implements Disposable{

    public Stage stage;
    private Viewport viewport;
    PlayState ps;

    int mult = 1;
    float compassScale = .2f;
    //Scene2D Widgets
    private Label multNumberLabel, multLabel, timeLabel, useForSomethingBetter,
            velLabel, velLabelCopy, actualVel, actualVelCopy, altLabel, actualAlt;
    Sprite compass;
    ShapeRenderer compassBackground;

    float dashX, dashY, cWidth, cHeight, dashW, dashH;


    public PlayStateHud(PlayState ps, SpriteBatch sb) {



        this.ps = ps;

        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,sb );


        multLabel = new Label("MULTIPLIER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        multNumberLabel = new Label(String.format("%06d", mult), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        useForSomethingBetter = new Label(String.format("%06d", 20), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        velLabel = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVel = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        //define a table used to organize hud's labels
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(multLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(velLabel ).expandX().padTop(10);
        table.row();
        table.add(multNumberLabel).expandX();
        table.add(useForSomethingBetter).expandX();
        table.add(actualVel ).expandX();

        stage.addActor(table);


        //compass dash stuff
        velLabelCopy = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVelCopy = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        altLabel = new Label("ALTITUDE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        double alt = ps.getKlobjects().get(0).getLoc().minus(ps.getKlobjects().get(0).getParentBody().getLoc()).distance(0,0);
        actualAlt = new Label(String.format("%.2f",alt), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table compassTable = new Table();
        compassTable.bottom();
        compassTable.setFillParent(true);

        int padR = ps.getScreenWidth() - 500;
        int padB = 50;


        compassTable.add(altLabel).padRight(padR);
        compassTable.row();
        compassTable.add(actualAlt).padRight(padR).padBottom(20);
        compassTable.row();
        compassTable.add(velLabelCopy).padRight(padR);
        compassTable.row();
        compassTable.add(actualVelCopy ).padRight(padR).padBottom(padB);



        stage.addActor(compassTable);



      initDash();

    }
    public void initDash(){
        compassBackground = new ShapeRenderer();

        dashW = 300;
        dashH = 150;
        dashX = 5f;
        dashY = 5f;
        int border = 15;

        compass = new Sprite(ps.assets.manager.get(Assets.spaceship));
        compass.setSize(dashH/compass.getHeight()* compass.getWidth(),dashH);
        compass.setOrigin(compass.getWidth() / 2, compass.getHeight() / 2);
        compass.setRotation((float) Math.toDegrees(ps.getKlobjects().get(0).getRotation()-Math.PI/2));

        dashW = 2*border+dashW;
        dashH = 2*border+dashH;

        cWidth = compass.getWidth();
        cHeight = compass.getHeight();

        compass.setPosition(dashX-cWidth/2+cHeight/2+border,dashY+border);
    }
    public void update(float dt){
        multNumberLabel.setText(mult + "X");
        timeLabel.setText(new Date().toString().toUpperCase()+"");
        //useForSomethingBetter.setText(new Date().toString().toUpperCase()+"");
        compass.setRotation((float) (Math.toDegrees(ps.getKlobjects().get(0).getRotation()-Math.PI/2)+ps.getCamRotation()));
        double vel = ps.getKlobjects().get(0).getVel().distance(0,0);

        actualVel.setText(String.format("%.2f",vel));
        actualVelCopy.setText(String.format("%.2f",vel));

        double p = ps.getKlobjects().get(0).getLoc().minus(ps.getKlobjects().get(0).getParentBody().getLoc()).distance(0,0);

        if (p >= 1000000){
            p = p /1000;
            actualAlt.setText(String.format("%.2f",p) + "K");
            useForSomethingBetter.setText("ALTITUDE: " + String.format("%.2f",p) + "K");
        }
        else{
            actualAlt.setText(String.format("%.2f",p) + "M");
            useForSomethingBetter.setText("ALTITUDE: " + String.format("%.2f",p) + "M");
        }


    }

    @Override
    public void dispose() {
        stage.dispose();


    }
    public void draw(SpriteBatch batch){

        batch.setProjectionMatrix(getHCamera().combined);
        compassBackground.setProjectionMatrix(getHCamera().combined);
        compassBackground.begin(ShapeRenderer.ShapeType.Filled);
        try {
            compassBackground.rect((dashX),(dashY),dashW,dashH);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
        }
        compassBackground.setColor(100f/255f, 100f/255f, 100f/255f, 1f);
        compassBackground.setColor(100f/255f, 100f/255f, 100f/255f, 1f);
        compassBackground.end();
        stage.draw();
        batch.begin();
        compass.draw(batch);
        batch.end();
    }


    public void setMult(double mult) {
        this.mult = (int)mult;
    }
    public Camera getHCamera(){return viewport.getCamera();}



}