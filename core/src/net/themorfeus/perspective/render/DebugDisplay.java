package net.themorfeus.perspective.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.themorfeus.perspective.Util;

import java.util.Random;

/**
 * Created by morf on 05.06.2016.
 */
public class DebugDisplay {

    private Stage stage;
    private Label.LabelStyle style;

    private Label debugInfo;

    public DebugDisplay(Stage stage){

        this.style = new Label.LabelStyle();

        style.font = Util.getFont(10);

        debugInfo = new Label("hello\nhi", style);
        debugInfo.setPosition(10,10);

        stage.addActor(debugInfo);
    }

    public void update(String text){
        debugInfo.setText(text);
    }
}
