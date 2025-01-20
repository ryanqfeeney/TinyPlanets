package com.mygdx.game.Manager.Utility.RenderManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.badlogic.gdx.graphics.Color;

public class KlobjectRenderManager implements Disposable {
    private static KlobjectRenderManager instance = new KlobjectRenderManager();
    public final ShapeRenderer pathRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch;
    private boolean isPathRendering = false;

    private KlobjectRenderManager() {}

    public static KlobjectRenderManager get() {
        return instance;
    }

    public void beginPath(PlayState ps) {
        if (!isPathRendering) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            pathRenderer.setProjectionMatrix(ps.getCamera().combined);
            pathRenderer.begin(ShapeRenderer.ShapeType.Line);
            isPathRendering = true;
        }
    }

    public void endPath() {
        if (isPathRendering) {
            pathRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            isPathRendering = false;
        }
    }

    public void drawSprite(PlayState ps, Sprite sprite, float x, float y) {
        if (spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }
        
        spriteBatch.setProjectionMatrix(ps.getCamera().combined);
        spriteBatch.begin();
        sprite.setPosition(
            (float)(x - ps.getCamX()),
            (float)(y - ps.getCamY())
        );
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public void drawPath(float[] verts, Color color, double fade, float minFade) {
        if (!isPathRendering) return;
        pathRenderer.setColor(color.r, color.g, color.b, (float)fade);
        pathRenderer.polyline(verts, minFade, (float) fade);
    }

    @Override
    public void dispose() {
        if (isPathRendering) endPath();
        pathRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
    }
}