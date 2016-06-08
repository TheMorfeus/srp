package net.themorfeus.srp.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import net.themorfeus.srp.MainGame;
import net.themorfeus.srp.Util;
import net.themorfeus.srp.render.FrameBufferManager;

public class PopupEntity extends Entity{

    private static final int POPUP_WIDTH = 2048, POPUP_HEIGHT = 1024;

    private static final int FONT_SIZE = 75;

    private FrameBuffer popupFrameBuffer;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private BitmapFont font;
    private GlyphLayout layout;

    private float oscillationAlpha = 0;

    public PopupEntity(MainGame g, float x, float y){
        super(g, new TextureRegion(new Texture("popup.png")), x, y, 4, 4 * (POPUP_HEIGHT/(float)POPUP_WIDTH));
        this.offset.x-=1f;
        this.position.y = 2.25f * (POPUP_HEIGHT/1024f);

        this.displayShadow = false;

        this.popupFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, POPUP_WIDTH, POPUP_HEIGHT, false);

        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();

        this.shapeRenderer.setAutoShapeType(true);

        this.font = Util.getFont(FONT_SIZE);
        font.getData().markupEnabled = true;


        this.layout = new GlyphLayout(font, "");

        sprite.flip(false, true);
    }

    public void tick(DecalBatch decalBatch){


        tempV3.set(gameInstance.getCamera().direction);
        tempV3.crs(0,-1,0).nor().scl((1f * (POPUP_HEIGHT / 512f)) * .75f);

        this.offset.x = tempV3.x;
        this.offset.y = (float) ((Math.sin(Math.toRadians(oscillationAlpha))+1)/20f);
        this.offset.z = tempV3.z;

        oscillationAlpha+=2f;

        /**
         * ANY TEXT MANIPULATION MUST GO HERE
         * */

        layout.setText(font, "[WHITE]Tony Bamanaboni", Color.WHITE, POPUP_WIDTH - 50, Align.right, true);


        FrameBufferManager.begin(popupFrameBuffer);
        {

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //Gdx.gl.glColorMask(true, true, true, true);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            //Gdx.gl.glDisable(GL20.GL_BLEND);


            shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, POPUP_WIDTH, POPUP_HEIGHT);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(1, 1, 1, 0.65f);
            shapeRenderer.rect(POPUP_WIDTH - layout.width - 50, 0, layout.width + 50, layout.height + (2 * FONT_SIZE / 3) + 25);

            shapeRenderer.end();

            batch.getProjectionMatrix().setToOrtho2D(0, 0, POPUP_WIDTH, POPUP_HEIGHT);
            batch.begin();

            font.draw(batch, layout, 25, layout.height + (2 * FONT_SIZE / 3));

            batch.end();

        }FrameBufferManager.end();

        popupFrameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        sprite.setTexture(popupFrameBuffer.getColorBufferTexture());
        super.tick(decalBatch);
    }
}
