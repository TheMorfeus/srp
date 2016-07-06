package net.themorfeus.srp.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.math.Vector3;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.game.GameScreen;
import net.themorfeus.srp.render.models.ModelBatchManager;
import net.themorfeus.srp.render.shaders.models.TestShader;

/**
 * Created by morf on 11.06.2016.
 */
public class ModelEntity extends Entity{

    private ModelInstance model;
    private ModelBatchManager modelBatch;

    private Environment environment;

    private Shader shader;


    public ModelEntity(GameScreen g, Environment environment, ModelInstance m, ModelBatchManager modelBatch, float x, float y, float z) {
        super(g);
        this.model = m;
        this.modelBatch = modelBatch;
        this.position.set(x,y,z);

        this.environment = environment;

        model.transform.scale(.2f, .2f, .2f);

        String vert = Gdx.files.internal("shaders/model/test.v").readString();
        String frag = Gdx.files.internal("shaders/model/test.f").readString();

        this.shader = new TestShader();
        this.shader.init();
    }

    @Override
    public void tick() {

        model.transform.setTranslation(position);
        model.calculateTransforms();

        modelBatch.add(model, null, shader);

    }
}
