package net.themorfeus.srp.render.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 *  Shader helper for easier rendering, and less clutter in the rest of the code
 *  Shaders should be placed in running directory in "shaders" folder.
 *  They should have extensions of ".v" for vertex shader, and ".f" for fragment shader
 *  @author themorfeus
 * */
public abstract class ShaderHelper implements Disposable {

    /**
     * @see ShaderProgram.pedantic
     * */
    static{
        ShaderProgram.pedantic = false;
    }

    protected ShaderProgram program;

    /**
     *  Human readable shader name
     * */
    protected String name;

    /**
     *  Vertex shader name (without file extension)
     * */
    protected String vertName;

    /**
     *  Fragment shader name (without file extension)
     * */
    protected String fragName;

    /**
     * Texture ID which can be passed into sampler2D. Used for post-processing shaders.
     * */
    protected int textureID;

    /**
     * @param name Shader name. Will be used as human readable name, vertex and fragment shaders name.
     * */
    public ShaderHelper(String name){
        this(name, name, name);
    }
    /**
     * @param name Human readable name
     * @param vertName Vertex shader filename (without file extension)
     * @param fragName Fragment shader filename (without file extension)
     * */
    public ShaderHelper(String name, String vertName, String fragName){
        createNewProgram(name, vertName, fragName);
    }

    /**
     * Creates new instance of {@link ShaderProgram}
     * @param name Human readable name
     * @param vertName Vertex shader filename (without file extension)
     * @param fragName Fragment shader filename (without file extension)
     * */
    protected void createNewProgram(String name, String vertName, String fragName){

        this.name = name;

        this.vertName = vertName;
        this.fragName = fragName;

        String vert = Gdx.files.internal("shaders/" + vertName + ".v").readString();
        String frag = Gdx.files.internal("shaders/" + fragName + ".f").readString();

        this.program = new ShaderProgram(vert, frag);

        if(!program.isCompiled()){
            System.err.println(program.getLog());
        }else{
            System.out.println("Shader " + name + " compiled successfully.");
        }

    }

    /**
     * Sets the texture ID which can then be passed into the shader
     * */
    public void setTextureID(int id){
        this.textureID = id;
    }

    /**
     * Setup method. Uniform locations should be initialized here
     * */
    public abstract void setup();

    /**
     * Begin method. Called before drawing.
     * Its first line should be program.begin();
     * */
    public abstract void begin();

    /**
     * End method. Called after drawing.
     * If there are any errors or warinings in shaders, they're printed here.
     * */
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

    /**
     * @return Human-readable shader name
     * */
    public String getName(){
        return name;
    }

    public void dispose(){
        program.dispose();
    }
}
