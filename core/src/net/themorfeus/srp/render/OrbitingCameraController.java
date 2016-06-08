package net.themorfeus.srp.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Basic class that allows the camera to orbit around a given pivot point.
 * Contains built-in input adapter to handle it.
 * If one were to use an {@link com.badlogic.gdx.InputMultiplexer}, this should be the last InputProcessor added to order.
 *
 * NOTE: the actualXXX values have animation smoothing applied to them (i.e. the ones actually seen by the player)
 *
 * @author themorfeus
 */
public class OrbitingCameraController extends InputAdapter{
    /**
     * Temporary vector used in various calculations.
     * */
    private Vector3 tempV3 = new Vector3();

    /**
     * Animation smoothing. The lower the value, the more jittery the movement gets.
     * */
    public float animSmoothing = 5;

    /**
     * Camera's orbit radius
     * */
    private float orbitRadius = 55;
    private float actualOrbitRadius = 55;

    /**
     * Minimal orbit radius value
     * Set to 0 to ignore
     * */
    private float minimalOrbitRadius = 15;

    /**
     * Maximal orbit radius value
     * Set to 0 to ignore
     * */
    private float maximalOrbitRadius = 500;

    /**
     * Camera's yaw
     * */
    private float yaw = 45;
    private float actualYaw = 45;

    /**
     * Camera's pitch
     * */
    private float pitch = 60;
    private float actualPitch = 60;

    private float minPitch = 10;
    private float maxPitch = 80;


    /**
     * Whether or not allow for camera pitch movement (i.e. up and down)
     * */
    private boolean allowPitchMovement = false;

    /**
     * Whether or not allow for camera yaw movement (i.e. left to right)
     * */
    private boolean allowYawMovement = false;

    /**
     * Camera's pivot point. The point at which the camera is looking at.
     * */
    private Vector3 pivotPoint = new Vector3();
    private Vector3 actualPivotPoint = new Vector3();

    private float touchStartX, touchStartY;
    private float lastTouchX, lastTouchY;

    /**
     * Camera object to control.
     * */
    private Camera camera;

    public OrbitingCameraController(Camera cam){
        this.camera = cam;
    }

    /**
     * An update method
     * */
    public void update(){
        if(animSmoothing<1)animSmoothing = 1;

        if(pitch<minPitch) pitch = minPitch;
        if(pitch>maxPitch) pitch = maxPitch;

        actualPivotPoint.x += (pivotPoint.x - actualPivotPoint.x)/animSmoothing;
        actualPivotPoint.y += (pivotPoint.y - actualPivotPoint.y)/animSmoothing;
        actualPivotPoint.z += (pivotPoint.z - actualPivotPoint.z)/animSmoothing;

        actualYaw += (yaw - actualYaw)/animSmoothing;
        actualPitch += (pitch - actualPitch)/animSmoothing;
        actualOrbitRadius += (orbitRadius - actualOrbitRadius)/animSmoothing;

        if(camera instanceof OrthographicCamera)((OrthographicCamera)camera).zoom = actualOrbitRadius/50;

        camera.position.set(
                actualPivotPoint.x + (float) (Math.cos(Math.toRadians(actualYaw)) * Math.sin(Math.toRadians(actualPitch)) * actualOrbitRadius),
                actualPivotPoint.y + (float) (Math.cos(Math.toRadians(actualPitch)) * actualOrbitRadius),
                actualPivotPoint.z + (float) (Math.sin(Math.toRadians(actualYaw)) * Math.sin(Math.toRadians(actualPitch)) * actualOrbitRadius));
        tempV3 = actualPivotPoint.cpy().sub(camera.position).nor();
        camera.direction.set(tempV3);
    }

    /**
     * @return The camera's pivot point
     * */
    public Vector3 getPivotPoint(){
        return pivotPoint.cpy();
    }

    /**
     * Sets the camera's pivot point
     * @param x Pivot's new X position
     * @param y Pivot's new Y position
     * @param z Pivot's new Z position
     * */
    public void setPivotPoint(float x, float y, float z){
        this.pivotPoint.set(x,y,z);
    }

    /**
     * Sets the camera's pivot point
     * @param pivot The new pivot position
     * */
    public void setPivotPoint(Vector3 pivot){
        this.pivotPoint.set(pivot);
    }

    /**
     * Sets the camera's yaw
     * @param yaw New camera yaw
     * */
    public void setYaw(float yaw){
        this.yaw = yaw;
    }

    /**
     * Adds a value to the camera's yaw
     * @param yaw The value to add to yaw
     * */
    public void addYaw(float yaw){
        this.yaw += yaw;
    }

    /**
     * Sets the camera's pitch
     * @param pitch New camera pitch
     * */
    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    /**
     * Adds a value to the camera's pitch
     * @param pitch The value to add to camera's pitch
     * */
    public void addPitch(float pitch){
        this.pitch += pitch;
    }

    /**
     * @return Current orbit radius of the camera
     * */
    public float getOrbitRadius(){
        return orbitRadius;
    }

    /**
     * @return Current yaw of the camera
     * */
    public float getYaw(){
        return yaw;
    }

    /**
     * @return Current pitch of the camera
     * */
    public float getPitch(){
        return pitch;
    }

    /**
     * Sets wheter or not pitch movement is allowed
     * */
    public void setAllowPitchMovement(boolean allow){
        this.allowPitchMovement = allow;
    }

    /**
     * Sets wheter or not yaw movement is allowed
     * */
    public void setAllowYawMovement(boolean allow){
        this.allowYawMovement = allow;
    }

    /**
     * @return false, for further input handling
     * */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchStartX = screenX;
        touchStartY = Gdx.graphics.getHeight() - screenY;
        lastTouchX = touchStartX;
        lastTouchY = touchStartY;
        return false;
    }

    /**
     * @return false, for further input handling
     * */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            ///something here
        }else if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){

            float dx = lastTouchX - screenX;
            float dy = lastTouchY - (Gdx.graphics.getHeight() - screenY);

            tempV3.set(camera.direction).crs(0,1,0).nor().scl(dx/60f).scl(orbitRadius * .02f);
            pivotPoint.x += tempV3.x;
            pivotPoint.z += tempV3.z;

            tempV3.set(camera.direction).nor().scl(dy/20f).scl(orbitRadius * .02f);
            pivotPoint.x += tempV3.x;
            pivotPoint.z += tempV3.z;

        }else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){

            if(allowPitchMovement)pitch -= (lastTouchY - (Gdx.graphics.getHeight() - screenY))/10;
            if(allowYawMovement)yaw -= (lastTouchX - screenX)/5f;

        }

        lastTouchX = screenX;
        lastTouchY = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    /**
     * @return false, for further input handling
     * */
    @Override
    public boolean scrolled(int amount) {
        orbitRadius += (amount * orbitRadius * .02f)/.25f;
        if(orbitRadius<minimalOrbitRadius && minimalOrbitRadius!=0)orbitRadius = minimalOrbitRadius;
        if(orbitRadius>maximalOrbitRadius&& maximalOrbitRadius!=0)orbitRadius = maximalOrbitRadius;
        return false;
    }
}
