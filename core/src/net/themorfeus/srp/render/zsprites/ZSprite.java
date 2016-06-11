package net.themorfeus.srp.render.zsprites;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import net.themorfeus.srp.Util;

/**
 * Replacement for Decals, since they do not cope well with the environment.
 */
public class ZSprite{

    /** Temporary vector for various calculations. */
    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();

    private float width, height;
    private TextureRegion region;

    private Vector3 position = new Vector3();
    private Quaternion rotation = new Quaternion();

    private int srcBlendFunc, dstBlendFunc;
    private Model model;
    private ModelInstance modelInstance;

    private boolean updated;

    public ZSprite(float width, float height, TextureRegion region){
        this(width, height, region, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public ZSprite(float width, float height, TextureRegion region, int srcBlendFunc, int dstBlendFunc){
        this.width = width;
        this.height = height;
        this.region = region;

        this.srcBlendFunc = srcBlendFunc;
        this.dstBlendFunc = dstBlendFunc;

        recalcModel();
    }

    private void recalcModel(){
        model = Util.createPlaneModel(
                width,
                height,
                new Material(new TextureAttribute(TextureAttribute.Diffuse, region), new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA), new IntAttribute(IntAttribute.CullFace, GL20.GL_NONE)),
                0,0,
                1,1);
        modelInstance = new ModelInstance(model);
    }

    private void recalcTexture(){
        model.materials.get(0).remove(TextureAttribute.Diffuse);
        model.materials.get(0).set(new TextureAttribute(TextureAttribute.Diffuse, region));
        modelInstance = new ModelInstance(model);
        updated = false;
        //model.nodes.get(0).get
    }

    public void setRotation (float yaw, float pitch, float roll) {
        rotation.setEulerAngles(yaw, pitch, roll);
        this.updated = false;
    }

    public void setRotation (Vector3 dir, Vector3 up) {
        tmp.set(up).crs(dir).nor();
        tmp2.set(dir).crs(tmp).nor();
        rotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);
        this.updated = false;
    }

    public void setPosition(float x, float y, float z){
        this.position.set(x,y,z);
        this.updated = false;
    }

    public void setPosition(Vector3 position){
        this.setPosition(position.x, position.y, position.z);
    }

    public ModelInstance getModelInstance(){
        if(!updated) {
            modelInstance.transform.setToTranslation(position);
            modelInstance.transform.rotate(rotation);
            modelInstance.calculateTransforms();
            updated = true;
        }
        return modelInstance;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public TextureRegion getTextureRegion() {
        return region;
    }

    public void setTextureRegion(TextureRegion r){
        this.region = r;
        recalcTexture();
    }
}
