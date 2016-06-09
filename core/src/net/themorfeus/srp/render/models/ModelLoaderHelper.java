package net.themorfeus.srp.render.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;

import java.util.HashMap;

/**
 * Helper class for loading and managing models
 */
public class ModelLoaderHelper {

    private HashMap<String, Model> models;
    private ModelLoader loader;

    public ModelLoaderHelper(){
        models = new HashMap<String, Model>();
    }

    public void load(String path){
        models.put(path, loader.loadModel(Gdx.files.internal(path)));
    }

    public Model getModel(String path){
        if(!models.containsKey(path)){
            load(path);
        }

        return models.get(path);
    }
}
