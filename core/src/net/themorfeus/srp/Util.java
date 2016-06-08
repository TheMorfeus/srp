package net.themorfeus.srp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import net.themorfeus.srp.render.FrameBufferManager;
import net.themorfeus.srp.render.shaders.ShaderHelper;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by morf on 02.06.2016.
 */
public class Util {


    public static final String FONT_FILE = "bbc.ttf";
    private static HashMap<Integer, BitmapFont> fonts;
    public static BitmapFont getFont(int size){
        if(fonts==null) fonts = new HashMap<Integer, BitmapFont>();
        if(!fonts.containsKey(size)){
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            parameter.genMipMaps = true;
            parameter.spaceY = -size;
            parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
            parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;


            fonts.put(size, generator.generateFont(parameter));
            generator.dispose();
        }

        return fonts.get(size);
    }

    private static HashMap<String, Float[]> colorCache = new HashMap<String, Float[]>();
    public static float[] getColorFromHex(String hex){
        if(hex.startsWith("#"))hex = hex.substring(1);
        if(hex.length()!=6)return new float[]{1,0,0};

        if(colorCache.containsKey(hex)){
            Float[] color = colorCache.get(hex);
            return new float[]{color[0], color[1], color[2]};
        }

        int color;

        try{
            color = Integer.parseInt(hex, 16);
        }catch (Exception e){
            e.printStackTrace();
            return new float[]{1,0,0};
        }

        float red = color >> 16;
        float green = (color & 0xffff)>>8;
        float blue = (color) & 0xff;

        red /= 255;
        green /= 255;
        blue /= 255;
        colorCache.put(hex, new Float[]{red, green, blue});
        return new float[]{red, green, blue};
    }

    public static float clamp(float val, float min, float max){
        if(val<min)return min;
        if(val>max)return max;
        return val;
    }

    public static TextureRegion[] splitUntearableTileset(Texture t, int cellWidth, int cellHeight, int textureWidth, int textureHeight){

        int columns = t.getWidth() / cellWidth;
        int rows = t.getHeight() / cellHeight;

        int gutterX = (cellWidth - textureWidth)/2;
        int gutterY = (cellHeight - textureHeight)/2;

        TextureRegion[] split = new TextureRegion[rows*columns];

        for(int y = 0; y<rows; y++){
            for(int x = 0; x<columns; x++){

                int tX = x * cellWidth;
                int tY = y * cellHeight;

                split[y*rows + x] = new TextureRegion(t, tX+gutterX, tY+gutterY, textureWidth, textureHeight);
            }
        }

        return split;
    }

    public static <E> E choose(E... items){
        return items[new Random().nextInt(items.length)];
    }

    private static Mesh renderQuad;
    public static Texture renderShaderedTextureToTextureUsingBuffer(Texture t, ShaderHelper shader, FrameBuffer buffer){

        if(renderQuad==null)renderQuad = createFullScreenQuad();

        FrameBufferManager.begin(buffer);
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            t.bind(0);
            shader.setTextureID(0);

            shader.begin();{
                Gdx.gl20.glEnable(GL20.GL_BLEND);
                renderQuad.render(shader.getProgram(), GL20.GL_TRIANGLE_FAN);
            }shader.end();
        FrameBufferManager.end();

        return buffer.getColorBufferTexture();
    }

    public static Mesh createFullScreenQuad() {

        float[] verts = new float[20];
        int i = 0;

        verts[i++] = -1; // x1
        verts[i++] = -1; // y1
        verts[i++] = 0;
        verts[i++] = 0f; // u1
        verts[i++] = 0f; // v1

        verts[i++] = 1f; // x2
        verts[i++] = -1; // y2
        verts[i++] = 0;
        verts[i++] = 1f; // u2
        verts[i++] = 0f; // v2

        verts[i++] = 1f; // x3
        verts[i++] = 1f; // y2
        verts[i++] = 0;
        verts[i++] = 1f; // u3
        verts[i++] = 1f; // v3

        verts[i++] = -1; // x4
        verts[i++] = 1f; // y4
        verts[i++] = 0;
        verts[i++] = 0f; // u4
        verts[i++] = 1f; // v4

        Mesh mesh = new Mesh( true, 4, 0,  // static mesh with 4 vertices and no indices
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"));

        mesh.setVertices( verts );
        return mesh;
    }
}
