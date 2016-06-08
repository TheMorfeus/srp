package net.themorfeus.perspective.render.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by morf on 05.06.2016.
 */
public class FXAAShader extends ShaderHelper{

    private int u_fxaa_span_max;
    private int u_fxaa_reduce_mul;
    private int u_fxaa_reduce_min;
    private int u_viewportInverse;
    private int u_texture;
    private int u_projView;

    private Matrix4 projectionMatrix;

    private float antialiasingFactor = 4;

    public FXAAShader() {
        super("fxaa", "passthrough", "fxaa");
    }

    @Override
    public void setup() {
        this.u_fxaa_span_max = program.getUniformLocation("FXAA_SPAN_MAX");
        this.u_fxaa_reduce_mul = program.getUniformLocation("FXAA_REDUCE_MUL");
        this.u_fxaa_reduce_min = program.getUniformLocation("FXAA_REDUCE_MIN");
        this.u_viewportInverse = program.getUniformLocation("u_viewportInverse");
        this.u_texture = program.getUniformLocation("u_texture");
        this.u_projView = program.getUniformLocation("u_projView");

        this.projectionMatrix = new Matrix4();
    }

    public float getAntialiasingFactor(){
        return antialiasingFactor;
    }

    public void setAntialiasingFactor(float factor){
        this.antialiasingFactor = factor*2;

        if(this.antialiasingFactor<5f)this.antialiasingFactor = 5f;
    }

    @Override
    public void begin() {
        program.begin();

        program.setUniformMatrix(u_projView, projectionMatrix);

        program.setUniformi(u_texture, textureID);

        program.setUniformf(u_viewportInverse, 1f / Gdx.graphics.getWidth(), 1f / Gdx.graphics.getHeight());
        program.setUniformf(u_fxaa_reduce_min, (float) (1f / (Math.pow(antialiasingFactor, 8))));
        program.setUniformf(u_fxaa_reduce_mul, 1f / antialiasingFactor);
        program.setUniformf(u_fxaa_span_max, antialiasingFactor);
    }

}
