package net.themorfeus.srp.game.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.render.models.ModelBatchManager;

/**
 * Created by morf on 11.06.2016.
 */
public class ModelEntity extends Entity{

    private ModelInstance model;
    private ModelBatchManager modelBatch;

    private Environment environment;


    public ModelEntity(SuperRenderPass g, ModelInstance m, ModelBatchManager modelBatch, float x, float y, float z) {
        super(g);
        this.model = m;
        this.modelBatch = modelBatch;
        this.position.set(x,y,z);

        this.environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(.6f, .6f, .6f, -1f, -0.8f, -0.2f));

        model.transform.scale(.2f, .2f, .2f);
    }

    @Override
    public void tick() {

        model.transform.setTranslation(position);
        model.calculateTransforms();

        modelBatch.add(model, environment, null);

    }
}
