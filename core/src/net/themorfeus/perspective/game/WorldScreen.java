package net.themorfeus.perspective.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import net.themorfeus.perspective.MainGame;
import net.themorfeus.perspective.render.NoBleedingCameraGroupStrategy;
import net.themorfeus.perspective.world.World;

/**
 * Created by morf on 07.06.2016.
 */
public class WorldScreen implements Screen{

    private MainGame gameInstance;

    /**
     * Main game world
     * */
    private World world;

    /**
     * Main decal batch (for entities and such)
     * */
    private DecalBatch decalBatch;

    /**
     * World ground batch
     * */
    private DecalBatch groundBatch;

    /**
     * Group strategy for displaying decals
     * */
    private GroupStrategy decalStrategy;


    public WorldScreen(MainGame g){
        this.gameInstance = g;
        this.world = new World(gameInstance, 50, 50);
        gameInstance.getCameraController().setPivotPoint(world.getWidth()/2f, 0, world.getHeight()/2f);
        setupRendering();
    }

    /**
     * Sets up rendering components - batches, and all alike;
     * */
    private void setupRendering(){
        decalStrategy = new NoBleedingCameraGroupStrategy(gameInstance.getCamera());
        decalBatch = new DecalBatch(decalStrategy);
        groundBatch = new DecalBatch(new CameraGroupStrategy(gameInstance.getCamera()));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameInstance.glClearHexColor("#5253bc");
        //gameInstance.glClearHexColor("#bca958");
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        world.render(groundBatch, decalBatch);

        groundBatch.flush();
        decalBatch.flush();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
