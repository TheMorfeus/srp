package net.themorfeus.perspective.render.shaders;

import com.badlogic.gdx.math.Matrix4;

/**
 * Created by morf on 05.06.2016.
 */
public class PassthroughShader extends ShaderHelper{

    private int u_texture;
    private int u_projView;

    private Matrix4 projectionMatrix;

    public PassthroughShader() {
        super("passthrough");
    }

    @Override
    public void setup() {
        this.u_texture = program.getUniformLocation("u_texture");
        this.u_projView = program.getUniformLocation("u_projView");

        this.projectionMatrix = new Matrix4();
    }

    @Override
    public void begin() {
        program.begin();
        program.setUniformMatrix(u_projView, new Matrix4());
        program.setUniformi(u_texture, textureID);
    }
}
