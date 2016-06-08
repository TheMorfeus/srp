package net.themorfeus.perspective.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.sun.org.apache.bcel.internal.generic.POP;
import net.themorfeus.perspective.MainGame;
import net.themorfeus.perspective.Util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by morf on 02.06.2016.
 */
public class World {

    private MainGame gameInstance;

    private int width, height;
    private int[] grid;
    private Decal[] decalGrid;

    public static TextureRegion[] tiles;

    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public World(MainGame g, int width, int height){

        this.gameInstance = g;

        this.width = width;
        this.height = height;
        grid = new int[width * height];
        decalGrid = new Decal[width * height];

        tiles = Util.splitUntearableTileset(new Texture("tileset.png"), 66, 66, 64, 64);

        Random r = new Random();
        for(int x = 0; x<width; x++){
            for(int y = 0; y<height; y++){

//                if((x<2 || y<2) || (x>width-3 || y>width-3)){
//                    if(r.nextInt(10) == 0){
//                        grid[y*width + x] = 0;
//                    }else{
//                        grid[y*width + x] = r.nextInt(4)+5;
//                    }
//                }
//                else if(x<6 || y<6 || (x>width-7 || y>width-7))grid[y*width + x] = r.nextInt(8)+1;
//                else grid[y*width + x] = r.nextInt(4)+1;
                grid[y*width + x] = r.nextInt(4)+1;
            }
        }

        for(int i = 0; i<r.nextInt((width * height)*2); i++){
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            TextureRegion sprite = new TextureRegion(new Texture(Util.choose("shitwaddup.png", "a/char.png","a/test.png","a/elephant.png", "a/hippo.png", "a/panda.png", "a/penguin.png", "a/rabbit.png", "a/giraffe.png", "a/monkey.png", "a/parrot.png", "a/pig.png", "a/snake.png")));
            Entity e = new Entity(gameInstance, sprite,x,y,1,1);
            entities.add(e);
        }

        entities.add(new Entity(gameInstance, new TextureRegion(new Texture("a/test.png")),(int)Math.floor(width/2), (int)Math.floor(height/2), 1, 1));
        entities.add(new PopupEntity(gameInstance, (int)Math.floor(width/2), (int)Math.floor(height/2)));
    }

    public void render(DecalBatch groundBatch, DecalBatch batch){
        for(int x = 0; x<width; x++){
            for(int y = 0; y<height; y++){
                int id = grid[y*height + x];
                if(id==0)continue;

                if(decalGrid[y*height + x]==null){
                    decalGrid[y*height + x] = Decal.newDecal(1,1, tiles[id-1], false);
                }

                Decal tile = decalGrid[y*height + x];
                tile.setPosition(x, -.03f, y);
                tile.setPosition(x, 0, y);
                tile.setRotation(0, 90, 0);
                groundBatch.add(tile);

            }
        }

        for(Entity e:entities){
            e.tick(batch);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
