package net.themorfeus.srp;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.themorfeus.srp.game.WorldScreen;
import net.themorfeus.srp.logic.LoadingScreen;
import net.themorfeus.srp.render.DebugDisplay;
import net.themorfeus.srp.render.FrameBufferManager;
import net.themorfeus.srp.render.OrbitingCameraController;
import net.themorfeus.srp.render.shaders.*;

/**
 * Main engine class
 * */
public class MainGame extends Game {

    /**
     * Temporary vector used for various calculations
     * */
    private Vector3 tempV3 = new Vector3();

    private Camera camera;
    private OrbitingCameraController cameraController;

    /**
     * Stage for UI related things
     * */
    private Stage stage;

    /**
     * Post-processing FrameBuffer
     * */
    private FrameBuffer screenFrameBuffer;

    /**
     * Multi-pass rendering FrameBuffer
     * */
    private FrameBuffer multipassFrameBuffer;

    /**
     * Post-processing screen mesh
     * */
    private Mesh screenQuad;

    /**
     * Shaders
     * */
    private FXAAShader fxaaShader;
    private PassthroughShader passthroughShader;
    private HBlurShader hBlurShader;
    private VBlurShader vBlurShader;

    private boolean fullscreen;
    private boolean vsync;

    /**
     * Debug variables
     * */
    private DebugDisplay debugDisplay;
    private boolean fxaa = true;

    /**
     * Main game screen
     * */
    private Screen gameScreen;

    @Override
    public void create () {
        setupAssets();
        setupCamera();
        setupRendering();
        setupPostProcessing();
        setupInput();
        setupScreen();
    }

    public void setupAssets(){
        Resources.getInstance().load();
    }

    /**
     * Sets up camera and its settings.
     * */
    private void setupCamera(){
        //camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = -20f;
        camera.far = 700f;
        camera.update();

        cameraController = new OrbitingCameraController(camera);
        cameraController.setAllowYawMovement(true);
        cameraController.setAllowPitchMovement(true);
    }

    /**
     * Sets up rendering components - batches, and all alike;
     * */
    private void setupRendering(){
        stage = new Stage();

        stage.setViewport(new ScreenViewport());

        debugDisplay = new DebugDisplay(stage);
    }

    /**
     * Sets up post processing projection matrix, frame buffer, screen mesh, shader and its uniforms.
     * */
    private void setupPostProcessing(){
        screenFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        multipassFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.screenQuad = Util.createFullScreenQuad();

        passthroughShader = new PassthroughShader();
        fxaaShader = new FXAAShader();
        hBlurShader = new HBlurShader();
        vBlurShader = new VBlurShader();

        passthroughShader.setup();
        fxaaShader.setup();

        hBlurShader.setup();
        vBlurShader.setup();
    }

