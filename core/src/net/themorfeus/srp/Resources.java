package net.themorfeus.srp;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;

/**
 * This class should contain resources of the game.
 */
public class Resources{

    private static Resources instance;

    //technically this shoudln't be . But it should be managed well enough in SuperRenderPass class, that SRP can get away with it
    public AssetManager manager;

    public final String TILESET = "tileset.png";

    public final String SHITWADDUP = "img/shitwaddup.png";
    public final String OBAMA  = "img/test.png";
    public final String CHARIZARD  = "img/char.png";
    public final String HIPPO  = "img/hippo.png";
    public final String PANDA  = "img/panda.png";
    public final String PENGUIN  = "img/penguin.png";
    public final String RABBIT  = "img/rabbit.png";
    public final String GIRAFFE  = "img/giraffe.png";
    public final String MONKEY  = "img/monkey.png";
    public final String PARROT  = "img/parrot.png";
    public final String PIG  = "img/pig.png";
    public final String SNAKE = "img/snake.png";

    public final String TEST_MODEL = "mdl/test.obj";

    public void load(){
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

    public boolean isLoaded(){
        return manager.getProgress()>=1;
    }

    public void dispose(){
        manager.dispose();
        manager = null;
    }

    public static Resources getInstance(){
        if(instance == null)instance = new Resources();
        return instance;
    }

}
