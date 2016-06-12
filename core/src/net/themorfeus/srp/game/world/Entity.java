package net.themorfeus.srp.game.world;

import com.badlogic.gdx.math.Vector3;
import net.themorfeus.srp.SuperRenderPass;

/**
 * Created by morf on 11.06.2016.
 */
public abstract class Entity {

    public static Vector3 tempV3 = new Vector3();

    protected Vector3 position = new Vector3();
    protected Vector3 size = new Vector3();

    protected SuperRenderPass gameInstance;

    public Entity(SuperRenderPass g){
        this.gameInstance = g;
    }

    public abstract void tick();
}