    /**
     * Sets up input
     * */
    private void setupInput(){
        InputMultiplexer multiplexer = new InputMultiplexer();

        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(cameraController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Sets up the game screen
     * */
    private void setupScreen(){
        gameScreen = new LoadingScreen(this);
        this.setScreen(gameScreen);

        System.out.println(this.getScreen());

        /*new WorldScreen(this);
        this.setScreen(gameScreen);*/
    }

    /**
     * Called by LoadingScreen when resources have been loaded.
     * */
    public void resourcesLoaded(){
        //Setting to null, so that it doesn't throw error on hide() as we will dispose of the current screen.
        this.setScreen(null);
        //Loading screen is visible only once, so we can dispose of it now
        gameScreen.dispose();

        this.gameScreen = new WorldScreen(this);
        this.setScreen(gameScreen);
    }

    /**
     * Standard libgdx's render method.
     * Also the main game loop
     * */
    @Override
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        tempV3.set(0, 0, 0);

        debug();
        handleInput();

        cameraController.update();
        camera.update();

        //fxaaShader.setAntialiasingFactor(cameraController.getOrbitRadius() / 13.75f);

        /**
         * Rendering the game screen
         * */
        renderGameToFrameBuffer(screenFrameBuffer);
        renderFrameBufferUsingShader(screenFrameBuffer, fxaa ? fxaaShader : null, hBlurShader, vBlurShader);

        /**
         * Rendering the UI
         * */
        renderUIToFrameBuffer(screenFrameBuffer);
        renderFrameBufferUsingShader(screenFrameBuffer);
    }

    /**
     * Renders world
     * */
    private void renderGame(){
        super.render();
    }

    /**
     * Renders world to the given framebuffer
     * */
    private void renderGameToFrameBuffer(FrameBuffer fbo){
        FrameBufferManager.begin(fbo);{
            renderGame();
        }FrameBufferManager.end();
    }

    /**
     * Renders UI
     * */
    private void renderUI(){
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    /**
     * Renders UI to the given framebuffer
     * */
    private void renderUIToFrameBuffer(FrameBuffer fbo){
        FrameBufferManager.begin(fbo);{
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl20.glClearColor(0, 0, 0, 0);
            renderUI();
        }FrameBufferManager.end();
    }

    /**
     * Renders the given framebuffer using the given post-processing shader
     * */
    private void renderFrameBufferUsingShader(FrameBuffer fbo, ShaderHelper... shader) {
        if(shader == null || shader.length == 0){

            renderTextureUsingShader(fbo.getColorBufferTexture(), passthroughShader);

        }else if(shader.length==1){

            renderTextureUsingShader(fbo.getColorBufferTexture(), shader[0]);

        }else{

            Texture previousPass = null;
            for(int i = 0; i<shader.length-1; i++){
                if(shader[i]==null)continue;
                if(previousPass==null){
                    previousPass = renderShaderedTextureToTexture(fbo.getColorBufferTexture(), shader[i]);
                }else{
                    previousPass = renderShaderedTextureToTexture(previousPass, shader[i]);
                }
            }

            renderTextureUsingShader(previousPass, shader[shader.length-1]);

        }
    }

    /**
     * Renders the given texture using the given post-processing shader
     * */
    private void renderTextureUsingShader(Texture t, ShaderHelper shader){
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        t.bind(0);
        shader.setTextureID(0);

        shader.begin();{
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            screenQuad.render(shader.getProgram(), GL20.GL_TRIANGLE_FAN);
        }shader.end();
    }

    /**
     * Renders the given texture using the given post-processing shader to a texture.
     * Can be used for multipass rendering
     * */
    private Texture renderShaderedTextureToTexture(Texture t, ShaderHelper shader){
        FrameBufferManager.begin(multipassFrameBuffer);
        renderTextureUsingShader(t, shader);
        FrameBufferManager.end();

        return multipassFrameBuffer.getColorBufferTexture();
    }

    /**
     * Handles miscellaneous input
     * */
    private void handleInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            cameraController.animSmoothing--;
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            cameraController.animSmoothing++;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            fullscreen = !fullscreen;
            setFullscreen(fullscreen);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }

    /**
     * Sets the application to fullscreen or windowed mode
     * @param fs wheter the application should be fullscreen or not
     * */
    private void setFullscreen(boolean fs){
        if(fs){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }else{
            Gdx.graphics.setWindowedMode((Gdx.graphics.getDisplayMode().width * 2) / 3, (Gdx.graphics.getDisplayMode().height * 2) / 3);
        }

        this.fullscreen = fs;

        setVSync(fs);
    }

    /**
     * Sets wheter the application should be vertically synchronized or not
     * @param vs wheter the application should be vertically synchronized or not
     * */
    private void setVSync(boolean vs){
        this.vsync = vs;
        Gdx.graphics.setVSync(vsync);
    }

    /**
     * Various debug functions
     * */
    private void debug(){
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " FPS, anim quality: " + ((int) Math.floor(cameraController.animSmoothing)));
        debugDisplay.update("fps: " + Gdx.graphics.getFramesPerSecond() +
                "\nvsync: " + (vsync ?"on":"off") +
                "\nfxaa: " + (fxaa?"on":"off") +
                "\ngpu: " + Gdx.gl.glGetString(GL20.GL_RENDERER));

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            fxaa = !fxaa;
        }
    }

    /**
     * Sets the clear color
     * @param hex Color represented as a hex string. May or may not start with a hash key
     * */
    public void glClearHexColor(String hex){
        float[] colors = Util.getColorFromHex(hex);
        Gdx.gl.glClearColor(colors[0], colors[1], colors[2], 1);
    }

    /**
     * @return current camera
     * */
    public Camera getCamera(){
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    public OrbitingCameraController getCameraController(){
        return cameraController;
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        screenFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        multipassFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camera.viewportWidth = width * .02f;
        camera.viewportHeight = height* .02f;
        camera.update();

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause(){
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose(){
        super.dispose();

        screenFrameBuffer.dispose();
        multipassFrameBuffer.dispose();
        stage.dispose();
        screenQuad.dispose();
        fxaaShader.dispose();
        passthroughShader.dispose();
        hBlurShader.dispose();
        vBlurShader.dispose();
        Util.clearFonts();

        Resources.getInstance().dispose();
    }
}
