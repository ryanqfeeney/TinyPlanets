package com.mygdx.game.Manager.Utility.RenderManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Manager.GameStates.PlayState;
import com.badlogic.gdx.graphics.Color;

public class CbodyRenderManager implements Disposable {
    private static CbodyRenderManager instance = new CbodyRenderManager();
    public final ShapeRenderer pathRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch;
    private boolean isPathRendering = false;
    private ShapeRenderer circleRenderer = new ShapeRenderer();

    private CbodyRenderManager() {}

    public static CbodyRenderManager get() {
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

    public void drawPath(float[] verts, Color color, float fade) {
        if (!isPathRendering) return;
        pathRenderer.setColor(color.r, color.g, color.b, fade);
        pathRenderer.polyline(verts);
    }

    public void drawCircle(PlayState ps, float x, float y, float size, Color color, float alpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        circleRenderer.setProjectionMatrix(ps.getCamera().combined);
        circleRenderer.begin(ShapeRenderer.ShapeType.Filled);
        circleRenderer.setColor(color.r, color.g, color.b, alpha);
        circleRenderer.circle(x, y, size);
        circleRenderer.end();
    }

    @Override
    public void dispose() {
        if (isPathRendering) endPath();
        pathRenderer.dispose();
        circleRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
    }
} 