package net.themorfeus.srp.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.themorfeus.srp.SuperRenderPass;
import net.themorfeus.srp.Resources;
import net.themorfeus.srp.Util;
import net.themorfeus.srp.render.DebugDisplay;

/**
 * This is displayed when the game is loading
 */
public class LoadingScreen extends ScreenWithUIAdapter{

    private SuperRenderPass engineInstance;

    private Label.LabelStyle labelStyle;
    private Label loadingLabel;

    /**
     * Stage for UI related things
     * */
    private Stage stage;

    public LoadingScreen(SuperRenderPass engine){
        this.engineInstance = engine;

        this.labelStyle = new Label.LabelStyle();
        labelStyle.font = Util.getFont(20);
        labelStyle.fontColor = Color.WHITE;
        loadingLabel = new Label("Loading: xxx%", labelStyle);

        stage = new Stage();
        stage.setViewport(new ScreenViewport());

        stage.addActor(loadingLabel);
    }

    public void render(float delta){
        Resources.getInstance().manager.update();

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        loadingLabel.setText("Loading: " + (int)(Resources.getInstance().manager.getProgress() * 100) + "%");
        loadingLabel.setPosition(stage.getWidth()/2 - loadingLabel.getWidth()/2, stage.getHeight()/2 - loadingLabel.getHeight()/2);

        if(Resources.getInstance().isLoaded()){
            engineInstance.resourcesLoaded();
            loadingLabel.remove();
        }

    }

    public void renderUI(){
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

}
