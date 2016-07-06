package net.themorfeus.srp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.game.world.World;
import net.themorfeus.srp.logic.ScreenWithUI;
import net.themorfeus.srp.render.DebugDisplay;
import net.themorfeus.srp.render.OrbitingCameraController;
import net.themorfeus.srp.render.models.ModelBatchManager;


public class GameScreen implements ScreenWithUI {

    private SuperRenderPass engineInstance;

    private Camera camera;
    private OrbitingCameraController cameraController;

    private DebugDisplay debugDisplay;

    /**
     * Stage for UI related things
     * */
    private Stage stage;

    private ModelBatch modelBatch;
    private ModelBatchManager modelBatchManager;


    private World w;
    private Environment environment;


    public GameScreen(SuperRenderPass engine){
        this.engineInstance = engine;

        setupCamera();
        setupRendering();
        setupInput();
        setupWorld();
    }

    private void setupWorld(){

        this.environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(.6f, .6f, .6f, -1f, -0.8f, -0.2f));

        this.w = new World(this, environment, modelBatchManager);
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
        cameraController.setMinimalPitch(0);
        cameraController.setMaximalPitch(0);
    }

    private void setupRendering(){
        stage = new Stage();
        stage.setViewport(new ScreenViewport());
        debugDisplay = new DebugDisplay(stage);

        modelBatch = new ModelBatch();
        modelBatchManager = new ModelBatchManager(modelBatch);
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

        w.render();

        modelBatchManager.flush(camera);
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
