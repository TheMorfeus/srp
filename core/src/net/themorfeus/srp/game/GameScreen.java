package net.themorfeus.srp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.logic.ScreenWithUI;
import net.themorfeus.srp.render.DebugDisplay;
import net.themorfeus.srp.render.OrbitingCameraController;


public class GameScreen implements ScreenWithUI {

    private SuperRenderPass engineInstance;

    private Camera camera;
    private OrbitingCameraController cameraController;

    private DebugDisplay debugDisplay;

    /**
     * Stage for UI related things
     * */
    private Stage stage;


    public GameScreen(SuperRenderPass engine){
        this.engineInstance = engine;

        setupCamera();
        setupRendering();
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

    private void setupRendering(){
        stage = new Stage();
        stage.setViewport(new ScreenViewport());
        debugDisplay = new DebugDisplay(stage);
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
     * Various debug functions
     * */
    private void debug(){
        Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + " FPS, anim quality: " + ((int) Math.floor(cameraController.animSmoothing)));
        debugDisplay.update("fps: " + Gdx.graphics.getFramesPerSecond() +
                "\nvsync: " + (engineInstance.isVsyncEnabled() ?"on":"off") +
                "\nfxaa: " + (engineInstance.isFxaaEnabled()?"on":"off") +
                "\ngpu: " + Gdx.gl.glGetString(GL20.GL_RENDERER));

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            engineInstance.setFxaaEnabled(!engineInstance.isFxaaEnabled());
        }
    }

    @Override
    public void show() {
        setupInput();
    }

    @Override
    public void render(float delta) {

        cameraController.update();
        camera.update();

        debug();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        engineInstance.glClearHexColor("#5253bc");
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderUI() {
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width * .02f;
        camera.viewportHeight = height* .02f;
        camera.update();

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
}
