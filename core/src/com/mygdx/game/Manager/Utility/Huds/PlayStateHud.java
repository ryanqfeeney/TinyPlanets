package com.mygdx.game.Manager.Utility.Huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.Entity.Klobjects.Klobject;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.mygdx.game.Manager.Utility.Assets;
import com.mygdx.game.Manager.Utility.Sprites.Sprite;

import java.util.Date;


public class PlayStateHud implements Disposable{

    public Stage stage;
    private Viewport viewport;
    PlayState ps;

    int mult = 1;

    Table table, compassTable, controlTable, statusTable, sasTable;

    int padCompassRight =  -720;
    int padCompassBotton = 35;

    int controlX =  -220;
    int controlY = 20;

    int statusX =  20;
    int statusY = 200 ;




    double controlOrig = -220;
    double controlExitStop = 420;
    double controlExitRate = 12; //keep as an even number
    boolean controlToggle = true;
    boolean controlStop = true;

    double statusOrig = 20;
    double statusExitStop = -350;
    double statusExitRate = 12; //keep as an even number
    boolean statusToggle = true;
    boolean statusStop = true;





    //Scene2D Widgets
    private final Label  multLabel, multNumberLabel, timeLabel, altLabel,
            topFullAltString, actualVel, actualAlt;

    private final Label deltaV, velLabelCopy, actualVelCopy, velLabel, compassSAS;

    //Controls Text
    private Label control_toggle_cam, control_rotate_cam, control_pan_cam, control_zoom_cam, control_throttle_up, control_cut_engines,
            control_throttle_down, control_next_cbody, control_prev_cbody, control_rotate_countcw, control_rotate_cw, control_full_engines,
            control_warp_up, control_warp_down, control_drop_warp, control_jump_to, control_click_to, control_toggle_menu, control_toggle_status,
            control_toggle_sas;

    private Label status_parent, status_parent_mass, status_semiA, status_semiB, status_peri, status_apoap, status_ecc,
            status_w, status_Eanom, status_Manom, status_Tanom, status_startAnom, status_tmax;


    Sprite compass;
    ShapeRenderer compassBackground;
    ShapeRenderer throttleIndicator;

    float dashX, dashY, cWidth, cHeight, dashW, dashH;


    public PlayStateHud(PlayState ps, SpriteBatch sb) {

        this.ps = ps;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,sb );


        controlX +=  ps.getScreenWidth();
        controlOrig += ps.getScreenWidth();


        //****
        //Top Label
        //****

        table = new Table();
        table.top();
        table.setFillParent(true);

