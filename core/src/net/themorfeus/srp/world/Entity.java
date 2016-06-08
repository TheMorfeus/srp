package net.themorfeus.srp.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import net.themorfeus.srp.MainGame;

import java.util.Random;

/**
 * Created by morf on 02.06.2016.
 */
public class Entity {

    protected MainGame gameInstance;

    protected Decal decal;
    protected Decal shadowDecal;

    protected TextureRegion sprite;
    protected TextureRegion shadow;

    protected float elevation = .5f;

    protected Vector3 position = new Vector3();
    protected Vector3 offset = new Vector3();
    protected Vector3 size = new Vector3();
    protected Vector3 tempV3 = new Vector3();

    protected final Vector3 up = new Vector3(0,1,0);

    private static boolean firstone = false;
    private boolean follow = false;

    private float startY;
    private float amt;
    private float alpha;

    protected boolean displayShadow = true;

    public Entity(MainGame game, TextureRegion t, float x, float y, float width, float height){

        this.gameInstance = game;

        if(width<=.1f)width = .1f;
        if(height<=.1f)height = .1f;

        this.position.set(x , elevation * height, y);
        this.size.set(width/2,height,width/2);

        //this.sprite = new TextureRegion(new Texture(Util.choose("a/char.png","a/test.png","a/elephant.png", "a/hippo.png", "a/panda.png", "a/penguin.png", "a/rabbit.png", "a/giraffe.png", "a/monkey.png", "a/parrot.png", "a/pig.png", "a/snake.png")));
        this.sprite = t;
        this.shadow = new TextureRegion(new Texture("shadow.png"));

        this.decal = Decal.newDecal(width,height,sprite, true);
        this.shadowDecal = Decal.newDecal(2*width/3,2*width/3,shadow, true);

        if(!firstone){
            firstone = true;
            follow = true;
        }

        amt = new Random().nextInt(2) + new Random().nextFloat() + 1;
        startY = position.y;
    }

    public void tick(DecalBatch batch){

        if(follow){

            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                position.x+=.05f;
            }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
                position.x-=.05f;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                position.z-=.05f;
            }else if(Gdx.input.isKeyPressed(Input.Keys.D)){
                position.z+=.05f;
            }

        }

        decal.setTextureRegion(sprite);

        shadowDecal.setPosition(position.x, 0, position.z);
        decal.setPosition(position.cpy().add(offset));

        shadowDecal.setRotation(0, 90,0);
        decal.setRotation(gameInstance.getCamera().direction.cpy().scl(-1), up);
        batch.add(decal);
        if(displayShadow)batch.add(shadowDecal);
    }
}
