package net.themorfeus.srp.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.themorfeus.srp.MainGame;
import net.themorfeus.srp.Resources;
import net.themorfeus.srp.Util;

/**
 * This is displayed when the game is loading
 */
public class LoadingScreen extends ScreenAdapter{

    private MainGame gameInstance;

    private Label.LabelStyle labelStyle;
    private Label loadingLabel;

    public LoadingScreen(MainGame gameInstance){
        this.gameInstance = gameInstance;

        this.labelStyle = new Label.LabelStyle();
        labelStyle.font = Util.getFont(50);
        labelStyle.fontColor = Color.WHITE;
        loadingLabel = new Label("Loading: xxx", labelStyle);

        gameInstance.getStage().addActor(loadingLabel);
    }

    public void render(float delta){
        Resources.manager.update();

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        loadingLabel.setText("Loading: " + (int)(Resources.manager.getProgress() * 100));
        loadingLabel.setPosition(gameInstance.getStage().getWidth()/2 - loadingLabel.getWidth()/2, gameInstance.getStage().getHeight()/2 - loadingLabel.getHeight()/2);
    }

}
