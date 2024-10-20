/*
MIT License

Copyright (c) 2018 BMSTU

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
* */


package zstumpf.learn_javafx2;

import javafx.scene.PerspectiveCamera;

/**
 * Class for 3D space camera presenting.
 * Presents PerspectiveCamera node in a scene with defined
 * far clip, near clip and field of view.
 * W,S,A,D, Ctrl, Space used for movement on a scene.
 * Mouse dragging(while presse) used for rotation on a scene.
 * SHIFT modifier can be used to increase step modifier
 *
 * @author      Vladislav Khakin
 * @version     %I%, %G%
 * @see PerspectiveCamera
 */
public class Camera {
    private XForm xForm;
    private PerspectiveCamera camera;
    private boolean invertRotations = false;

    private final double FIELD_OF_VIEW  = 40.0;
    private final double CAMERA_NEAR_CLIP = 0.1;
    private final double CAMERA_FAR_CLIP = 10000.0;
    private final double CAMERA_MOVEMENT_UNIT = 25;
    private final double Z_POSITION = -1000;


    // return 1 if invertRotations else -1
    private int getRotationDirection(){
        return invertRotations ? 1: -1;
    }

    private void moveUnitForward(){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() + zCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTy(xForm.t.getY() - yCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitBackward(){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() - zCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTy(xForm.t.getY() + yCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitLeft(){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() + zCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitRight(){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * CAMERA_MOVEMENT_UNIT);
        xForm.setTz(xForm.t.getZ() - zCoord * CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitUp(){
        xForm.setTy(xForm.t.getY() - CAMERA_MOVEMENT_UNIT);
    }

    private void moveUnitDown(){
        xForm.setTy(xForm.t.getY() + CAMERA_MOVEMENT_UNIT);
    }


    /**
     * Default constructor for Camera creation.
     * Set field of view, nead and far clips,create
     * scene node and link Perspective camera with it
     * Use defaul rotation (not inversed)
     */
    public Camera(){
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(Z_POSITION);
        camera.setFieldOfView(FIELD_OF_VIEW);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        xForm = new XForm();
        xForm.getChildren().add(camera);
        invertRotations = false;
    }

    /**
     * Generate camera by default constructor and set inversed rotation
     * @see #Camera()
     * @param invertRotations if rotation should be applied with inversion
     */
    public Camera(boolean invertRotations){
        super();
        this.invertRotations = invertRotations;
    }

    /**
     * Return scene node of camera
     * @return XFrom
     */
    public XForm getXForm(){
        return xForm;
    }

    /**
     * Return PerspectiveCamera object in created camera
     * @return Camera base class of PerspectiveCamera
     */
    public javafx.scene.Camera getCamera(){
        return camera;
    }

    /**
     * Move camera forward in it's rotated direction. Straight where it "look's"
     * @param value - how far to move
     */
    public void moveForward(double value){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * value);
        xForm.setTz(xForm.t.getZ() + zCoord * value);
        xForm.setTy(xForm.t.getY() - yCoord * value);
    }

    /**
     * Move camera forward in it's rotated direction. Straight where it "look's"
     * Use default movement unit as step size
     */
    public void moveForward(){
        moveUnitForward();
    }

    /**
     * Move camera backward in it's rotated direction.
     * @param value - how far to move
     */
    public void moveBackward(double value){
        double xCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double yCoord = Math.sin(Math.toRadians(xForm.rx.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * value);
        xForm.setTz(xForm.t.getZ() - zCoord * value);
        xForm.setTy(xForm.t.getY() + yCoord * value);
    }

    /**
     * Move camera backward in it's rotated direction.
     * Use default movement unit as step size
     */
    public void moveBackward(){
        moveUnitBackward();
    }

    /**
     * Move camera left in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveLeft(double value){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() - xCoord * value);
        xForm.setTz(xForm.t.getZ() + zCoord * value);
    }

    /**
     * Move camera left in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveLeft(){
        moveUnitLeft();
    }

    /**
     * Move camera right in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveRight(double value){
        double xCoord = Math.cos(Math.toRadians(xForm.ry.getAngle()));
        double zCoord = Math.sin(Math.toRadians(xForm.ry.getAngle()));
        xForm.setTx(xForm.t.getX() + xCoord * value);
        xForm.setTz(xForm.t.getZ() - zCoord * value);
    }

    /**
     * Move camera right in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveRight(){
        moveUnitRight();
    }

    /**
     * Move camera up in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveUp(double value){
        xForm.setTy(xForm.t.getY() - value);
    }

    /**
     * Move camera up in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveUp(){
        moveUnitUp();
    }

    /**
     * Move camera down in it's rotated direction (without applying rotation)
     * @param value - how far to move
     */
    public void moveDown(double value){
        xForm.setTy(xForm.t.getY() + value);
    }

    /**
     * Move camera down in it's rotated direction (without applying rotation)
     * Use default movement unit as step size
     */
    public void moveDown(){
        moveUnitDown();
    }



    /**
     * Rotate camera around x axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateX(double angle){
        xForm.setRotateX(xForm.rx.getAngle() + getRotationDirection() * angle);

    }

    /**
     * Rotate camera around y axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateY(double angle){
        xForm.setRotateY(xForm.ry.getAngle() - getRotationDirection() * angle);
    }

    /**
     * Rotate camera around z axis on defined angle (in degree)
     * @param angle - rotation angle in degree
     */
    public void rotateZ(double angle){
        xForm.setRotateZ(xForm.rz.getAngle() + getRotationDirection() * angle);

    }

    /**
     * Resets the camera's position and rotation to how it is when the application starts.
     */
    public void resetCamera() {
        xForm.reset();
    }

}