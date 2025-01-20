package com.mygdx.game.Manager.Utility.RenderManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Manager.Utility.Colors;

public class PlayStateHudRenderManager implements Disposable {
    private static PlayStateHudRenderManager instance = new PlayStateHudRenderManager();
    
    private ShapeRenderer gridRenderer;
    private ShapeRenderer compassBackgroundRenderer;
    private ShapeRenderer tickRenderer;
    private ShapeRenderer throttleRenderer;
    private SpriteBatch compassBatch;
    private Sprite compassSprite;
    
    private boolean isGridRendering = false;

    private PlayStateHudRenderManager() {}

    public static PlayStateHudRenderManager get() {
        return instance;
    }

    public void beginGrid(Camera camera) {
        if (!isGridRendering) {
            if (gridRenderer == null) {
                gridRenderer = new ShapeRenderer();
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            gridRenderer.setProjectionMatrix(camera.combined);
            gridRenderer.begin(ShapeRenderer.ShapeType.Filled);
            isGridRendering = true;
        }
    }

    public void drawGrid(float x, float y, float width, float height, float spacing, float lineThickness, Color color) {
        if (!isGridRendering) return;
        
        gridRenderer.setColor(color);
        
        // Draw horizontal lines
        for(float yPos = y; yPos <= y + height; yPos += spacing) {
            gridRenderer.rectLine(x, yPos, x + width, yPos, lineThickness);
        }
        
        // Draw vertical lines
        for(float xPos = x; xPos <= x + width; xPos += spacing) {
            gridRenderer.rectLine(xPos, y, xPos, y + height, lineThickness);
        }
    }

    public void endGrid() {
        if (isGridRendering) {
            gridRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            isGridRendering = false;
        }
    }

    public void beginThrottle(Camera camera) {
        if (throttleRenderer == null) {
            throttleRenderer = new ShapeRenderer();
        }
        throttleRenderer.setProjectionMatrix(camera.combined);
        throttleRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawThrottle(float x, float y, float width, float height, Color color) {
        throttleRenderer.setColor(color);
        throttleRenderer.rect(x, y, width, height);
    }

    public void endThrottle() {
        throttleRenderer.end();
    }

    public void beginTicks(Camera camera) {
        if (tickRenderer == null) {
            tickRenderer = new ShapeRenderer();
        }
        tickRenderer.setProjectionMatrix(camera.combined);
        tickRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawTicks(float dashX, float dashY, float dashW, float dashH, 
                         int numTicks, float tickWidth, float tickHeight, Color color) {
        tickRenderer.setColor(color);
        for(int i = 1; i < numTicks; i++) {
            float yPos = dashY + (dashH * (i / (float)numTicks));
            tickRenderer.rect(dashW + dashX - tickWidth, yPos - tickHeight/2, tickWidth, tickHeight);
        }
    }

    public void endTicks() {
        tickRenderer.end();
    }

    public void beginCompassBackground(Camera camera) {
        if (compassBackgroundRenderer == null) {
            compassBackgroundRenderer = new ShapeRenderer();
        }
        compassBackgroundRenderer.setProjectionMatrix(camera.combined);
        compassBackgroundRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawCompassBackground(float dashX, float dashY, float dashW, float dashH, float borderThickness) {
        compassBackgroundRenderer.setColor(Colors.COMPASS_BORDER);
        compassBackgroundRenderer.rect(
            dashX - borderThickness, 
            dashY - borderThickness,
            dashW + (2 * borderThickness),
            dashH + (2 * borderThickness)
        );
        
        compassBackgroundRenderer.setColor(Colors.COMPASS_BACKGROUND);
        compassBackgroundRenderer.rect(dashX, dashY, dashW, dashH);
    }

    public void endCompassBackground() {
        compassBackgroundRenderer.end();
    }

    public void drawCompass(Camera camera, Texture texture, float dashX, float dashY, float dashH, float rotation) {
        if (compassBatch == null) {
            compassBatch = new SpriteBatch();
        }
        
        if (compassSprite == null) {
            compassSprite = new Sprite(texture);
        }

        float height = dashH * 0.8f;
        float width = height * (texture.getWidth() / (float)texture.getHeight());
        
        compassSprite.setSize(width, height);
        compassSprite.setOrigin(width / 2, height / 2);
        compassSprite.setRotation(-rotation );
        compassSprite.setPosition(
            dashX + dashH/2 - width/2,
            dashY + dashH/2 - height/2
        );

        compassBatch.setProjectionMatrix(camera.combined);
        compassBatch.begin();
        compassSprite.draw(compassBatch);
        compassBatch.end();
    }

    @Override
    public void dispose() {
        if (gridRenderer != null) gridRenderer.dispose();
        if (compassBackgroundRenderer != null) compassBackgroundRenderer.dispose();
        if (tickRenderer != null) tickRenderer.dispose();
        if (throttleRenderer != null) throttleRenderer.dispose();
        if (compassBatch != null) compassBatch.dispose();
    }
}