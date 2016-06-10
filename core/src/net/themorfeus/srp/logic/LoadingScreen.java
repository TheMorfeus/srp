package net.themorfeus.srp.logic;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.themorfeus.srp.MainGame;
import net.themorfeus.srp.Resources;
import net.themorfeus.srp.Util;

/**
 * Created by morf on 10.06.2016.
 */
public class LoadingScreen extends ScreenAdapter{

    private MainGame gameInstance;

    private Label.LabelStyle labelStyle;
    private Label loadingLabel;

    public LoadingScreen(MainGame gameInstance){
        this.gameInstance = gameInstance;

        this.labelStyle = new Label.LabelStyle();
        labelStyle.font = Util.getFont(75);
        labelStyle.fontColor = Color.WHITE;
        loadingLabel = new Label("Loading...", labelStyle);

        gameInstance.getStage().addActor(loadingLabel);
    }

    public void render(float delta){
        Resources.manager.update();

        loadingLabel.setText("Loading: " + (int)(Resources.manager.getProgress() * 100));
        loadingLabel.setPosition(gameInstance.getStage().getWidth()/2 - loadingLabel.getWidth()/2, gameInstance.getStage().getHeight()/2 - loadingLabel.getHeight()/2);
    }

}