        multLabel = new Label("WARP", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        multNumberLabel = new Label(String.format("%06d", mult), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        topFullAltString = new Label(String.format("%06d", 20), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        velLabel = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVel = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(multLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(velLabel ).expandX().padTop(10);
        table.row();
        table.add(multNumberLabel).expandX();
        table.add(topFullAltString).expandX();
        table.add(actualVel ).expandX();


        //****
        //Compass Label
        //****

        compassTable = new Table();
        compassTable.bottom();
        compassTable.setFillParent(true);

        //compassTable.moveBy(padCompassRight ,padCompassBotton);

        //compass dash stuff
        velLabelCopy = new Label("VELOCITY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualVelCopy = new Label(String.format("%.2f", ps.getKlobjects().get(0).getVel().distance(0,0)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        altLabel = new Label("ALTITUDE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        double alt = ps.getKlobjects().get(0).getLoc().minus(ps.getKlobjects().get(0).getParentBody().getLoc()).distance(0,0);
        actualAlt = new Label(String.format("%.2f",alt), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        deltaV = new Label("dV: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        compassTable.add(deltaV).padRight(15).padBottom(20);
        compassTable.row();
        compassTable.add(altLabel);
        compassTable.row();
        compassTable.add(actualAlt).padBottom(20);
        compassTable.row();
        compassTable.add(velLabelCopy);
        compassTable.row();
        compassTable.add(actualVelCopy );


        //****
        //Control Label
        //****

        controlTable = new Table();
        controlTable.bottom();
        controlTable.setFillParent(true);

        setControlLabels(controlTable);


        //****
        //Compass Label
        //****

        sasTable = new Table();
        sasTable.bottom().left();
        sasTable.setFillParent(true);

        compassSAS = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        sasTable.add(compassSAS).padBottom(160).padLeft(12);

        //****
        //Status Label
        //****

        statusTable = new Table();
        statusTable.bottom();
        statusTable.setFillParent(true);

        setStatusLabels(statusTable);

        stage.addActor(table);
        stage.addActor(compassTable);
        stage.addActor(controlTable);
        stage.addActor(statusTable);
        stage.addActor(sasTable);

        compassTable.moveBy(padCompassRight,padCompassBotton);


        initDash();


    }
    public void initDash(){
        compassBackground = new ShapeRenderer();
        throttleIndicator = new ShapeRenderer();

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
        Klobject k = ps.getKlobjects().get(0);


        multNumberLabel.setText(mult + "X");
        timeLabel.setText(new Date().toString().toUpperCase()+"");
        //useForSomethingBetter.setText(new Date().toString().toUpperCase()+"");
        compass.setRotation((float) (Math.toDegrees(k.getRotation()-Math.PI/2)+ps.getCamRotation()));
        double vel = k.getVel().distance(0,0);

        if (k.getSAS()){ compassSAS.setText("SAS"); }
        else { compassSAS.setText(""); }

        moveControl();
        moveStatus();
        statusSetText();

        //table/top
        actualVel.setText(String.format("%.2f",vel/1000) + " KM/S");
        //tcomp
        actualVelCopy.setText(String.format("%.2f",vel/1000) + " km/s");
        deltaV.setText("dV: "+String.format("%.2f",k.getDeltaV()) + " km/s");

        double p = k.getLoc().minus(k.getParentBody().getLoc()).distance(0,0);

        if (p >= 1000000){
            p = p /1000;
            actualAlt.setText(String.format("%.2f",p) + " KM");
            topFullAltString.setText("ALTITUDE: " + String.format("%.2f",p) + " KM");
        }
        else{
            actualAlt.setText(String.format("%.2f",p) + " M");
            topFullAltString.setText("ALTITUDE: " + String.format("%.2f",p) + " M");
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
        compassBackground.end();

        throttleIndicator.begin(ShapeRenderer.ShapeType.Filled);
        try {
            Klobject k = ps.getKlobjects().get(0);
            int side = 10;
            throttleIndicator.rect((dashW + dashX - ((float)(side/2.0))),((float)(dashY + (dashH-side)*(k.getThrottle()/k.getMaxThrottle()))), side,side);
            //throttleIndicator.circle((dashW + dashX ),((float)(dashY + (dashH)*(k.getThrottle()/k.getMaxThrottle()))),side);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
        }
        throttleIndicator.setColor(255f/255f, 131f/255f, 0f/255f, 1f);
        throttleIndicator.end();



        stage.draw();
        batch.begin();
        compass.draw(batch);
        batch.end();
    }

    public void toggleControl(){
        controlToggle = !controlToggle;
        controlStop = false;
    }

    public void toggleStatus(){
        statusToggle = !statusToggle;
        statusStop = false;
    }

    public void moveControl() {
        if (!controlToggle && !controlStop) {
            if (controlX < (controlOrig + controlExitStop)) {
                controlX += controlExitRate;
                controlTable.moveBy((float) controlExitRate, 0);
            } else {
                controlToggle = false;
                controlStop = true;
            }
        } else if (!controlStop) {
            if (controlX >= (controlOrig)) {
                controlX -= controlExitRate;
                controlTable.moveBy(-(float) controlExitRate, 0);
            } else {
                controlToggle = true;
                controlStop = true;
            }

        }

    }

    public void moveStatus() {
        if (!statusToggle && !statusStop) {
            if (statusX > (statusOrig + statusExitStop)) {
                statusX -= statusExitRate;
                statusTable.moveBy(-(float) statusExitRate, 0);
            } else {
                statusToggle = false;
                statusStop = true;
            }
        } else if (!statusStop) {
            if (statusX <= (statusOrig)) {
                statusX += statusExitRate;
                statusTable.moveBy((float) statusExitRate, 0);
            } else {
                statusToggle = true;
            }

        }

    }



    public void setMult(double mult) {
        this.mult = (int)mult;
    }
    public Camera getHCamera(){return viewport.getCamera();}


    public void statusSetText(){
        Klobject k = ps.getKlobjects().get(0);
        String firstLetter = k.getParentBody().getName().substring(0,1).toUpperCase();
        String name = firstLetter + k.getParentBody().getName().substring(1);
        status_parent.setText("Parent: " + name);
        status_parent_mass.setText(name + " Mass: " + String.format("%E",k.getParentBody().getMass()));
        status_semiA.setText("Semi Major Axis: " + String.format("%.4f",k.getSemiA()/1000.0f) + " km");
        status_semiB.setText("Semi Minor Axis: " + String.format("%.4f",k.getSemiB()/1000f) + " km");
        status_peri.setText("Periapsis: " + String.format("%.4f",k.getPeri()/1000f) + " km");
        status_apoap.setText("Apoapsis: " + String.format("%.4f",k.getApoap()/1000f) + " km");
        status_apoap.setText("Apoapsis: " + String.format("%.4f",k.getApoap()/1000f) + " km");
        status_ecc.setText("Eccentricity: " + String.format("%.4f",k.getEcc()));
        status_Tanom.setText("True Anomaly: " + String.format("%.4f",k.getTanom()));
        status_Eanom.setText("Eccentric Anomaly: " + String.format("%.4f",k.getEanom()));
        status_Manom.setText("Mean Anomaly: " + String.format("%.4f",k.getManom()));
    }

    public void setStatusLabels(Table statusTable){
        status_parent        = new Label("Parent: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_parent_mass   = new Label("Parent Mass: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_semiA         = new Label("Semi Major: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_semiB         = new Label("Semi Minor: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_peri          = new Label("Periapsis: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_apoap         = new Label("Apoapsis: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_ecc           = new Label("Eccentricity: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //status_w             = new Label("Pan Camera: RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_Tanom         = new Label("True Anomaly: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_Eanom         = new Label("Eccentric Anomaly: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        status_Manom         = new Label("Mean Anomaly: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //status_startAnom     = new Label("Pan Camera: RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //status_tmax          = new Label("Pan Camera: RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        statusTable.add(status_parent ).expandX().left().row();
        statusTable.add(status_parent_mass ).expandX().left().row();
        statusTable.add(status_semiA ).expandX().left().row();
        statusTable.add(status_semiB ).expandX().left().row();
        statusTable.add(status_peri ).expandX().left().row();
        statusTable.add(status_apoap ).expandX().left().row();
        statusTable.add(status_ecc ).expandX().left().row();
        //statusTable.add(status_w ).expandX().left().row();
        statusTable.add(status_Tanom ).expandX().left().row();
        statusTable.add(status_Eanom ).expandX().left().row();
        statusTable.add(status_Manom ).expandX().left().row();
        //statusTable.add(status_startAnom ).expandX().left().row();
        //statusTable.add(status_tmax ).expandX().left().row();

        statusTable.moveBy(statusX,statusY);




    }

    public void setControlLabels(Table controlTable){
        control_pan_cam        = new Label("Pan Camera: RMB", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_cam     = new Label("Rotate Camera: Alt + RMB\nRotate Camera: Arrow Keys", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_zoom_cam       = new Label("Zoom Camera: Scroll Wheel", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_throttle_up    = new Label("Throttle Up: Shift", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_throttle_down  = new Label("Throttle Down: Ctrl", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_full_engines    = new Label("Full Throttle: Z", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_cut_engines    = new Label("Cut Throttle: X", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_next_cbody     = new Label("Next C-Body:  +", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_prev_cbody     = new Label("Prev C-Cbody: -", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_countcw = new Label("Rotate CCW: Q", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_rotate_cw      = new Label("Rotate  CW: E", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_warp_up        = new Label("Warp Up: >", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_warp_down      = new Label("Warp Down: <", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_drop_warp      = new Label("Drop Out of Warp: /", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_jump_to        = new Label("Jump to Player: 1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_click_to       = new Label("Click any C-Body to Lock", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_cam     = new Label("Toggle Camera Lock: C", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_menu    = new Label("Toggle Control Menu: M", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_status    = new Label("Toggle Orbital Menu: O", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        control_toggle_sas    = new Label("Toggle Stability Assist: T", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        controlTable.add(control_pan_cam ).expandX().left().row();
        controlTable.add(control_rotate_cam).expandX().left().row();
        controlTable.add(control_zoom_cam).expandX().left().row();
        controlTable.add(control_toggle_cam).expandX().left().row();
        controlTable.add(control_rotate_countcw ).left().row();
        controlTable.add(control_rotate_cw ).expandX().left().row();
        controlTable.add(control_toggle_sas).expandX().left().row();
        controlTable.add(control_throttle_up).expandX().left().row();
        controlTable.add(control_throttle_down ).expandX().left().row();
        controlTable.add(control_full_engines ).expandX().left().row();
        controlTable.add(control_cut_engines ).expandX().left().row();
        controlTable.add(control_next_cbody ).expandX().left().row();
        controlTable.add(control_prev_cbody ).expandX().left().row();
        controlTable.add(control_jump_to ).expandX().left().row();
        controlTable.add(control_warp_up ).expandX().left().row();
        controlTable.add(control_warp_down ).expandX().left().row();
        controlTable.add(control_drop_warp ).expandX().left().row();
        controlTable.add(control_click_to ).expandX().left().row();
        controlTable.add(control_toggle_status).expandX().left().row();
        controlTable.add(control_toggle_menu).expandX().left().row();

        controlTable.moveBy(controlX,controlY);

    }


}