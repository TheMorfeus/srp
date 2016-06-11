package net.themorfeus.srp;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;

/**
 * This class should contain resources of the game.
 */
public class Resources{

    //technically this shoudln't be static. But it should be managed well enough in MainGame class, that SRP can get away with it
    public static AssetManager manager;

    public static final String TILESET = "tileset.png";

    public static final String SHITWADDUP = "img/shitwaddup.png";
    public static final String OBAMA  = "img/test.png";
    public static final String CHARIZARD  = "img/char.png";
    public static final String HIPPO  = "img/hippo.png";
    public static final String PANDA  = "img/panda.png";
    public static final String PENGUIN  = "img/penguin.png";
    public static final String RABBIT  = "img/rabbit.png";
    public static final String GIRAFFE  = "img/giraffe.png";
    public static final String MONKEY  = "img/monkey.png";
    public static final String PARROT  = "img/parrot.png";
    public static final String PIG  = "img/pig.png";
    public static final String SNAKE = "img/snake.png";

    public static final String TEST_MODEL = "mdl/test.obj";

    public static void load(){
        if(manager!=null)manager.dispose();
        manager = new AssetManager();

        manager.load(TILESET, Texture.class);

        manager.load(SHITWADDUP, Texture.class);
        manager.load(CHARIZARD, Texture.class);
        manager.load(OBAMA, Texture.class);
        manager.load(HIPPO, Texture.class);
        manager.load(PANDA, Texture.class);
        manager.load(PENGUIN, Texture.class);
        manager.load(RABBIT, Texture.class);
        manager.load(GIRAFFE, Texture.class);
        manager.load(MONKEY, Texture.class);
        manager.load(PARROT, Texture.class);
        manager.load(PIG, Texture.class);
        manager.load(SNAKE, Texture.class);

        manager.load(TEST_MODEL, Model.class);
    }

    public static boolean isLoaded(){
        return manager.getProgress()>=1;
    }

    public static void dispose(){
        manager.dispose();
        manager = null;
    }

}
