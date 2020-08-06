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

import java.util.Date;


public class PlayStateHud implements Disposable{

    public Stage stage;
    private Viewport viewport;
    PlayState ps;

    int mult = 1;

    Table table, compassTable, controlTable;

    int padCompassRight =  -500;
    int padCompassBotton = 50;
    float compassScale = .2f;

    int padControlLeft =  -220;
    int origPad =  -220;

    double controlExitStop = 420;
    double controlExitRate = 8; //keep as an even number
    boolean controlToggle = true;
    boolean stop = true;
    int padControlBotton = 20;




    //Scene2D Widgets
    private final Label  multLabel, altLabel, velLabel, velLabelCopy, multNumberLabel, timeLabel,
            useForSomethingBetter, actualVel, actualVelCopy,  actualAlt;

    //Controls Text
    private Label control_toggle_cam, control_rotate_cam, control_pan_cam, control_zoom_cam, control_throttle_up,
            control_throttle_down, control_next_cbody, control_prev_cbody, control_rotate_countcw, control_rotate_cw,
            control_warp_up, control_warp_down, control_drop_warp, control_jump_to, control_click_to, control_toggle_menu;

    Sprite compass;
    ShapeRenderer compassBackground;

    float dashX, dashY, cWidth, cHeight, dashW, dashH;


    public PlayStateHud(PlayState ps, SpriteBatch sb) {

        this.ps = ps;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,sb );

        padCompassRight += ps.getScreenWidth();
        padControlLeft +=  ps.getScreenWidth();
        origPad += ps.getScreenWidth();


        //****
        //Top Label
        //****

        table = new Table();
        table.top();
        table.setFillParent(true);

        multLabel = new Label("MULTIPLIER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        multNumberLabel = new Label(String.format("%06d", mult), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        useForSomethingBetter = new Label(String.format("%06d", 20), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        velLabel = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVel = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(multLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(velLabel ).expandX().padTop(10);
        table.row();
        table.add(multNumberLabel).expandX();
        table.add(useForSomethingBetter).expandX();
        table.add(actualVel ).expandX();


        //****
        //Compass Label
        //****

        compassTable = new Table();
        compassTable.bottom();
        compassTable.setFillParent(true);

        //compass dash stuff
        velLabelCopy = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVelCopy = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        altLabel = new Label("ALTITUDE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        double alt = ps.getKlobjects().get(0).getLoc().minus(ps.getKlobjects().get(0).getParentBody().getLoc()).distance(0,0);
        actualAlt = new Label(String.format("%.2f",alt), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        compassTable.add(altLabel).padRight(padCompassRight);
        compassTable.row();
        compassTable.add(actualAlt).padRight(padCompassRight).padBottom(20);
        compassTable.row();
        compassTable.add(velLabelCopy).padRight(padCompassRight);
        compassTable.row();
        compassTable.add(actualVelCopy ).padRight(padCompassRight).padBottom(padCompassBotton);


        //****
        //Control Label
        //****

        controlTable = new Table();
        controlTable.bottom();
        controlTable.setFillParent(true);

        setControlLabels(controlTable);

        stage.addActor(table);
        stage.addActor(compassTable);
        stage.addActor(controlTable);


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

        moveControl();

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

    public void setControlLabels(Table controlTable){
        control_pan_cam        = new Label("Pan Camera: RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_cam     = new Label("Rotate Camera: Alt + RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_zoom_cam       = new Label("Zoom Camera: Scroll Wheel", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_throttle_up    = new Label("Throttle Up: Shift", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_throttle_down  = new Label("Throttle Down: Ctrl", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_next_cbody     = new Label("Next C-Body:  +", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_prev_cbody     = new Label("Prev C-Cbody: -", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_countcw = new Label("Rotate CCW: Q", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_cw      = new Label("Rotate  CW: E", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_warp_up        = new Label("Warp Up: >", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_warp_down      = new Label("Warp Down: <", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_drop_warp      = new Label("Drop out of warp: ?", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_jump_to        = new Label("Jump to player: 1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_click_to       = new Label("Click any C-Body to Lock", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_cam     = new Label("Toggle Camera Lock: X", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_menu    = new Label("Toggle Control Menu: M", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        controlTable.add(control_rotate_cam).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_pan_cam ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_zoom_cam).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_throttle_up).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_throttle_down ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_next_cbody ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_prev_cbody ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_rotate_countcw ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_rotate_cw ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_warp_up ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_warp_down ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_drop_warp ).expandX().padLeft(padControlLeft).left().left().row();
        controlTable.add(control_jump_to ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_click_to ).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_toggle_cam).expandX().padLeft(padControlLeft).left().row();
        controlTable.add(control_toggle_menu).expandX().padLeft(padControlLeft).left().row();
        controlTable.padBottom(padControlBotton);

    }
    public void toggleControl(){
        controlToggle = !controlToggle;
        System.out.println(controlToggle);
        stop = false;
    }

    public void moveControl() {
        if (!controlToggle && !stop) {
            if (padControlLeft < (origPad + controlExitStop)) {
                padControlLeft += controlExitRate;
                controlTable.moveBy((float) controlExitRate, 0);
            } else {
                controlToggle = false;
                stop = true;
            }
        } else if (!stop) {
            if (padControlLeft >= (origPad)) {
                padControlLeft -= controlExitRate;
                controlTable.moveBy(-(float) controlExitRate, 0);
            } else {
                controlToggle = true;
                stop = true;
            }

        }

    }


    public void setMult(double mult) {
        this.mult = (int)mult;
    }
    public Camera getHCamera(){return viewport.getCamera();}



}