package net.themorfeus.perspective.render.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by morf on 05.06.2016.
 */
public abstract class ShaderHelper implements Disposable {

    protected String name;

    protected String vertName, fragName;

    protected ShaderProgram program;

    /**
     * Texture ID which should be passed into sampler2D
     * */
    protected int textureID;

    public ShaderHelper(String name){
        this(name, name, name);
    }

    public ShaderHelper(String name, String vertName, String fragName){
        createNewProgram(name, vertName, fragName);
    }

    protected void createNewProgram(String name, String vertName, String fragName){

        this.name = name;

        this.vertName = vertName;
        this.fragName = fragName;

        String vert = Gdx.files.internal("shaders/" + vertName + ".v").readString();
        String frag = Gdx.files.internal("shaders/" + fragName + ".f").readString();

        this.program = new ShaderProgram(vert, frag);
        this.program.pedantic = true;

        if(!program.isCompiled()){
            System.err.println(program.getLog());
        }else{
            System.out.println("Shader " + name + " compiled successfully.");
        }

    }

    public void setTextureID(int id){
        this.textureID = id;
    }

    public abstract void setup();

    public abstract void begin();

    public void end(){

        if(!program.getLog().trim().equals("")){
            System.err.println(program.getLog());
            if(program.getLog().contains("error"))throw new RuntimeException("Error in shader " + name + "! Aborting...");
        }

        program.end();
    }

    public ShaderProgram getProgram(){
        return program;
    }

    public String getName(){
        return name;
    }

    public void dispose(){
        program.dispose();
    }
}
