package net.themorfeus.srp.render.shaders.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by morf on 12.06.2016.
 */
public class TestShader implements Shader {

    private ShaderProgram program;
    private Camera camera;
    private RenderContext context;

    @Override
    public void init () {

        String vert = Gdx.files.internal("shaders/model/test.v").readString();
        String frag = Gdx.files.internal("shaders/model/test.f").readString();

        this.program = new ShaderProgram(vert, frag);

        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
    }
    @Override
    public void dispose () {
        program.dispose();
    }
    @Override
    public void begin (Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();
        program.setUniformMatrix("u_projViewTrans", camera.combined);

        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }
    @Override
    public void render (Renderable renderable) {
        program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        program.setUniformi("u_texture", 0);

        TextureAttribute t = (TextureAttribute)renderable.material.get(TextureAttribute.Diffuse);
        if(t!=null)t.textureDescription.texture.bind(0);
        renderable.meshPart.render(program);

    }
    @Override
    public void end () {
        program.end();
    }
    @Override
    public int compareTo (Shader other) {
        return 0;
    }
    @Override
    public boolean canRender (Renderable instance) {
        return true;
    }
}