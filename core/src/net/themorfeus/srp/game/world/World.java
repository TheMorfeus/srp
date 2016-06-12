package net.themorfeus.srp.game.world;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.Resources;
import net.themorfeus.srp.render.models.ModelBatchManager;

import java.util.ArrayList;

public class World {

    private SuperRenderPass gameInstance;

    private ModelBatchManager modelBatch;


    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public World(SuperRenderPass g, ModelBatchManager modelBatch){

        this.gameInstance = g;
        this.modelBatch = modelBatch;

        Model m = Resources.getInstance().manager.get(Resources.getInstance().TEST_MODEL);

        entities.add(new ModelEntity(gameInstance, new ModelInstance(m), modelBatch, 0, 0, 0));
    }

    public void render(){
        for(Entity e:entities){
            e.tick();
        }
    }
}
