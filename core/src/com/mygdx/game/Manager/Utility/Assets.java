package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

public class Assets {
    public AssetManager manager = new AssetManager();

//    public static final AssetDescriptor<TextureAtlas> klobjectAtlas =
//            new AssetDescriptor<TextureAtlas>("spaceship.txt", TextureAtlas.class);

//    public static final AssetDescriptor<TextureAtlas> cBTextureAtlas =
//            new AssetDescriptor<TextureAtlas>("sprites.txt", TextureAtlas.class);

    public static final AssetDescriptor<Texture> sun =
            new AssetDescriptor<Texture>("planets/sun.png", Texture.class);

    public static final AssetDescriptor<Texture> kerbin =
        new AssetDescriptor<Texture>("planets/kerbin.png", Texture.class);

    public static final AssetDescriptor<Texture> nars =
            new AssetDescriptor<Texture>("planets/nars.png", Texture.class);

    public static final AssetDescriptor<Texture> mun =
        new AssetDescriptor<Texture>("planets/mun.png", Texture.class);

    public static final AssetDescriptor<Texture> minmus =
        new AssetDescriptor<Texture>("planets/mun.png", Texture.class);

    public static final AssetDescriptor<Texture> spaceship =
        new AssetDescriptor<Texture>("klobjects/spaceship.png", Texture.class);


    static {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        int c = (int)(Math.sqrt(Math.pow(width,2)+Math.pow(height,2))+1);
        Pixmap bg = new Pixmap(c,c,Pixmap.Format.RGBA8888);
        for (int rc = 0; rc < c; rc++) {
            for (int cc = 0; cc < c; cc++) {
                // Set the pixel colour of the image n.b. x = cc, y = rc
                int i = new Random().nextInt(10000);
                if (i <= 1) {
                    bg.setColor(Color.WHITE);
                    //background.drawPixel(cc,rc,Color.WHITE.toIntBits());
                    bg.fillCircle(cc, rc, 1);
                }
                //else {   //background.drawPixel(cc,rc,Color.BLACK.toIntBits());    }
            }
        }
        try {
            FileHandle outputfile = new FileHandle("res/background.png");
            PixmapIO.writePNG(outputfile,bg);
        } catch (Exception e) {
            System.out.println();
        }

        bg.dispose();
    }


    public static final AssetDescriptor<Texture> background =
            new AssetDescriptor<Texture>("res/background.png", Texture.class);


    public void load()
    {

        //manager.load(klobjectAtlas);
        //manager.load(cBTextureAtlas);
        manager.load(background);
        manager.load(sun);
        manager.load(kerbin);
        manager.load(mun);
        manager.load(minmus);
        manager.load(spaceship);
        manager.load(nars);

    }

    public void dispose()
    {
        manager.dispose();
    }


}