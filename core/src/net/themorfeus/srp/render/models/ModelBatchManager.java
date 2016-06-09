package net.themorfeus.srp.render.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.*;

import java.util.Stack;


/**
 * Used for easier managing of ModelBatch.
 * The models can be added outside of ModelBatch render loop, just as in {@link com.badlogic.gdx.graphics.g3d.decals.DecalBatch}
 */
public class ModelBatchManager {

    private ModelBatch modelBatch;
    private Stack<RenderableInfo> renderStack;

    public ModelBatchManager(ModelBatch b){
        this.modelBatch = b;
        renderStack = new Stack<RenderableInfo>();
    }

    public void addRenderable(Renderable renderable){
        RenderableInfo info = new RenderableInfo();

        info.renderable = renderable;

        renderStack.push(info);
    }

    public void addProvider(RenderableProvider provider, Environment environment, Shader shader){
        RenderableInfo info = new RenderableInfo();

        info.provider = provider;
        info.environment = environment;
        info.shader = shader;

        renderStack.push(info);
    }

    public void flush(Camera c){
        modelBatch.begin(c);
        while(!renderStack.isEmpty()) {

            RenderableInfo info = renderStack.pop();
            if(info.provider!=null){
                modelBatch.render(info.provider, info.environment, info.shader);
            }else{
                modelBatch.render(info.renderable);
            }

        }
        modelBatch.flush();
        modelBatch.end();
    }

    private class RenderableInfo{
        Renderable renderable;

        RenderableProvider provider;
        Environment environment;
        Shader shader;
    }
}
