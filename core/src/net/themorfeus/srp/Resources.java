package net.themorfeus.srp;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class should contain resources of the game.
 */
public class Resources{

    public static AssetManager manager;

    public static final String TILESET_FILE = "tileset.png";

    public static void load(){
        if(manager!=null)manager.dispose();
        manager = new AssetManager();

        manager.load(TILESET_FILE, Texture.class);
    }

    public boolean isLoaded(){
        return manager.getProgress()<1;
    }

    public static void dispose(){
        manager.dispose();
        manager = null;
    }

}
